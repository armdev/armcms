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
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "LeftNavigatorBean")
@ViewScoped
public class LeftNavigatorBean implements Serializable {

    private List<ArticleEntity> showTopLinks;
    private List<ArticleEntity> showLatestLinks;
    private List<ArticleEntity> showRegularLinks;
    private List<ArticleEntity> showRandomNews;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public LeftNavigatorBean() {
    }

    @PostConstruct
    public void init() {
        showTopLinks = this.getTopLinks();
        showLatestLinks = this.getLastLinks();
        showRegularLinks = this.getRegularLinks();
        showRandomNews = this.getRandomNews();
    }

    private List<ArticleEntity> getRandomNews() {
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject select = new BasicDBObject();
        // select.put("top", StatusTypes.ARTICLE_REGULAR);
        select.put("status", StatusTypes.PUBLISHED);
        List<ArticleEntity> result = new ArrayList<ArticleEntity>();
        DBCollection collection = initBean.getArticleCollection();
        try {
            long count = collection.getCount();
            int limit = 5; //or whatever you want
            if (count <= limit) {
                DBCursor cursor = collection.find(select).sort(sortCriteria);
                while (cursor.hasNext()) {
                    DBObject document = cursor.next();
                    ArticleEntity article = new ArticleEntity();
                    article.setId((Long) document.get("id"));
                    article.setCategoryId((Long) document.get("categoryId"));
                    article.setCategoryName((String) document.get("categoryName"));
                    article.setTitle((String) document.get("title"));
                    article.setHeader((String) document.get("header"));
                    article.setImageId((String) document.get("imageId"));
                    article.setAuthorName((String) document.get("authorName"));
                    article.setSlug((String) document.get("slug"));
                    article.setPermalink((String) document.get("permalink"));
                    article.setArticleTopStatus((Integer) document.get("top"));
                    result.add(article);
                }
            } else {
                long skip = Math.round((double) count / limit);
                DBCursor cursor = collection.find(select).sort(sortCriteria);
                while (result.size() < limit) {
                    int offset = (int) ((skip * result.size() + (int) ((Math.random() * skip) % count)) % count);
                    //System.out.println(offset);
                    DBObject next = cursor.skip(offset).next();
                    DBObject document = cursor.next();
                    ArticleEntity article = new ArticleEntity();
                    article.setId((Long) document.get("id"));
                    article.setCategoryId((Long) document.get("categoryId"));
                    article.setCategoryName((String) document.get("categoryName"));
                    article.setTitle((String) document.get("title"));
                    article.setHeader((String) document.get("header"));
                    article.setImageId((String) document.get("imageId"));
                    article.setAuthorName((String) document.get("authorName"));
                    article.setSlug((String) document.get("slug"));
                    article.setPermalink((String) document.get("permalink"));
                    article.setArticleTopStatus((Integer) document.get("top"));
                    result.add(article);
                    cursor = collection.find(select);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<ArticleEntity> getTopLinks() {
        List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
        String sort = "viewCount";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("top", StatusTypes.ARTICLE_TOP);
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(0).limit(10);
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

    private List<ArticleEntity> getLastLinks() {
        List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("top", StatusTypes.ARTICLE_LATEST);
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(0).limit(10);
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

    private List<ArticleEntity> getRegularLinks() {
        List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("top", StatusTypes.ARTICLE_REGULAR);
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(0).limit(10);
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

    public List<ArticleEntity> getShowRandomNews() {
        return showRandomNews;
    }

    public void setShowRandomNews(List<ArticleEntity> showRandomNews) {
        this.showRandomNews = showRandomNews;
    }

    public List<ArticleEntity> getShowTopLinks() {
        return showTopLinks;
    }

    public List<ArticleEntity> getShowLatestLinks() {
        return showLatestLinks;
    }

    public List<ArticleEntity> getShowRegularLinks() {
        return showRegularLinks;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
}
