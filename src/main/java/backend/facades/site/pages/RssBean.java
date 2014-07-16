package backend.facades.site.pages;

import backend.connections.DbInitBean;
import backend.entities.ArticleEntity;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author armen
 */
@ManagedBean(name = "RssBean")
@RequestScoped
public class RssBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<ArticleEntity> rssList;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public RssBean() {
    }

    @PostConstruct
    public void init() {
    }

    public List<ArticleEntity> getRssList() {
        List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(0).limit(100);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            ArticleEntity article = new ArticleEntity();
            article.setId((Long) document.get("id"));
            article.setAuthorName((String) document.get("authorName"));
            article.setCategoryId((Long) document.get("categoryId"));
            article.setCategoryName((String) document.get("categoryName"));
            article.setTitle((String) document.get("title"));
            article.setContent((String) document.get("content"));
            article.setHeader((String) document.get("header"));
            article.setImageId((String) document.get("imageId"));
            article.setSlug((String) document.get("slug"));
            article.setDatePosted((Date) document.get("datePosted"));
            article.setPermalink((String) document.get("permalink"));
            article.setArticleTopStatus((Integer) document.get("top"));
            articles.add(article);
        }
        return articles;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
}
