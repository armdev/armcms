package backend.facades.site;

import backend.connections.DbInitBean;
import backend.entities.ArticleEntity;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "SearchBean")
@ViewScoped
public class SearchBean implements Serializable {

    private List<ArticleEntity> resultList;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;
    private String searchKey;
 

    public SearchBean() {
    }

    @PostConstruct
    public void init() {
    }

    public void search() {
        resultList = this.getTopLinks(searchKey);
    }

    private List<ArticleEntity> getTopLinks(String searchKey) {
        Pattern search = Pattern.compile(searchKey, Pattern.CASE_INSENSITIVE);
        List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
        String sort = "datePosted";
        String order = "desc";      
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
          // query.put("_id", new BasicDBObject("$ne", id));//not
        BasicDBObject query1 = new BasicDBObject("content", search);
        BasicDBObject query2 = new BasicDBObject("header", search);
        BasicDBObject query3 = new BasicDBObject("title", search);
        ArrayList<BasicDBObject> myList = new ArrayList<BasicDBObject>();
        myList.add(query1);
        myList.add(query2);
        myList.add(query3);
        BasicDBObject query = new BasicDBObject("$or", myList);
//        BasicDBObject query = new BasicDBObject();
       // query.put("content", java.util.regex.Pattern.compile(searchKey));
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(0).limit(1000);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            ArticleEntity article = new ArticleEntity();
            article.setId((Long) document.get("id"));
            article.setCategoryId((Long) document.get("categoryId"));
            article.setCategoryName((String) document.get("categoryName"));
            article.setTitle((String) document.get("title"));
            article.setHeader((String) document.get("header"));
            article.setAuthorName((String) document.get("authorName"));
            article.setSlug((String) document.get("slug"));
            article.setPermalink((String) document.get("permalink"));
            article.setArticleTopStatus((Integer) document.get("top"));
            articles.add(article);
        }
        return articles;
    }

    
    
    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<ArticleEntity> getResultList() {
        return resultList;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
}
