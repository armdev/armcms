package frontend.web.beans.headers;

import backend.entities.SiteHeaderEntity;
import backend.facades.AdsManagerFacade;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "HeaderBean")
@ViewScoped
public class HeaderBean implements Serializable {

    @ManagedProperty("#{AdsManagerFacade}")
    private AdsManagerFacade adsManagerFacade;
    
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private SiteHeaderEntity siteHeaderEntity;
    private String url = null;

    public HeaderBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        siteHeaderEntity = new SiteHeaderEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }
   

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<SiteHeaderEntity> getHeadersList() {
        return adsManagerFacade.getHeadersList();
    }

    public String addHeader() {
        adsManagerFacade.addHeader(siteHeaderEntity);
        return "headers";
    }

    public String remove(Long id) {
        adsManagerFacade.removeHeader(id);
        return null;
    }

    public void setAdsManagerFacade(AdsManagerFacade adsManagerFacade) {
        this.adsManagerFacade = adsManagerFacade;
    }

    public SiteHeaderEntity getSiteHeaderEntity() {
        return siteHeaderEntity;
    }

    public void setSiteHeaderEntity(SiteHeaderEntity siteHeaderEntity) {
        this.siteHeaderEntity = siteHeaderEntity;
    }

  
}
