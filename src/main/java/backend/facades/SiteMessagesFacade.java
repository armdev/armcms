package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.UserEntity;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "SiteMessagesFacade")
@ApplicationScoped
public class SiteMessagesFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{MailerFacade}")
    private MailerFacade mailerFacade;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public SiteMessagesFacade() {
    }

//    public void addSubscribers(SubscribersEntity subscribersEntity) {
//        try {
//            BasicDBObject document = new BasicDBObject();
//            subscribersEntity.setId(Util.longValue(getNextId(initBean.getDatabase(), "subsEntityGen")));
//            document.put("id", subscribersEntity.getId());
//            document.put("email", subscribersEntity.getEmail());
//            document.put("status", StatusTypes.ACCEPT_SUBSCRB);
//            initBean.getSubscribersColl().insert(document, WriteConcern.SAFE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public List<UserEntity> getSubscribersList() {
        UserEntity subscibers = null;
        List<UserEntity> list = new ArrayList<UserEntity>();
        BasicDBObject query = new BasicDBObject();
        query.put("subscribe", StatusTypes.ACCEPT_SUBSCRB);
        DBCursor cursor = initBean.getUserCollection().find(query);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                subscibers = new UserEntity();
                subscibers.setId((Long) document.get("id"));
                subscibers.setStatus((Integer) document.get("status"));
                subscibers.setEmail((String) document.get("email"));
                list.add(subscibers);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public void sendMailToSubscribers(String subject, String body) {
        List<UserEntity> sublist = new Vector<UserEntity>();
        try {
            List<UserEntity> list = this.getSubscribersList();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    UserEntity result = list.get(i);
                    UserEntity subs = new UserEntity();
                    subs.setId(result.getId());
                    subs.setEmail(result.getEmail());
                    sublist.add(subs);
                    mailerFacade.sendMailToSubscribers(sublist, subject, body);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setMailerFacade(MailerFacade mailerFacade) {
        this.mailerFacade = mailerFacade;
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
