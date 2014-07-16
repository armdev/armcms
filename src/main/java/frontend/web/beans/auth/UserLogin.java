package frontend.web.beans.auth;

import backend.entities.UserEntity;
import backend.facades.UserManagerFacade;
import backend.types.StatusTypes;
import frontend.web.listeners.SessionController;
import frontend.web.listeners.UserContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "UserLogin")
@ViewScoped
public class UserLogin implements Serializable {

    private FacesContext context = null;
    private ExternalContext ex = null;
    private String email;
    private String password;
    @ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userFacade = null;
    private Integer inputValue = null;
    private Integer randomValue = Math.abs(random.nextInt()) % 9999;
    private static Random random = new Random();
    private ResourceBundle bundle;

    public UserLogin() {
    }

    public String loginUser() {
        bundle = ResourceBundle.getBundle("ConfirmationMessages");       
        if (inputValue != null && !inputValue.equals(randomValue)) {
            randomValue = Math.abs(random.nextInt()) % 9999;
            FacesMessage msg = new FacesMessage(bundle.getString("securcodewrong"), bundle.getString("securcodewrong"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        ExternalContext facesContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = facesContext.getSessionMap();
        sessionMap.remove("expired");
        String navigation = null;

        try {
            UserEntity user = userFacade.userLogin(email, password);

            if (user == null) {
                FacesMessage msg = new FacesMessage(bundle.getString("notuser"), bundle.getString("notuser"));
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
            if (user != null) {
                UserContext userContext = (UserContext) facesContext.getSessionMap().get("UserContext");
                if (userContext == null) {
                    userContext = new UserContext();
                    sessionMap.put("UserContext", userContext);
                }
                SessionController sessionController = (SessionController) sessionMap.get("SessionController");

                if (sessionController == null) {
                    sessionController = new SessionController();
                    sessionMap.put("SessionController", sessionController);
                }
                userContext.setId(user.getId());
                userContext.setEmail(user.getEmail());
                userContext.setFirstname(user.getFirstname());
                userContext.setLastname(user.getLastname());
                userContext.setStatus(user.getStatus());
                userContext.setUserRole(user.getUserRole());
                sessionController.setUserContext(userContext);
                if (user.getUserRole() != null && user.getUserRole().equals(StatusTypes.SITE_ADMIN)) {
                    navigation = "mainpanel";
                } else if (user.getUserRole().equals(StatusTypes.REGULAR_USER)) {
                    navigation = "index";
                }
            }
        } catch (Exception e) {
        }
        return navigation;
    }

    public Integer getInputValue() {
        return inputValue;
    }

    public void setInputValue(Integer inputValue) {
        this.inputValue = inputValue;
    }

    public Integer getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(Integer randomValue) {
        this.randomValue = randomValue;
    }

    public void setUserFacade(UserManagerFacade userFacade) {
        this.userFacade = userFacade;
    }

    private static void redirect(FacesContext facesContext, String url) {
        try {
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
