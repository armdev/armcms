package frontend.web.beans.headers;

import backend.entities.SiteHeaderEntity;
import backend.facades.AdsManagerFacade;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "HeaderUpdateBean")
@ViewScoped
public class HeaderUpdateBean implements Serializable {

    @ManagedProperty("#{AdsManagerFacade}")
    private AdsManagerFacade adsManagerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private SiteHeaderEntity siteHeaderEntity;
    private Long id;

    public HeaderUpdateBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        siteHeaderEntity = new SiteHeaderEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            id = Util.longValue(ex.getRequestParameterMap().get("next"));
        }
        siteHeaderEntity = adsManagerFacade.getHeaderById(id);
    }

    public String update() {
        siteHeaderEntity.setId(id);
        adsManagerFacade.updateHeader(siteHeaderEntity);
        FacesMessage msg = new FacesMessage(bundle.getString("success"), bundle.getString("success"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "headers";
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
