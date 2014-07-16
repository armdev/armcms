package frontend.web.listeners;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Armen Arzumanyan
 */
@ManagedBean(name = "SessionController")
@SessionScoped
public class SessionController implements Serializable {

    private FacesContext context;
    private ExternalContext ex;   
    private UserContext userContext;

    public SessionController() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
    }

    public Long getLoggedUserId() {
//        if(userContext == null){
//            return null;
//        }
        return userContext.getId();
    }

    public String getEmail() {
        return userContext.getEmail();
    }

    public String getUsername() {
        return userContext.getUsername();
    }

    public String getLastname() {
        return userContext.getLastname();
    }

    public String getFirstName() {
        return userContext.getFirstname();
    }

    public Integer getStatus() {
        return userContext.getStatus();
    }

    public Integer getUserRole() {       
        return userContext.getUserRole();
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(UserContext context) {
        userContext = context;
    }
}