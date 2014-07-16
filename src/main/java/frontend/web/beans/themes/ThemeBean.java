package frontend.web.beans.themes;

import backend.entities.ThemeEntity;
import backend.facades.ThemeLoaderFacade;
import backend.types.StatusTypes;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "ThemeBean")
@ViewScoped
public class ThemeBean implements Serializable {

    @ManagedProperty("#{ThemeLoaderFacade}")
    private ThemeLoaderFacade themeLoaderFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private ThemeEntity entity;
    private String url = null;

    public ThemeBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        entity = new ThemeEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ThemeEntity> getThemsList() {
        return themeLoaderFacade.getThemsList();
    }

    public String addTheme() {
        themeLoaderFacade.addTheme(entity);
        return "themes";
    }

    public String remove(Long id) {
        themeLoaderFacade.removeTheme(id);
        return null;
    }
    
    public String activate(Long id) {
        ThemeEntity update = themeLoaderFacade.getThemeById(id);
        update.setId(id);
        update.setStatus(StatusTypes.ACTIVE);
        themeLoaderFacade.updateTheme(update);
        return null;
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
}
