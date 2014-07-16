package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.GeneralSettingsEntity;
import backend.entities.StaticPagesEntity;
import backend.facades.site.StaticPagesLoaderBean;
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
@ManagedBean(name = "StaticPagesFacade")
@ApplicationScoped
public class StaticPagesFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;
    @ManagedProperty("#{GeneralSettingsFacade}")
    private GeneralSettingsFacade generalSettingsFacade;
    @ManagedProperty("#{StaticPagesLoaderBean}")
    private StaticPagesLoaderBean staticPagesLoaderBean;

    public StaticPagesFacade() {
    }

    public StaticPagesEntity getPageById(Long id) {
        StaticPagesEntity item = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
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

    public void removePage(Long id) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", id);
            initBean.getStaticPagesColl().remove(document);
            staticPagesLoaderBean.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<StaticPagesEntity> getPagesList() {
        List<StaticPagesEntity> items = new ArrayList<StaticPagesEntity>();
        String sort = "sortOrder";
        String order = "asc";
        DBObject sortCriteria = new BasicDBObject(sort, "asc".equals(order) ? 1 : -1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getStaticPagesColl().find(query).sort(sortCriteria);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                StaticPagesEntity item = new StaticPagesEntity();
                item.setId((Long) document.get("id"));
                item.setTitle((String) document.get("title"));
                item.setContent((String) document.get("content"));
                item.setPermalink((String) document.get("permalink"));
                item.setStatus((Integer) document.get("status"));
                item.setDatePosted((Date) document.get("datePosted"));
                items.add(item);
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public void addPage(StaticPagesEntity entity) {
        try {
            BasicDBObject document = new BasicDBObject();
            entity.setId(Util.longValue(getNextId(initBean.getDatabase(), "staticPagesSeqGen")));
            document.put("id", entity.getId());
            document.put("title", entity.getTitle());
            document.put("content", entity.getContent());
            document.put("sortOrder", entity.getSortOrder());
            document.put("status", entity.getStatus());
            document.put("datePosted", new Date(System.currentTimeMillis()));
            GeneralSettingsEntity generalSettingsEntity = generalSettingsFacade.getSettings();
            String permalink = generalSettingsEntity.getSiteAddress() + "/pages/" + entity.getId() + "/" + entity.getTitle();
            document.put("permalink", permalink);
            initBean.getStaticPagesColl().insert(document, WriteConcern.SAFE);
            staticPagesLoaderBean.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePage(StaticPagesEntity entity) {
        GeneralSettingsEntity generalSettingsEntity = generalSettingsFacade.getSettings();
        String permalink = generalSettingsEntity.getSiteAddress() + "/pages/" + entity.getId() + "/" + entity.getTitle();
        try {
            BasicDBObject document = new BasicDBObject();
            document.append("$set", new BasicDBObject()
                    .append("title", entity.getTitle())
                    .append("content", entity.getContent())
                    .append("sortOrder", entity.getSortOrder())
                    .append("status", entity.getStatus())
                    .append("datePosted", new Date(System.currentTimeMillis()))
                    .append("permalink", permalink));
            initBean.getStaticPagesColl().update(new BasicDBObject().append("id", entity.getId()), document);
            staticPagesLoaderBean.init();
        } catch (Exception e) {
        }
    }

    public void setStaticPagesLoaderBean(StaticPagesLoaderBean staticPagesLoaderBean) {
        this.staticPagesLoaderBean = staticPagesLoaderBean;
    }   
    

    public void setGeneralSettingsFacade(GeneralSettingsFacade generalSettingsFacade) {
        this.generalSettingsFacade = generalSettingsFacade;
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
