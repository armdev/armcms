package frontend.web.beans.mail;

import backend.facades.SiteMessagesFacade;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "MailSender")
@ViewScoped
public class MailSender implements Serializable {

    @ManagedProperty("#{SiteMessagesFacade}")
    private SiteMessagesFacade siteMessagesFacade;    
    private FacesContext context = null;
    private ExternalContext ex = null;
    private String subject;
    private String body;
    private ResourceBundle bundle;

    public MailSender() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    } 

    public String sendMessageToSubs() {
        siteMessagesFacade.sendMailToSubscribers(subject, body);
        subject = "";
        body = "";
        FacesMessage msg = new FacesMessage(bundle.getString("messagesent"), bundle.getString("messagesent"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return null;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public SiteMessagesFacade getSiteMessagesFacade() {
        return siteMessagesFacade;
    }

    public void setSiteMessagesFacade(SiteMessagesFacade siteMessagesFacade) {
        this.siteMessagesFacade = siteMessagesFacade;
    }

  
}
