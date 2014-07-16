package backend.facades;

import backend.connections.DbInitBean;
import backend.connections.MongoConnectionFactory;
import backend.entities.GeneralSettingsEntity;
import backend.entities.VideoEntity;
import backend.facades.site.VideoListBean;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
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
@ManagedBean(name = "VideoManagerFacade")
@ApplicationScoped
public class VideoManagerFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{GeneralSettingsFacade}")
    private GeneralSettingsFacade generalSettingsFacade;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;
    @ManagedProperty("#{VideoListBean}")
    private VideoListBean videoListBean;

    public VideoManagerFacade() {
    }

    public VideoEntity getVideoByLocation(Integer location) {
        VideoEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("location", location);
        query.put("status", StatusTypes.ACTIVE);
        DBObject object = initBean.getVideoColl().findOne(query);//must be one, location and active 
        try {
            if (object != null) {
                item = new VideoEntity();
                item.setId((Long) object.get("id"));
                item.setStatus((Integer) object.get("status"));
                item.setTitle((String) object.get("title"));
                item.setUrl((String) object.get("url"));
                item.setLocation((Integer) object.get("location"));
                item.setDatePosted((Date) object.get("datePosted"));
                item.setPermalink((String) object.get("permalink"));
                item.setSlug((String) object.get("slug"));
            }
        } catch (Exception e) {
        }
        return item;
    }

    public VideoEntity getVideoById(Long id) {
        VideoEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBObject object = initBean.getVideoColl().findOne(query);
        try {
            if (object != null) {               
                item = new VideoEntity();
                item.setId((Long) object.get("id"));
                item.setStatus((Integer) object.get("status"));
                item.setTitle((String) object.get("title"));
                item.setUrl((String) object.get("url"));
                item.setLocation((Integer) object.get("location"));
                item.setDatePosted((Date) object.get("datePosted"));
                item.setPermalink((String) object.get("permalink"));
                item.setSlug((String) object.get("slug"));
            }
        } catch (Exception e) {
        } 
        return item;
    }

    public Integer getVideoCount() {
        Integer listCount = 0;
        try {
            BasicDBObject query = new BasicDBObject();
            DBCursor cursor = initBean.getVideoColl().find(query);
            listCount = cursor.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCount;
    }

    public List<VideoEntity> getVideoList(Integer skip, Integer limit) {
        List<VideoEntity> list = new ArrayList<VideoEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getVideoColl().find(query).sort(sortCriteria).skip(skip).limit(limit);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            VideoEntity item = new VideoEntity();
            item.setId((Long) document.get("id"));
            item.setStatus((Integer) document.get("status"));
            item.setTitle((String) document.get("title"));
            item.setUrl((String) document.get("url"));
            item.setPermalink((String) document.get("permalink"));
            item.setSlug((String) document.get("slug"));
            item.setLocation((Integer) document.get("location"));
            item.setDatePosted((Date) document.get("datePosted"));
            list.add(item);
        }
        return list;
    }

    public void updateVideo(VideoEntity entity) {
        BasicDBObject document = new BasicDBObject();
        try {
            GeneralSettingsEntity settings = generalSettingsFacade.getSettings();
            entity.setHostName(settings.getSiteAddress());
            String permalink = entity.getHostName() + "/video/" + entity.getId() + "/" + this.replaceSpace(entity.getSlug());
            document.append("$set", new BasicDBObject()
                    .append("title", entity.getTitle())
                    .append("url", entity.getUrl())
                    .append("datePosted", new Date(System.currentTimeMillis()))
                    .append("location", entity.getLocation())
                    .append("slug", entity.getSlug())
                    .append("permalink", permalink)
                    .append("hostname", entity.getHostName())
                    .append("status", entity.getStatus()));
            initBean.getVideoColl().update(new BasicDBObject().append("id", entity.getId()), document);
            videoListBean.init();
        } catch (Exception e) {
        }
    }

    public void removeVideo(Long id) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", id);
            if (document != null) {
                initBean.getVideoColl().remove(document);
            }
            videoListBean.init();
        } catch (Exception e) {
        }
    }

    public Long addVideo(VideoEntity entity) {
        try {
            BasicDBObject document = new BasicDBObject();
            entity.setId(Util.longValue(getNextId(initBean.getDatabase(), "videoSeqGen")));
            document.put("id", entity.getId());
            document.put("title", entity.getTitle());
            document.put("status", entity.getStatus());
            document.put("url", entity.getUrl());
            document.put("location", entity.getLocation());
            document.put("slug", this.replaceSpace(entity.getSlug()));
            GeneralSettingsEntity settings = generalSettingsFacade.getSettings();
            entity.setHostName(settings.getSiteAddress());
            String permalink = entity.getHostName() + "/video/" + entity.getId() + "/" + this.replaceSpace(entity.getSlug());
            document.put("permalink", permalink);
            document.put("hostname", entity.getHostName());
            document.put("datePosted", new Date(System.currentTimeMillis()));
            initBean.getVideoColl().insert(document, WriteConcern.SAFE);
            videoListBean.init();
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

    public void setVideoListBean(VideoListBean videoListBean) {
        this.videoListBean = videoListBean;
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
