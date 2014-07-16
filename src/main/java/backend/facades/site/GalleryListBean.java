package backend.facades.site;

import backend.connections.DbInitBean;
import backend.entities.GalleryEntity;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "GalleryListBean")
@ApplicationScoped
public class GalleryListBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<GalleryEntity> latestGalleryList;
    private GalleryEntity topGallery;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public GalleryListBean() {
    }

    @PostConstruct
    public void init() {
        latestGalleryList = this.getGalleryListLinks();
        topGallery = this.getTopGalleryLink();

    }

    private GalleryEntity getTopGalleryLink() {
        GalleryEntity item = null;
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.ACTIVE);
        DBCursor cursor = initBean.getGalleryColl().find(query).sort(sortCriteria).skip(0).limit(0);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                item = new GalleryEntity();
                item.setId((Long) document.get("id"));
                item.setTitle((String) document.get("title"));
                item.setSlug((String) document.get("slug"));
                item.setDatePosted((Date) document.get("datePosted"));
                item.setPermalink((String) document.get("permalink"));
                item.setImages((List<String>) document.get("images"));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return item;
    }

    private List<GalleryEntity> getGalleryListLinks() {
        List<GalleryEntity> items = new ArrayList<GalleryEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.ACTIVE);
        DBCursor cursor = initBean.getGalleryColl().find(query).sort(sortCriteria).skip(1).limit(24);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            GalleryEntity item = new GalleryEntity();
            item.setId((Long) document.get("id"));
            item.setTitle((String) document.get("title"));
            item.setImages((List<String>) document.get("images"));
            item.setSlug((String) document.get("slug"));
            item.setDatePosted((Date) document.get("datePosted"));
            item.setPermalink((String) document.get("permalink"));
            items.add(item);
        }
        return items;
    }

    public List<GalleryEntity> getLatestGalleryList() {
        return latestGalleryList;
    }

    public GalleryEntity getTopGallery() {
        return topGallery;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
}
