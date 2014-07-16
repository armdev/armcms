package frontend.web.beans.config.general;

import backend.entities.GeneralSettingsEntity;
import backend.facades.GeneralSettingsFacade;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "GeneralConfigBean")
@ViewScoped
public class GeneralConfigBean implements Serializable {

    @ManagedProperty("#{GeneralSettingsFacade}")
    private GeneralSettingsFacade generalSettingsFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private GeneralSettingsEntity generalSettingsEntity;
    private ResourceBundle bundle;
    private Locale locale;

    public GeneralConfigBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        generalSettingsEntity = generalSettingsFacade.getSettings();
        if (generalSettingsEntity == null) {
            generalSettingsEntity = new GeneralSettingsEntity();
        }
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    public String updateConfigs() {
        generalSettingsFacade.updateSettings(generalSettingsEntity);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("success"), bundle.getString("success")));
        return null;
    }

    public void setGeneralSettingsFacade(GeneralSettingsFacade generalSettingsFacade) {
        this.generalSettingsFacade = generalSettingsFacade;
    }

    public GeneralSettingsEntity getGeneralSettingsEntity() {
        return generalSettingsEntity;
    }

    public void setGeneralSettingsEntity(GeneralSettingsEntity generalSettingsEntity) {
        this.generalSettingsEntity = generalSettingsEntity;
    }
}
