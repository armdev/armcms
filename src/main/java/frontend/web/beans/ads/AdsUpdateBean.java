package frontend.web.beans.ads;

import backend.entities.AdsEntity;
import backend.facades.AdsManagerFacade;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "AdsUpdateBean")
@ViewScoped
public class AdsUpdateBean implements Serializable {

    @ManagedProperty("#{AdsManagerFacade}")
    private AdsManagerFacade adsManagerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private AdsEntity adsEntity;
    private Long adsId;

    public AdsUpdateBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        adsEntity = new AdsEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            adsId = Util.longValue(ex.getRequestParameterMap().get("next"));
        }
        adsEntity = adsManagerFacade.getAdsById(adsId);
    }

    public String update() {
        adsEntity.setId(adsId);
        adsManagerFacade.updateAds(adsEntity);
        FacesMessage msg = new FacesMessage(bundle.getString("adssuccess"), bundle.getString("adssuccess"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return null;
    }

    public void setAdsManagerFacade(AdsManagerFacade adsManagerFacade) {
        this.adsManagerFacade = adsManagerFacade;
    }

    public AdsEntity getAdsEntity() {
        return adsEntity;
    }

    public void setAdsEntity(AdsEntity adsEntity) {
        this.adsEntity = adsEntity;
    }

    public Long getAdsId() {
        return adsId;
    }

    public void setAdsId(Long adsId) {
        this.adsId = adsId;
    }
}
