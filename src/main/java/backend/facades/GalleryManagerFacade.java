package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.GalleryEntity;
import backend.entities.GeneralSettingsEntity;
import backend.facades.site.GalleryListBean;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author armen
 */
@ManagedBean(name = "GalleryManagerFacade")
@ApplicationScoped
public class GalleryManagerFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{GeneralSettingsFacade}")
    private GeneralSettingsFacade generalSettingsFacade;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean; 
    @ManagedProperty("#{GalleryListBean}")
    private GalleryListBean  galleryListBean;

    public GalleryManagerFacade() {
    }

    public void removeGallery(Long id) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", id);
            if (document != null) {
                initBean.getGalleryColl().remove(document);
            }
        } catch (Exception e) {
        }
    }

    public Integer getGalleryCount() {
        Integer listCount = 0;
        try {
            BasicDBObject query = new BasicDBObject();
            DBCursor cursor = initBean.getGalleryColl().find(query);
            listCount = cursor.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCount;
    }

    public List<GalleryEntity> getGalleryList(Integer skip, Integer limit) {
        List<GalleryEntity> list = new ArrayList<GalleryEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getGalleryColl().find(query).sort(sortCriteria).skip(skip).limit(limit);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            GalleryEntity item = new GalleryEntity();
            item.setId((Long) document.get("id"));
            item.setStatus((Integer) document.get("status"));
            item.setImages((List<String>) document.get("images"));
            item.setTitle((String) document.get("title"));
            item.setPermalink((String) document.get("permalink"));
            item.setSlug((String) document.get("slug"));
            item.setDatePosted((Date) document.get("datePosted"));
            list.add(item);
        }
        return list;
    }

    public GalleryEntity getGalleryById(Long id) {
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

    public boolean updateGallery(GalleryEntity entity) {
        try {
            GeneralSettingsEntity settings = generalSettingsFacade.getSettings();
            entity.setHostName(settings.getSiteAddress());
            String permalink = entity.getHostName() + "/gallery/" + entity.getId() + "/" + this.replaceSpace(entity.getSlug());
            BasicDBObject document = new BasicDBObject();
            document.append("$set", new BasicDBObject()
                    .append("images", entity.getImages())
                    .append("permalink", permalink)
                    .append("slug", entity.getSlug())
                    .append("hostname", entity.getHostName())
                    .append("title", entity.getTitle())
                    .append("datePosted", new Date(System.currentTimeMillis()))
                    .append("status", entity.getStatus()));
            initBean.getGalleryColl().update(new BasicDBObject().append("id", entity.getId()), document);
            galleryListBean.init();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Long createNewGallery(GalleryEntity entity) {
        try {
            BasicDBObject document = new BasicDBObject();
            entity.setId(Util.longValue(getNextId(initBean.getDatabase(), "gallerySeqGen")));
            document.put("id", entity.getId());
            document.put("title", entity.getTitle());
            document.put("status", entity.getStatus());
            document.put("slug", this.replaceSpace(entity.getSlug()));
            document.put("datePosted", new Date(System.currentTimeMillis()));
            GeneralSettingsEntity settings = generalSettingsFacade.getSettings();
            entity.setHostName(settings.getSiteAddress());
            String permalink = entity.getHostName() + "/gallery/" + entity.getId() + "/" + this.replaceSpace(entity.getSlug());
            document.put("permalink", entity.getPermalink());
            document.put("hostname", entity.getHostName());
            document.put("images", entity.getImages());
            initBean.getGalleryColl().insert(document, WriteConcern.SAFE);
            galleryListBean.init();
        } catch (Exception e) {
        }
        return entity.getId();
    }

    private String replaceSpace(String s) {
        if (s.length() < 2) {
            if (s.equals(" ")) {
                return "-";
            } else {
                return s;
            }
        }
        if (s.charAt(0) == ' ') {
            return "-" + replaceSpace(s.substring(1).toLowerCase());
        } else {
            return s.substring(0, 1) + replaceSpace(s.substring(1).toLowerCase());
        }
    }

    public void setGeneralSettingsFacade(GeneralSettingsFacade generalSettingsFacade) {
        this.generalSettingsFacade = generalSettingsFacade;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }

    public void setGalleryListBean(GalleryListBean galleryListBean) {
        this.galleryListBean = galleryListBean;
    }   
    

    protected static String getNextId(DB db, String seq_name) {
        String sequence_collection = "seq"; // the name of the sequence collection
        String sequence_field = "seq"; // the name of the field which holds the sequence
        DBCollection seq = db.getCollection(sequence_collection); // get the collection (this will create it if needed)               
        if (seq == null) {
            seq = db.createCollection(sequence_collection, null);
        }
        // this object represents your "query", its analogous to a WHERE clause in SQL
        DBObject query = new BasicDBObject();
        query.put("id", seq_name); // where id = the input sequence name
        // this object represents the "update" or the SET blah=blah in SQL
        DBObject change = new BasicDBObject(sequence_field, 1);
        DBObject update = new BasicDBObject("$inc", change); // the $inc here is a mongodb command for increment
        // Atomically updates the sequence field and returns the value for you
        DBObject res = seq.findAndModify(query, new BasicDBObject(), new BasicDBObject(), false, update, true, true);
        return res.get(sequence_field).toString();
    }
}
