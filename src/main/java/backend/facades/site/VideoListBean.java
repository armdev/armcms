package backend.facades.site;

import backend.connections.DbInitBean;
import backend.entities.VideoEntity;
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
@ManagedBean(name = "VideoListBean")
@ApplicationScoped
public class VideoListBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<VideoEntity> videoList;
    private List<VideoEntity> latestVideoList;
    private VideoEntity topVideo;
    private VideoEntity latestVideo;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public VideoListBean() {
    }

    @PostConstruct
    public void init() {
        videoList = this.getVideoListLinks();
        topVideo = this.getTopestVideo();
        latestVideo = this.getLatestVideoLink();
        latestVideoList = this.getLatestVideoListLinks();

    }

    private List<VideoEntity> getLatestVideoListLinks() {
        List<VideoEntity> items = new ArrayList<VideoEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.ACTIVE);
        query.put("location", StatusTypes.TOP_VIDEO);
        DBCursor cursor = initBean.getVideoColl().find(query).sort(sortCriteria).skip(1).limit(12);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            VideoEntity item = new VideoEntity();
            item.setId((Long) document.get("id"));
            item.setTitle((String) document.get("title"));
            item.setUrl((String) document.get("url"));
            item.setSlug((String) document.get("slug"));
            item.setDatePosted((Date) document.get("datePosted"));
            item.setPermalink((String) document.get("permalink"));
            items.add(item);
        }
        return items;
    }

    private VideoEntity getLatestVideoLink() {
        VideoEntity item = null;
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("location", StatusTypes.LATEST_VIDEO);
        query.put("status", StatusTypes.ACTIVE);
        DBCursor cursor = initBean.getVideoColl().find(query).sort(sortCriteria).skip(0).limit(0);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                item = new VideoEntity();
                item.setId((Long) document.get("id"));
                item.setTitle((String) document.get("title"));
                item.setUrl((String) document.get("url"));
                item.setSlug((String) document.get("slug"));
                item.setDatePosted((Date) document.get("datePosted"));
                item.setPermalink((String) document.get("permalink"));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return item;
    }

    private VideoEntity getTopestVideo() {
        VideoEntity item = null;
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("location", StatusTypes.TOP_VIDEO);
        query.put("status", StatusTypes.ACTIVE);
        DBCursor cursor = initBean.getVideoColl().find(query).sort(sortCriteria).skip(0).limit(0);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                item = new VideoEntity();
                item.setId((Long) document.get("id"));
                item.setTitle((String) document.get("title"));
                item.setUrl((String) document.get("url"));
                item.setSlug((String) document.get("slug"));
                item.setDatePosted((Date) document.get("datePosted"));
                item.setPermalink((String) document.get("permalink"));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return item;
    }

    private List<VideoEntity> getVideoListLinks() {
        List<VideoEntity> items = new ArrayList<VideoEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.ACTIVE);
        query.put("location", StatusTypes.TOP_VIDEO);
        DBCursor cursor = initBean.getVideoColl().find(query).sort(sortCriteria).skip(1).limit(30);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            VideoEntity item = new VideoEntity();
            item.setId((Long) document.get("id"));
            item.setTitle((String) document.get("title"));
            item.setUrl((String) document.get("url"));
            item.setSlug((String) document.get("slug"));
            item.setDatePosted((Date) document.get("datePosted"));
            item.setPermalink((String) document.get("permalink"));
            items.add(item);
        }
        return items;
    }

    public List<VideoEntity> getLatestVideoList() {
        return latestVideoList;
    }

    public VideoEntity getLatestVideo() {
        return latestVideo;
    }

    
    public List<VideoEntity> getVideoList() {
        return videoList;
    }

    public VideoEntity getTopVideo() {
        return topVideo;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
}
