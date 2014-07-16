package backend.facades.site.pages;

import backend.connections.DbInitBean;
import backend.entities.GalleryEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import frontend.web.utils.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@ManagedBean(name = "ShowGalleryBean")
@ViewScoped
public class ShowGalleryBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private Long id;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;
    private GalleryEntity entity;

    public ShowGalleryBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        entity = new GalleryEntity();
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("id")) {
            id = Util.longValue((ex.getRequestParameterMap().get("id")));
        }
        if (id != null) {
            entity = this.getGalleryById(id);
        } else {
            redirect(context, "../site/index.jsf");
        }
    }

    private GalleryEntity getGalleryById(Long id) {
        GalleryEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = initBean.getGalleryColl().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                item = new GalleryEntity();
                item.setId((Long) document.get("id"));
                item.setStatus((Integer) document.get("status"));
                item.setTitle((String) document.get("title"));
                item.setPermalink((String) document.get("permalink"));
                item.setSlug((String) document.get("slug"));
                item.setDatePosted((Date) document.get("datePosted"));
                item.setImages((List<String>) document.get("images"));
                if (item.getImages() == null) {
                    List<String> emptyList = new ArrayList<String>();
                    item.setImages(emptyList);
                }
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

    public GalleryEntity getEntity() {
        return entity;
    }

    public void setEntity(GalleryEntity entity) {
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
