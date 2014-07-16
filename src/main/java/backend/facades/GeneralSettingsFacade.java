package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.GeneralSettingsEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import frontend.web.utils.Util;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "GeneralSettingsFacade")
@ApplicationScoped
public class GeneralSettingsFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public GeneralSettingsFacade() {
    }

    public void addConfig() {
        GeneralSettingsEntity generalSettingsEntity = new GeneralSettingsEntity();
        try {
            BasicDBObject document = new BasicDBObject();
            generalSettingsEntity.setId(Util.longValue(getNextId(initBean.getDatabase(), "generalConfigSeqGen")));
            document.put("id", generalSettingsEntity.getId());
            document.put("siteTitle", generalSettingsEntity.getSiteTitle());
            document.put("tagLine", generalSettingsEntity.getSiteTitle());
            document.put("siteAddress", generalSettingsEntity.getSiteAddress());
            document.put("ownerEmail", generalSettingsEntity.getOwnerEmail());
            initBean.getGeneralSettings().insert(document, WriteConcern.SAFE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSettings(GeneralSettingsEntity generalSettingsEntity) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", generalSettingsEntity.getId());
            document.put("siteTitle", generalSettingsEntity.getSiteTitle());
            document.put("tagLine", generalSettingsEntity.getTagLine());
            document.put("siteAddress", generalSettingsEntity.getSiteAddress());
            document.put("ownerEmail", generalSettingsEntity.getOwnerEmail());
            initBean.getGeneralSettings().update(new BasicDBObject().append("id", generalSettingsEntity.getId()), document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GeneralSettingsEntity getSettings() {
        GeneralSettingsEntity properties = null;
        DBObject config = initBean.getGeneralSettings().findOne();
        try {
            if (config != null) {
                properties = new GeneralSettingsEntity();
                properties.setId((Long) config.get("id"));
                properties.setSiteTitle((String) config.get("siteTitle"));
                properties.setTagLine((String) config.get("tagLine"));
                properties.setSiteAddress((String) config.get("siteAddress"));
                properties.setOwnerEmail((String) config.get("ownerEmail"));
            } else if (config == null) {
                this.addConfig();
                DBObject configNew = initBean.getGeneralSettings().findOne();
                properties = new GeneralSettingsEntity();
                properties.setId((Long) configNew.get("id"));
                properties.setSiteTitle((String) configNew.get("siteTitle"));
                properties.setTagLine((String) configNew.get("tagLine"));
                properties.setSiteAddress((String) configNew.get("siteAddress"));
                properties.setOwnerEmail((String) configNew.get("ownerEmail"));
            }
        } catch (Exception e) {
        }
        return properties;
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
