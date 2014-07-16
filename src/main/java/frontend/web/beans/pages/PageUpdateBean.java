package frontend.web.beans.pages;

import backend.entities.StaticPagesEntity;
import backend.facades.StaticPagesFacade;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "PageUpdateBean")
@ViewScoped
public class PageUpdateBean implements Serializable {

    @ManagedProperty("#{StaticPagesFacade}")
    private StaticPagesFacade staticPagesFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private StaticPagesEntity entity;
    private Long id;

    public PageUpdateBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        entity = new StaticPagesEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            id = Util.longValue(ex.getRequestParameterMap().get("next"));
        }
        if (id != null) {
            entity = staticPagesFacade.getPageById(id);
        }
    }

    public String update() {
        entity.setId(id);
        staticPagesFacade.updatePage(entity);
        FacesMessage msg = new FacesMessage(bundle.getString("success"), bundle.getString("success"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "pages";
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
