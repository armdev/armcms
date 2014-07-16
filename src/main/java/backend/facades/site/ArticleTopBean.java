package backend.facades.site;

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
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author armen
 */
@ManagedBean(name = "ArticleTopBean")
@ApplicationScoped
public class ArticleTopBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArticleEntity topNews;
    private List<ArticleEntity> topList;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public ArticleTopBean() {
    }

    @PostConstruct
    public void init() {        
        topNews = this.getTopestArticle();
        topList = this.getTopArticleList();
       // System.out.println("TOP NEWS INIT");
        if(topNews == null){
            topNews = new ArticleEntity();
        }
    }

    public ArticleEntity getTopNews() {
        return topNews;
    }

    public void setTopNews(ArticleEntity topNews) {
        this.topNews = topNews;
    }

    public List<ArticleEntity> getTopList() {
        return topList;
    }

    private List<ArticleEntity> getTopArticleList() {
        //links
        List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.PUBLISHED);
        query.put("top", StatusTypes.ARTICLE_TOP);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(1).limit(4);
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
            article.setSlug((String) document.get("slug"));
            article.setPermalink((String) document.get("permalink"));
            article.setArticleTopStatus((Integer) document.get("top"));
            articles.add(article);
        }
        return articles;
    }

    private ArticleEntity getTopestArticle() {
        ArticleEntity article = null;
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("top", StatusTypes.ARTICLE_TOP);
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(0).limit(0);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                article = new ArticleEntity();
                article.setId((Long) document.get("id"));
                article.setAuthorName((String) document.get("authorName"));
                article.setCategoryId((Long) document.get("categoryId"));
                article.setCategoryName((String) document.get("categoryName"));
                article.setTitle((String) document.get("title"));
                article.setHeader((String) document.get("header"));
                article.setSlug((String) document.get("slug"));
                article.setContent((String) document.get("content"));
                article.setDatePosted((Date) document.get("datePosted"));
                article.setImageId((String) document.get("imageId"));
                article.setPermalink((String) document.get("permalink"));
                article.setArticleTopStatus((Integer) document.get("top"));
            }
        } catch (Exception e) {
        } finally {          
            cursor.close();
        }
        return article;
    }
    
    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }    
    
}
