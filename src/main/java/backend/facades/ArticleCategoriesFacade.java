package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.ArticleCategoriesEntity;
import backend.facades.site.ArticleMenuFacade;
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
 * @author armen
 */
@ManagedBean(name = "ArticleCategoriesFacade")
@ApplicationScoped
public class ArticleCategoriesFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{ArticleMenuFacade}")
    private ArticleMenuFacade articleMenuFacade;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean; 

    public ArticleCategoriesFacade() {
    }

    public ArticleCategoriesEntity getCategoryById(Long id) {
        ArticleCategoriesEntity book = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = initBean.getCategoriesCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                book = new ArticleCategoriesEntity();
                book.setId((Long) document.get("id"));
                book.setName((String) document.get("name"));
                book.setStatus((Integer) document.get("status"));
                book.setLanguageCode((String) document.get("languageCode"));
                book.setSortOrder((Integer) document.get("sortOrder"));
                book.setDatePosted((Date) document.get("datePosted"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return book;
    }

    public List<ArticleCategoriesEntity> getCategoriesList() {
        List<ArticleCategoriesEntity> list = new ArrayList<ArticleCategoriesEntity>();
        ArticleCategoriesEntity book = null;
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getCategoriesCollection().find(query).sort(sortCriteria);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                book = new ArticleCategoriesEntity();
                book.setId((Long) document.get("id"));
                book.setName((String) document.get("name"));
                book.setStatus((Integer) document.get("status"));
                book.setLanguageCode((String) document.get("languageCode"));
                book.setDatePosted((Date) document.get("datePosted"));
                list.add(book);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public void removeCategory(Long bookId) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", bookId);
            initBean.getCategoriesCollection().remove(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewCategory(ArticleCategoriesEntity booksEntity) {
        Date curDate = new Date(System.currentTimeMillis());
        try {
            BasicDBObject document = new BasicDBObject();
            booksEntity.setId(Util.longValue(getNextId(initBean.getDatabase(), "articleCategorySeqGen")));
            document.put("id", booksEntity.getId());
            document.put("name", booksEntity.getName());
            document.put("status", booksEntity.getStatus());
            document.put("sortOrder", booksEntity.getSortOrder());
            document.put("languageCode", booksEntity.getLanguageCode());
            document.put("datePosted", new Date(System.currentTimeMillis()));
            initBean.getCategoriesCollection().insert(document, WriteConcern.SAFE);
            
          //  if (booksEntity.getStatus().equals(StatusTypes.PUBLISHED)) {
                articleMenuFacade.init();
           // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCategory(ArticleCategoriesEntity booksEntity) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.append("$set", new BasicDBObject()
                    .append("name", booksEntity.getName())
                    .append("status", booksEntity.getStatus())
                    .append("sortOrder", booksEntity.getSortOrder())
                    .append("languageCode", booksEntity.getLanguageCode())
                    .append("datePosted", booksEntity.getDatePosted()));
            initBean.getCategoriesCollection().update(new BasicDBObject().append("id", booksEntity.getId()), document);

            //if (booksEntity.getStatus().equals(StatusTypes.PUBLISHED)) {
                articleMenuFacade.init();
           // }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }

    public void setArticleMenuFacade(ArticleMenuFacade articleMenuFacade) {
        this.articleMenuFacade = articleMenuFacade;
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
