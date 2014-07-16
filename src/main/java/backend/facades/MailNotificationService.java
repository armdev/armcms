package backend.facades;

import backend.entities.GeneralSettingsEntity;
import frontend.web.utils.MailMessageFactory;
import java.text.MessageFormat;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */

@ManagedBean(name = "MailNotificationService")
@ApplicationScoped
public class MailNotificationService {

    private MailMessageFactory bundle;
    @ManagedProperty("#{MailerFacade}")
    private MailerFacade mailerFacade;
    @ManagedProperty("#{GeneralSettingsFacade}")
    private GeneralSettingsFacade generalSettingsFacade;

    public MailNotificationService() {
        bundle = new MailMessageFactory();
    }

    public void sendActivationLink(Long userId, String email, String key) {
        try {
            GeneralSettingsEntity site = generalSettingsFacade.getSettings();
            String activationLink = site.getSiteAddress()+ "/" + "ActivateAccount?uid=" + userId + "&key=" + key;
            String to = email;
            String subject = bundle.getMessage("mailnotifications.register.activation.subject");
            String plainBodyFormat = bundle.getMessage("mailnotifications.register.activation.plainbody");
            String htmlBodyFormat = bundle.getMessage("mailnotifications.register.activation.htmlbody");
            String plainBody = MessageFormat.format(plainBodyFormat, activationLink);
            String htmlBody = MessageFormat.format(htmlBodyFormat, activationLink);
            mailerFacade.sendMessage(to, null, null, subject, plainBody, htmlBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendForgotPasswordLink(Long userId, String email, String key) {
        try {
            GeneralSettingsEntity site = generalSettingsFacade.getSettings();
            String activationLink = site.getSiteAddress()+ "/" + "ForgotPassword?uid=" + userId + "&key=" + key;
            String to = email;
            String subject = bundle.getMessage("mailnotifications.register.forgotpassword.subject");
            String plainBodyFormat = bundle.getMessage("mailnotifications.register.forgotpassword.plainbody");
            String htmlBodyFormat = bundle.getMessage("mailnotifications.register.forgotpassword.htmlbody");
            String plainBody = MessageFormat.format(plainBodyFormat, activationLink);
            String htmlBody = MessageFormat.format(htmlBodyFormat, activationLink);
            mailerFacade.sendMessage(to, null, null, subject, plainBody, htmlBody);
        } catch (Exception e) {
        }

    }

    public void setGeneralSettingsFacade(GeneralSettingsFacade generalSettingsFacade) {
        this.generalSettingsFacade = generalSettingsFacade;
    }

    public void setMailerFacade(MailerFacade mailerFacade) {
        this.mailerFacade = mailerFacade;
    }
}
