package frontend.web.beans.users;

import backend.entities.UserEntity;
import backend.facades.UserManagerFacade;
import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Armen Arzumanyan
 */
@ManagedBean(name = "ChangePassword")
@RequestScoped
public class ChangePassword {

    @ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userManager;
    private String password;
    private FacesContext context;
    private ExternalContext ex;
    private Long userId;

    public ChangePassword() {
        if ((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId") != null) {
            userId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        }
    }

    public void init() {
        if (userId == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("fail.jsf?fail");
            } catch (IOException ex) {
                //Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        UserEntity user = userManager.getUserById(userId);
        if (user == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("fail.jsf?fail");
            } catch (IOException io) {
                //Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String doChangePassword() throws IOException {
        if (userId != null && password != null) {
            userManager.updatePassword(userId, password);
            userManager.removeForgotPasswordKey(userId);
            return "index";
            //FacesContext.getCurrentInstance().getExternalContext().redirect("../site/index.jsf?changed=true");
        }
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserManager(UserManagerFacade userManager) {
        this.userManager = userManager;
    }
}
