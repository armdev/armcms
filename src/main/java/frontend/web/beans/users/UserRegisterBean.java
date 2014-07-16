package frontend.web.beans.users;

import backend.entities.UserEntity;
import backend.facades.UserManagerFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "UserRegisterBean")
@ViewScoped
public class UserRegisterBean implements Serializable {

    @ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userManagerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private UserEntity userEntity;
    private String password;
    private Long userId;
    private boolean checkEmail = false;
    private Integer randomValue = Math.abs(random.nextInt()) % 9999;
    private static Random random = new Random();
    private ResourceBundle bundle;
    private Integer inputValue = null;

    public UserRegisterBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        userEntity = new UserEntity();
    }

    @PostConstruct
    public void init() {
    }

    public String addNewUser() {
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
        
        if (inputValue != null && !inputValue.equals(randomValue)) {
            randomValue = Math.abs(random.nextInt()) % 9999;
            FacesMessage msg = new FacesMessage(bundle.getString("securcodewrong"), bundle.getString("securcodewrong"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        boolean check = userManagerFacade.checkEmail(userEntity.getEmail());
        if (check) {
            setCheckEmail(true);
            return null;
        }
        userEntity.setPasswd(password);
        userManagerFacade.registerNewUser(userEntity);
        return "success";
    }

    public Integer getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(Integer randomValue) {
        this.randomValue = randomValue;
    }

    public Integer getInputValue() {
        return inputValue;
    }

    public void setInputValue(Integer inputValue) {
        this.inputValue = inputValue;
    }
    
    public boolean isCheckEmail() {
        return checkEmail;
    }

    public void setCheckEmail(boolean checkEmail) {
        this.checkEmail = checkEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    private static void redirect(FacesContext facesContext, String url) {
        try {
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }

    public void setUserManagerFacade(UserManagerFacade userManagerFacade) {
        this.userManagerFacade = userManagerFacade;
    }
}
