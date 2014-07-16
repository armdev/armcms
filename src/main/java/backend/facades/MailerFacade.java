package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.MailerEntity;
import backend.entities.UserEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "MailerFacade")
@ApplicationScoped
public class MailerFacade implements Serializable {

    private final String MAIL_HEADER = "X-Mailer";
    private static Properties sessionProperties = null;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public MailerFacade() {
    }

    public void addConfig() {
        MailerEntity mailerEntity = new MailerEntity();
        try {
            BasicDBObject document = new BasicDBObject();
            mailerEntity.setId(Util.longValue(getNextId(initBean.getDatabase(), "mailConfigSeqGen")));
            document.put("id", mailerEntity.getId());
            document.put("email", "no-reply@musescriptum.com");
            document.put("host", "smtpout.secureserver.net");
            document.put("from_address", "no-reply@musescriptum.com");
            document.put("port_socket", "465");
            document.put("port_smtp", "465");
            document.put("passwd", "AwLmIz5MibZ");
            initBean.getMalerConfiguration().insert(document, WriteConcern.SAFE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMailToSubscribers(List<UserEntity> list, String subject, String plainBody) {
        try {
            for (int i = 0; i < list.size(); i++) {
                String to = list.get(i).getEmail();
                this.sendMessage(to, null, null, subject, plainBody, plainBody);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
    }

    public void updateMailerProperties(MailerEntity mailerEntity) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", mailerEntity.getId());
            document.put("host", mailerEntity.getHost());
            document.put("from_address", mailerEntity.getFromAddress());
            document.put("passwd", mailerEntity.getPasswd());
            document.put("email", mailerEntity.getEmail());
            document.put("port_socket", mailerEntity.getSocketPort());
            document.put("port_smtp", mailerEntity.getSmtpPort());
            initBean.getMalerConfiguration().update(new BasicDBObject().append("id", mailerEntity.getId()), document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MailerEntity getMailerProperties() {
        MailerEntity properties = null;
        DBObject mailer = initBean.getMalerConfiguration().findOne();
        try {
            if (mailer != null) {
                properties = new MailerEntity();
                properties.setId((Long) mailer.get("id"));
                properties.setHost((String) mailer.get("host"));
                properties.setFromAddress((String) mailer.get("from_address"));
                properties.setPasswd((String) mailer.get("passwd"));
                properties.setEmail((String) mailer.get("email"));
                properties.setSocketPort((String) mailer.get("port_socket"));
                properties.setSmtpPort((String) mailer.get("port_smtp"));
            }
        } catch (Exception e) {
        }
        return properties;
    }

    public void sendMessage(String messageTo, String messageCc, String messageBcc, String subject, String plainBody, String htmlBody)
            throws MessagingException {
        final MailerEntity mailprops = this.getMailerProperties();
        if (mailprops == null) {
            return;
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", mailprops.getHost());
        props.put("mail.smtp.socketFactory.port", mailprops.getSocketPort());
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", mailprops.getSmtpPort());

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailprops.getEmail(), mailprops.getPasswd());
                    }
                });

        MimeMessage message = new MimeMessage(session);

        boolean parseStrict = false;
        // set from whom message is
        message.setFrom(new InternetAddress(mailprops.getFromAddress()));
        // set recipients of message
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(messageTo, parseStrict));
        // set cc
        if (messageCc != null && messageCc.length() > 0) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(messageCc, parseStrict));
        }
        // set bcc
        if (messageBcc != null && messageBcc.length() > 0) {
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(messageBcc, parseStrict));
        }


        try {
            message.setSentDate(new Date());
            Multipart multiPart = new MimeMultipart("alternative");
            message.setSentDate(new Date());
            BodyPart plainBodyPart = new MimeBodyPart();
            plainBodyPart.setText(plainBody);
            plainBodyPart.setHeader("Content-Type", "text/plain; charset=UTF-8");
            plainBodyPart.setHeader("Content-Transfer-Encoding", "8bit");

            MimeBodyPart htmlBodyPart = new MimeBodyPart();
            htmlBodyPart.setText(htmlBody, "UTF-8");
            htmlBodyPart.setHeader("Content-Type", "text/html; charset=UTF-8");
            htmlBodyPart.setHeader("Content-Transfer-Encoding", "8bit");

            multiPart.addBodyPart(plainBodyPart);
            multiPart.addBodyPart(htmlBodyPart);

            message.setContent(multiPart, "UTF-8");

            message.setHeader("X-Mailer", MAIL_HEADER);
            message.setHeader("Content-Transfer-Encoding", "quoted-printable");
            message.setSubject(subject, "UTF-8");
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }

    protected static String getNextId(DB db, String seq_name) {
        String sequence_collection = "seq"; // the name of the sequence collection
        String sequence_field = "seq"; // the name of the field which holds the sequence


        DBCollection seq = db.getCollection(sequence_collection); // get the collection (this will create it if needed)               

        if (seq == null) {
            seq = db.createCollection(sequence_collection, null);
        }

        // this object represents your "query", its analogous to a WHERE clause in SQL
        DBObject query = new BasicDBObject();
        query.put("id", seq_name); // where id = the input sequence name

        // this object represents the "update" or the SET blah=blah in SQL
        DBObject change = new BasicDBObject(sequence_field, 1);
        DBObject update = new BasicDBObject("$inc", change); // the $inc here is a mongodb command for increment

        // Atomically updates the sequence field and returns the value for you
        DBObject res = seq.findAndModify(query, new BasicDBObject(), new BasicDBObject(), false, update, true, true);
        return res.get(sequence_field).toString();
    }
}
