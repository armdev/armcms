package backend.facades.site.pages;

import backend.connections.DbInitBean;
import backend.entities.StaticPagesEntity;
import backend.facades.StaticPagesFacade;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import frontend.web.utils.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author armen
 */
@ManagedBean(name = "StaticPagesViewBean")
@ViewScoped
public final class StaticPagesViewBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private StaticPagesEntity entity;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private Long id;

    public StaticPagesViewBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            id = Util.longValue((ex.getRequestParameterMap().get("next")));
        }
        if (id != null) {
            entity = this.getShowPageById(id);
        } else {
            redirect(context, "../site/index.jsf");
        }
    }

    private StaticPagesEntity getShowPageById(Long id) {
        StaticPagesEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        query.put("status", StatusTypes.ACTIVE);
        DBCursor cursor = initBean.getStaticPagesColl().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                item = new StaticPagesEntity();
                item.setId((Long) document.get("id"));
                item.setTitle((String) document.get("title"));
                item.setContent((String) document.get("content"));
                item.setPermalink((String) document.get("permalink"));
                item.setStatus((Integer) document.get("status"));
                item.setDatePosted((Date) document.get("datePosted"));
                item.setSortOrder((Integer) document.get("sortOrder"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return item;
    }

    private static void redirect(FacesContext facesContext, String url) {
        try {
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }

    public StaticPagesEntity getEntity() {
        return entity;
    }

    public void setEntity(StaticPagesEntity entity) {
        this.entity = entity;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
}
