package backend.facades.site.pages;

import backend.connections.DbInitBean;
import backend.entities.ArticleEntity;
import backend.facades.ArticleManagerFacade;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import frontend.web.utils.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "fullNewsBean")
@ViewScoped
public class FullNewsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArticleEntity articleEntity = new ArticleEntity();
    private FacesContext context = null;
    private ExternalContext ex = null;
    private Long newsId;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;
    @ManagedProperty("#{ArticleManagerFacade}")
    private ArticleManagerFacade articleManagerFacade;

    public FullNewsBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        articleEntity = new ArticleEntity();
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            newsId = Util.longValue((ex.getRequestParameterMap().get("next")));
        }
        if (newsId != null) {
            articleEntity = this.getArticleById(newsId);
        } else {
            redirect(context, "../site/index.jsf");
        }
    }
   
    private Long addLog(String ip, Long newsId) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("ip", ip);
            document.put("newsId", newsId);
            document.put("datePosted", new Date(System.currentTimeMillis()));
            initBean.getPageViewColl().insert(document, WriteConcern.SAFE);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkIp(String ip, Long newsId) {
        boolean retValue = false;
        BasicDBObject query = new BasicDBObject();
        query.put("newsId", newsId);
        query.put("ip", ip);
        DBCursor cursor = initBean.getPageViewColl().find(query);
        try {
            if (cursor != null && cursor.count() > 0) {
                retValue = true;
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return retValue;
    }

    public ArticleEntity getArticleById(Long id) {
        ArticleEntity article = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getArticleCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                article = new ArticleEntity();
                article.setId((Long) document.get("id"));
                article.setCategoryId((Long) document.get("categoryId"));
                article.setAuthorId((Long) document.get("authorId"));
                article.setStatus((Integer) document.get("status"));
                article.setCategoryName((String) document.get("categoryName"));
                article.setTitle((String) document.get("title"));
                article.setHeader((String) document.get("header"));
                article.setContent((String) document.get("content"));
                article.setSlug((String) document.get("slug"));
                article.setDatePosted((Date) document.get("datePosted"));
                article.setImageId((String) document.get("imageId"));
                article.setAuthorName((String) document.get("authorName"));
                article.setMetaLocale((String) document.get("metaLocale"));
                article.setMetaTitle((String) document.get("metaTitle"));
                article.setMetaDescription((String) document.get("metaDescription"));
                article.setMetaSiteName((String) document.get("metaSiteName"));
                article.setMetaType((String) document.get("metaType"));
                article.setVideoCode((String) document.get("videoCode"));
                article.setGoogleAds((String) document.get("googleAds"));
                article.setPermalink((String) document.get("permalink"));
                article.setArticleTopStatus((Integer) document.get("top"));
                article.setViewCount((Integer) document.get("viewCount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        this.updatePageView(article);
        return article;
    }

    private void updatePageView(ArticleEntity entity) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) ex.getRequest();
            String ip = httpServletRequest.getRemoteAddr();
            if (ip != null && entity.getId() != null) {
                boolean check = this.checkIp(ip, entity.getId());
                if (!check) {
                    this.addLog(ip, entity.getId());
                    //  System.out.println("VOTING FROM " + ip);
                    articleManagerFacade.updateArticleView(entity);
                } else {
                    // System.out.println("From your ip already voted " + ip);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setArticleManagerFacade(ArticleManagerFacade articleManagerFacade) {
        this.articleManagerFacade = articleManagerFacade;
    }

    public ArticleEntity getArticleEntity() {
        return articleEntity;
    }

    public void setArticleEntity(ArticleEntity articleEntity) {
        this.articleEntity = articleEntity;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }

    private static void redirect(FacesContext facesContext, String url) {
        try {
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }
}
