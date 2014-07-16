package backend.facades.site.pages;

import backend.connections.DbInitBean;
import backend.entities.VideoEntity;
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
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "showVideoBean")
@ViewScoped
public class ShowVideoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private Long id;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;
    private VideoEntity entity;

    public ShowVideoBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        entity = new VideoEntity();
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("videoId")) {
            id = Util.longValue((ex.getRequestParameterMap().get("videoId")));
        }  
        if (id != null) {
            entity = this.getVideoById(id);
        } else {
            redirect(context, "../site/index.jsf");
        }
    }

    private VideoEntity getVideoById(Long id) {
        VideoEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = initBean.getVideoColl().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                item = new VideoEntity();
                item.setId((Long) document.get("id"));
                item.setStatus((Integer) document.get("status"));
                item.setTitle((String) document.get("title"));
                item.setUrl((String) document.get("url"));
                item.setLocation((Integer) document.get("location"));
                item.setDatePosted((Date) document.get("datePosted"));
                item.setPermalink((String) document.get("permalink"));
                item.setSlug((String) document.get("slug"));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VideoEntity getEntity() {
        return entity;
    }

    public void setEntity(VideoEntity entity) {
        this.entity = entity;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }

    private static void redirect(FacesContext facesContext, String url) {
        try {
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }
}
