package frontend.web.beans.config.mail;

import backend.entities.MailerEntity;
import backend.facades.MailerFacade;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "MailConfigBean")
@ViewScoped
public class MailConfigBean implements Serializable {

    @ManagedProperty("#{MailerFacade}")
    private MailerFacade mailerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private MailerEntity mailerEntity;
    private ResourceBundle bundle;
    private Locale locale;

    public MailConfigBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        mailerEntity = mailerFacade.getMailerProperties();
        if (mailerEntity == null) {
            mailerEntity = new MailerEntity();
        }
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    public String updateConfigs() {
        mailerFacade.updateMailerProperties(mailerEntity);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("settings"), bundle.getString("settings")));
        return null;
    }

    public MailerEntity getMailerEntity() {
        return mailerEntity;
    }

    public void setMailerEntity(MailerEntity mailerEntity) {
        this.mailerEntity = mailerEntity;
    }

    public void setMailerFacade(MailerFacade mailerFacade) {
        this.mailerFacade = mailerFacade;
    }
}
