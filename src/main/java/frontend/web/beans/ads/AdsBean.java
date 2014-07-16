package frontend.web.beans.ads;

import backend.entities.AdsEntity;
import backend.facades.AdsManagerFacade;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "AdsBean")
@ViewScoped
public class AdsBean implements Serializable {

    @ManagedProperty("#{AdsManagerFacade}")
    private AdsManagerFacade adsManagerFacade;
    
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private AdsEntity adsEntity;
    private String url = null;

    public AdsBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        adsEntity = new AdsEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    public String show(String videoUrl) {
        url = videoUrl;
        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<AdsEntity> getAdsList() {
        return adsManagerFacade.getAdsList();
    }

    public String addAds() {
        adsManagerFacade.addNewAds(adsEntity);
        return "adlist";
    }

    public String remove(Long id) {
        adsManagerFacade.removeAds(id);
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
}
