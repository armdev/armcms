/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.web.beans.users;

import backend.entities.UserEntity;
import backend.facades.UserManagerFacade;
import backend.types.StatusTypes;
import java.io.IOException;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "reminder")
@RequestScoped
public class RemindPassword {

    @ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userManager;
    private String email;
    private String check = null;
    private Integer inputValue = null;
    private Integer randomValue = Math.abs(random.nextInt()) % 9999;
    private static Random random = new Random();

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
    private UserEntity userentity;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RemindPassword() {

        userentity = new UserEntity();
    }

    public UserEntity getUserentity() {
        return userentity;
    }

    public void setUserentity(UserEntity userentity) {
        this.userentity = userentity;
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

    public static Random getRandom() {
        return random;
    }

    public static void setRandom(Random random) {
        RemindPassword.random = random;
    }

    public String sendLinkForChangingPassword() throws IOException {
        if (inputValue != null && !inputValue.equals(randomValue)) {
            randomValue = Math.abs(random.nextInt()) % 9999;
            return null;
        }
        String navigation = null;
        try {
            UserEntity user = (UserEntity) userManager.getUserByEmail(getEmail());
            if (user == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("errorpages/nouser.jsf");
            } else if (user != null && user.getStatus().equals(StatusTypes.USER_ENABLED)) {//and activ        
                userManager.sendForgotPasswordLink(getEmail());
                FacesContext.getCurrentInstance().getExternalContext().redirect("errorpages/active.jsf");
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("errorpages/notactive.jsf");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return navigation;
    }

    public void setUserManager(UserManagerFacade userManager) {
        this.userManager = userManager;
    }
}
