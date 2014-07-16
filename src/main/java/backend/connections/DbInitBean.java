package backend.connections;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(eager = true, name = "DbInitBean")
@ApplicationScoped
public class DbInitBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{MongoConnectionFactory}")
    private MongoConnectionFactory mongoFactory;
    private DB database = null;
    private DBCollection articleCollection;
    private DBCollection articleAuthorsCollection;
    private DBCollection categoriesCollection;
    private DBCollection adsColl;
    private DBCollection headerColl;
    private DBCollection fileCollection;
    private DBCollection galleryColl;
    private DBCollection generalSettings;
    private DBCollection malerConfiguration;
    private DBCollection userCollection;
    private DBCollection activationCollection;
    private DBCollection forgotCollection;
    private DBCollection videoColl;
    private DBCollection themeLoaderColl;
    private DBCollection pageViewColl;
    private DBCollection staticPagesColl;

    public DbInitBean() {
        Mongo mongo;
        try {
            mongoFactory = new MongoConnectionFactory();
            mongo = mongoFactory.createMongoInstance();
            database = mongo.getDB("armcms");
            try {
                themeLoaderColl = database.getCollection("armcms_themes");
                articleCollection = database.getCollection("armcms_articles");
                categoriesCollection = database.getCollection("armcms_article_categories");
                adsColl = database.getCollection("armcms_ads");
                headerColl = database.getCollection("armcms_headers");
                articleAuthorsCollection = database.getCollection("armcms_articles_authors");
                fileCollection = database.getCollection("armcms_imagetable");
                galleryColl = database.getCollection("armcms_gallery");
                generalSettings = database.getCollection("armcms_general_settings");
                malerConfiguration = database.getCollection("armcms_mail_settings");
                userCollection = database.getCollection("armcms_users");
                activationCollection = database.getCollection("armcms_activation");            
                forgotCollection = database.getCollection("armcms_forgotpass");
                videoColl = database.getCollection("armcms_video");
                pageViewColl = database.getCollection("armcms_pageview");
                staticPagesColl = database.getCollection("armcms_static_pages");

            } catch (Exception e) {
//                articleCollection = database.createCollection("armcms_articles", null);
//                categoriesCollection = database.createCollection("armcms_article_categories", null);
//                headerColl = database.getCollection("armcms_headers");
//                adsColl = database.getCollection("armcms_ads");
//                articleAuthorsCollection = database.createCollection("armcms_articles_authors", null);
//                fileCollection = database.createCollection("armcms_imagetable", null);
//                galleryColl = database.getCollection("armcms_gallery");
//                generalSettings = database.createCollection("armcms_general_settings", null);
//                malerConfiguration = database.createCollection("armcms_mail_settings", null);
//                userCollection = database.createCollection("armcms_users", null);
                //activationCollection = database.createCollection("armcms_activation", null);
//                forgotCollection = database.createCollection("armcms_forgotpass", null);
//                videoColl = database.getCollection("armcms_video");
            }
        } catch (MongoException ex) {
            ex.printStackTrace();
        }
    }

    public DBCollection getStaticPagesColl() {
        return staticPagesColl;
    }   
    

    public DBCollection getPageViewColl() {
        return pageViewColl;
    }
    
    public DBCollection getThemeLoaderColl() {
        return themeLoaderColl;
    }   
    

    public DB getDatabase() {
        return database;
    }

    public DBCollection getArticleCollection() {
        return articleCollection;
    }

    public DBCollection getArticleAuthorsCollection() {
        return articleAuthorsCollection;
    }

    public DBCollection getCategoriesCollection() {
        return categoriesCollection;
    }

    public DBCollection getAdsColl() {
        return adsColl;
    }

    public DBCollection getHeaderColl() {
        return headerColl;
    }

    public DBCollection getFileCollection() {
        return fileCollection;
    }

    public DBCollection getGalleryColl() {
        return galleryColl;
    }

    public DBCollection getGeneralSettings() {
        return generalSettings;
    }

    public DBCollection getMalerConfiguration() {
        return malerConfiguration;
    }

    public DBCollection getUserCollection() {
        return userCollection;
    }

    public DBCollection getActivationCollection() {
        return activationCollection;
    }

    public DBCollection getForgotCollection() {
        return forgotCollection;
    }

    public DBCollection getVideoColl() {
        return videoColl;
    }

    public void setMongoFactory(MongoConnectionFactory mongoFactory) {
        this.mongoFactory = mongoFactory;
    }
}
