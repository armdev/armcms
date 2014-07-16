package frontend.web.beans.pages;

import backend.entities.StaticPagesEntity;
import backend.facades.StaticPagesFacade;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "PageBean")
@ViewScoped
public class PageBean implements Serializable {

    @ManagedProperty("#{StaticPagesFacade}")
    private StaticPagesFacade staticPagesFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private StaticPagesEntity entity;

    public PageBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        entity = new StaticPagesEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    public List<StaticPagesEntity> getPagesList() {
        return staticPagesFacade.getPagesList();
    }

    public String addPage() {
        staticPagesFacade.addPage(entity);
        return "pages";
    }

    public String remove(Long id) {
        staticPagesFacade.removePage(id);
        return null;
    }

    public void setStaticPagesFacade(StaticPagesFacade staticPagesFacade) {
        this.staticPagesFacade = staticPagesFacade;
    }

    public StaticPagesEntity getEntity() {
        return entity;
    }

    public void setEntity(StaticPagesEntity entity) {
        this.entity = entity;
    }
}
