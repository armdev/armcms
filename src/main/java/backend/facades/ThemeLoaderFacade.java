package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.ThemeEntity;
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

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "ThemeLoaderFacade")
@ApplicationScoped
public class ThemeLoaderFacade extends DbInitBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private ThemeEntity currentTheme;

    public ThemeLoaderFacade() {
        this.initTheme();
    }

    private ThemeEntity initTheme() {        
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.ACTIVE);
        DBObject object = getThemeLoaderColl().findOne(query);
        try {
            if (object != null) {
                currentTheme = new ThemeEntity();
                currentTheme.setId((Long) object.get("id"));
                currentTheme.setName((String) object.get("name"));
                currentTheme.setStatus((Integer) object.get("status"));
            }
        } catch (Exception e) {
        }
        return currentTheme;
    }

    public ThemeEntity getCurrentTheme() {
        return currentTheme;
    }

    public List<ThemeEntity> getThemsList() {
        List<ThemeEntity> list = new ArrayList<ThemeEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = getThemeLoaderColl().find(query).sort(sortCriteria);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            ThemeEntity item = new ThemeEntity();
            item.setId((Long) document.get("id"));
            item.setName((String) document.get("name"));
            item.setStatus((Integer) document.get("status"));
            list.add(item);
        }
        return list;
    }

    public void removeTheme(Long id) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", id);
            if (document != null) {
                getThemeLoaderColl().remove(document);
            }
            initTheme();
        } catch (Exception e) {
        }
    }

    public ThemeEntity getThemeById(Long id) {
        ThemeEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = getThemeLoaderColl().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                item = new ThemeEntity();
                item.setId((Long) document.get("id"));
                item.setName((String) document.get("name"));
                item.setStatus((Integer) document.get("status"));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return item;
    }

    public Long addTheme(ThemeEntity entity) {
        try {
            BasicDBObject document = new BasicDBObject();
            entity.setId(Util.longValue(getNextId(getDatabase(), "themeSeqGen")));
            document.put("id", entity.getId());
            document.put("name", entity.getName().trim());
            document.put("status", StatusTypes.NOT_ACTIVATED);
            document.put("datePosted", new Date(System.currentTimeMillis()));
            getThemeLoaderColl().insert(document, WriteConcern.SAFE);
            initTheme();
        } catch (Exception e) {
        }
        return entity.getId();
    }

    public void updateTheme(ThemeEntity entity) {
        if (entity.getStatus().equals(StatusTypes.ACTIVE)) {
            BasicDBObject query = new BasicDBObject();
            query.put("status", StatusTypes.ACTIVE);
            DBCursor cursor = getThemeLoaderColl().find(query);
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                Long id = (Long) document.get("id");
                BasicDBObject newDoc = new BasicDBObject();
                newDoc.append("$set", new BasicDBObject()
                        .append("status", StatusTypes.NOT_ACTIVATED));
                getThemeLoaderColl().update(new BasicDBObject().append("id", id), newDoc);
            }
        }

        BasicDBObject document = new BasicDBObject();
        document.append("$set", new BasicDBObject()
                .append("name", entity.getName().trim())
                .append("status", entity.getStatus()));

        getThemeLoaderColl().update(new BasicDBObject().append("id", entity.getId()), document);
        initTheme();
    }
//
//    public void setInitBean(DbInitBean initBean) {
//        this.initBean = initBean;
//    }

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
