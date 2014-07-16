package backend.facades.site.pages;

import backend.connections.DbInitBean;
import backend.entities.ArticleEntity;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "CategoryIdeasBean")
@ViewScoped
public class CategoryIdeasBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArticleEntity ideasNews;
    private List<ArticleEntity> ideasList;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private Long categoryId;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;  

    public CategoryIdeasBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("categoryId")) {
            categoryId = Util.longValue((ex.getRequestParameterMap().get("categoryId")));
        }
        if (categoryId != null) {
            ideasNews = this.getOneIdeaArticle(categoryId);
            ideasList = this.getIdeasArticleList(categoryId);
        }
    }

    private List<ArticleEntity> getIdeasArticleList(Long categoryId) {      
        List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("categoryId", categoryId);
        query.put("status", StatusTypes.PUBLISHED);
        query.put("top", StatusTypes.ARTICLE_IDEAS);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(1).limit(4);
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

    private ArticleEntity getOneIdeaArticle(Long categoryId) {
        ArticleEntity article = null;
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("categoryId", categoryId);
        query.put("top", StatusTypes.ARTICLE_IDEAS);
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(0).limit(0);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                article = new ArticleEntity();
                article.setId((Long) document.get("id"));
                article.setCategoryId((Long) document.get("categoryId"));
                article.setCategoryName((String) document.get("categoryName"));
                article.setTitle((String) document.get("title"));
                article.setHeader((String) document.get("header"));
                article.setAuthorName((String) document.get("authorName"));
                article.setSlug((String) document.get("slug"));
                article.setDatePosted((Date) document.get("datePosted"));
                article.setImageId((String) document.get("imageId"));
                article.setPermalink((String) document.get("permalink"));
                article.setArticleTopStatus((Integer) document.get("top"));
            }
        } catch (Exception e) {
        } finally {
            //   System.out.println("Cursor  closed");
            cursor.close();
        }
        return article;
    }

    
    public ArticleEntity getIdeasNews() {
        return ideasNews;
    }

    public void setIdeasNews(ArticleEntity ideasNews) {
        this.ideasNews = ideasNews;
    }

    public List<ArticleEntity> getIdeasList() {
        return ideasList;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
    
//    @PreDestroy
//    public void destroy() {
//        System.out.println("Destroy me ");
//        initBean = null;
//    }
//            
    
}
