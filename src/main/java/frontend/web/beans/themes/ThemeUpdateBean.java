package frontend.web.beans.themes;

import backend.entities.ThemeEntity;
import backend.facades.ThemeLoaderFacade;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "ThemeUpdateBean")
@ViewScoped
public class ThemeUpdateBean implements Serializable {

    @ManagedProperty("#{ThemeLoaderFacade}")
    private ThemeLoaderFacade themeLoaderFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private ThemeEntity entity;
    private Long id;

    public ThemeUpdateBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        entity = new ThemeEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            id = Util.longValue(ex.getRequestParameterMap().get("next"));
        }
        entity = themeLoaderFacade.getThemeById(id);
    }

    public String update() {
        entity.setId(id);
        themeLoaderFacade.updateTheme(entity);
        FacesMessage msg = new FacesMessage(bundle.getString("success"), bundle.getString("success"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "themes";
    } 

    public void setThemeLoaderFacade(ThemeLoaderFacade themeLoaderFacade) {
        this.themeLoaderFacade = themeLoaderFacade;
    }

    public ThemeEntity getEntity() {
        return entity;
    }

    public void setEntity(ThemeEntity entity) {
        this.entity = entity;
    }    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
