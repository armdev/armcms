package frontend.web.listeners;



import frontend.web.utils.MessageFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Armen Arzumanyan
 */
@FacesValidator("PasswordValidator")
public class PasswordValidator implements Validator {

  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    String passwordId = (String) component.getAttributes().get("passwordId");
    UIInput passwordInput = (UIInput) context.getViewRoot().findComponent(passwordId);
    String password = (String) passwordInput.getValue();
    String confirm = (String) value;
    if (password != null && password.length() != 0 && !password.equals(confirm)) {
      MessageFactory msg = new MessageFactory();
      FacesMessage errMsg = new FacesMessage(msg.getMessage("errorPasswordConfirm"));
      throw new ValidatorException(errMsg);

    }
  }
}
