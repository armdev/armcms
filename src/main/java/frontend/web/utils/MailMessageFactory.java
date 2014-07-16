package frontend.web.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author Armen Arzumanyan
 */
public class MailMessageFactory {

    private ResourceBundle bundle;
    private Locale locale;

    public MailMessageFactory() {
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        bundle = ResourceBundle.getBundle("MailNotifications");
    }

    public String getMessage(String key) {
        return bundle.getString(key);
    }

    public String getMessage(String key, Object arg1) {
        return getMessage(key, new Object[]{arg1});
    }

    public String getMessage(String key, Object arg1, Object arg2) {
        return getMessage(key, new Object[]{arg1, arg2});
    }

    public String getMessage(String key, Object[] args) {
        if (args == null || args.length == 0) {
            return bundle.getString(key);
        }
        MessageFormat fmt = new MessageFormat(bundle.getString(key));
        fmt.setLocale(locale);
        return fmt.format(args).toString();
    }
}