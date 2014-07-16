package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.AdsEntity;
import backend.entities.SiteHeaderEntity;
import backend.types.StatusTypes;
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
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "AdsManagerFacade")
@ApplicationScoped
public class AdsManagerFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public AdsManagerFacade() {
    }

    public List<SiteHeaderEntity> getHeadersList() {
        List<SiteHeaderEntity> list = new ArrayList<SiteHeaderEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getHeaderColl().find(query).sort(sortCriteria);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            SiteHeaderEntity feed = new SiteHeaderEntity();
            feed.setId((Long) document.get("id"));
            feed.setTitle((String) document.get("title"));
            feed.setUrl((String) document.get("url"));
            feed.setDatePosted((Date) document.get("datePosted"));
            list.add(feed);
        }
        return list;
    }

    public void removeHeader(Long id) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", id);
            if (document != null) {
                initBean.getHeaderColl().remove(document);
            }
        } catch (Exception e) {
        }
    }

    public SiteHeaderEntity getHeaderById(Long id) {
        SiteHeaderEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = initBean.getHeaderColl().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                item = new SiteHeaderEntity();
                item.setId((Long) document.get("id"));
                item.setTitle((String) document.get("title"));
                item.setUrl((String) document.get("url"));
                item.setDatePosted((Date) document.get("datePosted"));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return item;
    }

    public Long addHeader(SiteHeaderEntity entity) {
        try {
            BasicDBObject document = new BasicDBObject();
            entity.setId(Util.longValue(getNextId(initBean.getDatabase(), "headerSeqGen")));
            document.put("id", entity.getId());
            document.put("title", entity.getTitle());
            document.put("url", entity.getUrl());
            document.put("datePosted", new Date(System.currentTimeMillis()));
            initBean.getHeaderColl().insert(document, WriteConcern.SAFE);
        } catch (Exception e) {
        }
        return entity.getId();
    }

    public void updateHeader(SiteHeaderEntity entity) {
        BasicDBObject document = new BasicDBObject();
        document.append("$set", new BasicDBObject()
                .append("title", entity.getTitle())
                .append("url", entity.getUrl())
                .append("datePosted", new Date(System.currentTimeMillis())));
        initBean.getHeaderColl().update(new BasicDBObject().append("id", entity.getId()), document);
    }

    public AdsEntity getRightAds() {
        AdsEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("location", StatusTypes.ADS_RIGHT);
        query.put("status", StatusTypes.ACTIVE);
        DBObject object = initBean.getAdsColl().findOne(query);//must be one, location and active 
        try {
            if (object != null) {
                item = new AdsEntity();
                item.setId((Long) object.get("id"));
                item.setStatus((Integer) object.get("status"));
                item.setTitle((String) object.get("title"));
                item.setUrl((String) object.get("url"));
                item.setLocation((Integer) object.get("location"));
                item.setDatePosted((Date) object.get("datePosted"));
            }
        } catch (Exception e) {
        }
        return item;
    }

    public AdsEntity getLeftAds() {
        AdsEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("location", StatusTypes.ADS_LEFT);
        query.put("status", StatusTypes.ACTIVE);
        DBObject object = initBean.getAdsColl().findOne(query);//must be one, location and active 
        try {
            if (object != null) {
                item = new AdsEntity();
                item.setId((Long) object.get("id"));
                item.setStatus((Integer) object.get("status"));
                item.setTitle((String) object.get("title"));
                item.setUrl((String) object.get("url"));
                item.setLocation((Integer) object.get("location"));
                item.setDatePosted((Date) object.get("datePosted"));
            }
        } catch (Exception e) {
        }
        return item;
    }

    public AdsEntity getAdsByLocation(Integer location) {
        AdsEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("location", location);
        query.put("status", StatusTypes.ACTIVE);
        DBObject object = initBean.getAdsColl().findOne(query);//must be one, location and active 
        try {
            if (object != null) {
                item = new AdsEntity();
                item.setId((Long) object.get("id"));
                item.setStatus((Integer) object.get("status"));
                item.setTitle((String) object.get("title"));
                item.setUrl((String) object.get("url"));
                item.setLocation((Integer) object.get("location"));
                item.setDatePosted((Date) object.get("datePosted"));
            }
        } catch (Exception e) {
        }
        return item;
    }

    public AdsEntity getAdsById(Long id) {
        AdsEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = initBean.getAdsColl().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                item = new AdsEntity();
                item.setId((Long) document.get("id"));
                item.setStatus((Integer) document.get("status"));
                item.setTitle((String) document.get("title"));
                item.setUrl((String) document.get("url"));
                item.setLocation((Integer) document.get("location"));
                item.setDatePosted((Date) document.get("datePosted"));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return item;
    }

    public List<AdsEntity> getAdsList() {
        List<AdsEntity> list = new ArrayList<AdsEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getAdsColl().find(query).sort(sortCriteria);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            AdsEntity feed = new AdsEntity();
            feed.setId((Long) document.get("id"));
            feed.setStatus((Integer) document.get("status"));
            feed.setTitle((String) document.get("title"));
            feed.setUrl((String) document.get("url"));
            feed.setLocation((Integer) document.get("location"));
            feed.setDatePosted((Date) document.get("datePosted"));
            list.add(feed);
        }
        return list;
    }

    public void updateAds(AdsEntity entity) {
        BasicDBObject document = new BasicDBObject();
        document.append("$set", new BasicDBObject()
                .append("title", entity.getTitle())
                .append("url", entity.getUrl())
                .append("datePosted", new Date(System.currentTimeMillis()))
                .append("location", entity.getLocation())
                .append("status", entity.getStatus()));
        initBean.getAdsColl().update(new BasicDBObject().append("id", entity.getId()), document);
    }

    public void removeAds(Long id) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", id);
            if (document != null) {
                initBean.getAdsColl().remove(document);
            }
        } catch (Exception e) {
        }
    }

    public Long addNewAds(AdsEntity entity) {
        try {
            BasicDBObject document = new BasicDBObject();
            entity.setId(Util.longValue(getNextId(initBean.getDatabase(), "adsSeqGen")));
            document.put("id", entity.getId());
            document.put("title", entity.getTitle());
            document.put("status", entity.getStatus());
            document.put("url", entity.getUrl());
            document.put("location", entity.getLocation());
            document.put("datePosted", new Date(System.currentTimeMillis()));
            initBean.getAdsColl().insert(document, WriteConcern.SAFE);
        } catch (Exception e) {
        }
        return entity.getId();
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
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
