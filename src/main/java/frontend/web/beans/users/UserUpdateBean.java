package frontend.web.beans.users;

import backend.entities.UserEntity;
import backend.facades.UserManagerFacade;
import frontend.web.utils.Util;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "UserUpdateBean")
@ViewScoped
public class UserUpdateBean implements Serializable {

    @ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userManagerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private UserEntity userEntity;
    private String password;
    private Long userId;

    public UserUpdateBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();

    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("userId")) {
            userId = Util.longValue(ex.getRequestParameterMap().get("userId"));
        }
        userEntity = userManagerFacade.getUserById(userId);
    }

    public String updateUser() {
        userEntity.setId(userId);
        userEntity.setPasswd(password);
        userManagerFacade.updateUserProfile(userEntity);
        return "userlist?faces-redirect=true";
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
