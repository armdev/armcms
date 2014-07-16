package backend.facades;

import backend.connections.DbInitBean;
import backend.connections.MongoConnectionFactory;
import backend.entities.ArticleAuthorsEntity;
import backend.entities.ArticleCategoriesEntity;
import backend.entities.ArticleEntity;
import backend.entities.GeneralSettingsEntity;
import backend.facades.site.ArticleIdeasBean;
import backend.facades.site.ArticleIdeasContinueBean;
import backend.facades.site.ArticleLatestContinueBean;
import backend.facades.site.ArticleLatestNewsBean;
import backend.facades.site.ArticleOpinionBean;
import backend.facades.site.ArticleOpinionContinueBean;
import backend.facades.site.ArticleRegularContinueBean;
import backend.facades.site.ArticleSpecialBean;
import backend.facades.site.ArticleTopBean;
import backend.facades.site.ArticleTopContinueBean;
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
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "ArticleManagerFacade")
@ApplicationScoped
public class ArticleManagerFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userManagerFacade;
    @ManagedProperty("#{GeneralSettingsFacade}")
    private GeneralSettingsFacade generalSettingsFacade;
    @ManagedProperty("#{ArticleCategoriesFacade}")
    private ArticleCategoriesFacade articleCategoriesFacade;
    private MongoConnectionFactory mongoFactory;
    @ManagedProperty("#{ArticleTopBean}")
    private ArticleTopBean articleTopBean;
    @ManagedProperty("#{ArticleLatestNewsBean}")
    private ArticleLatestNewsBean articleLatestNewsBean;
    @ManagedProperty("#{ArticleSpecialBean}")
    private ArticleSpecialBean articleSpecialBean;
    @ManagedProperty("#{ArticleOpinionBean}")
    private ArticleOpinionBean articleOpinionBean;
    @ManagedProperty("#{ArticleLatestContinueBean}")
    private ArticleLatestContinueBean articleLatestContinueBean;
    @ManagedProperty("#{ArticleIdeasContinueBean}")
    private ArticleIdeasContinueBean articleIdeasContinueBean;
    @ManagedProperty("#{ArticleOpinionContinueBean}")
    private ArticleOpinionContinueBean articleOpinionContinueBean;
    @ManagedProperty("#{ArticleRegularContinueBean}")
    private ArticleRegularContinueBean articleRegularContinueBean;
    @ManagedProperty("#{ArticleTopContinueBean}")
    private ArticleTopContinueBean articleTopContinueBean;
    @ManagedProperty("#{ArticleIdeasBean}")
    private ArticleIdeasBean articleIdeasBean;
    private ResourceBundle bundle;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public ArticleManagerFacade() {
    }

    public List<ArticleEntity> getResultList(String searchKey, Integer start, Integer count) {
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
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(start).limit(count);       
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            ArticleEntity article = new ArticleEntity();
            article.setId((Long) document.get("id"));
            article.setCategoryId((Long) document.get("categoryId"));
            article.setCategoryName((String) document.get("categoryName"));
            article.setTitle((String) document.get("title"));
            article.setHeader((String) document.get("header"));
            article.setStatus((Integer) document.get("status"));
            article.setDatePosted((Date) document.get("datePosted"));
            article.setAuthorName((String) document.get("authorName"));
            article.setSlug((String) document.get("slug"));
            article.setPermalink((String) document.get("permalink"));
            article.setArticleTopStatus((Integer) document.get("top"));
            articles.add(article);
        }
        return articles;
    }

    public Integer getSearchCount(String searchKey) {
        Pattern search = Pattern.compile(searchKey, Pattern.CASE_INSENSITIVE);
        Integer listCount = 0;
        try {
            BasicDBObject query1 = new BasicDBObject("content", search);
            BasicDBObject query2 = new BasicDBObject("header", search);
            BasicDBObject query3 = new BasicDBObject("title", search);
            ArrayList<BasicDBObject> myList = new ArrayList<BasicDBObject>();
            myList.add(query1);
            myList.add(query2);
            myList.add(query3);
            BasicDBObject query = new BasicDBObject("$or", myList);
            DBCursor cursor = initBean.getArticleCollection().find(query);
            listCount = cursor.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCount;
    }

    public boolean changeStaticPagesPermalinks() {
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
        GeneralSettingsEntity generalSettingsEntity = generalSettingsFacade.getSettings();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getStaticPagesColl().find(query).sort(sortCriteria);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                BasicDBObject object = new BasicDBObject();
                Long id = (Long) document.get("id");
                String slug = (String) document.get("title");
                String permalink = generalSettingsEntity.getSiteAddress() + "/pages/" + id + "/" + slug;
                object.append("$set", new BasicDBObject()
                        .append("permalink", permalink));
                initBean.getStaticPagesColl().update(new BasicDBObject().append("id", id), object);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("success"), bundle.getString("success")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean changeAllArticlesPermalinks() {
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
        GeneralSettingsEntity generalSettingsEntity = generalSettingsFacade.getSettings();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                BasicDBObject object = new BasicDBObject();
                Long id = (Long) document.get("id");
                String slug = (String) document.get("slug");
                String permalink = generalSettingsEntity.getSiteAddress() + "/fullnews/" + id + "/" + slug;
                object.append("$set", new BasicDBObject()
                        .append("permalink", permalink)
                        .append("hostname", generalSettingsEntity.getSiteAddress()));
                initBean.getArticleCollection().update(new BasicDBObject().append("id", id), object);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("success"), bundle.getString("success")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean changeVideoPermalinks() {
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
        GeneralSettingsEntity generalSettingsEntity = generalSettingsFacade.getSettings();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getVideoColl().find(query).sort(sortCriteria);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                BasicDBObject object = new BasicDBObject();
                Long id = (Long) document.get("id");
                String slug = (String) document.get("slug");
                String permalink = generalSettingsEntity.getSiteAddress() + "/video/" + id + "/" + slug;
                object.append("$set", new BasicDBObject()
                        .append("permalink", permalink)
                        .append("hostname", generalSettingsEntity.getSiteAddress()));
                initBean.getVideoColl().update(new BasicDBObject().append("id", id), object);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("success"), bundle.getString("success")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean changeGalleryPermalinks() {
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
        GeneralSettingsEntity generalSettingsEntity = generalSettingsFacade.getSettings();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getGalleryColl().find(query).sort(sortCriteria);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                BasicDBObject object = new BasicDBObject();
                Long id = (Long) document.get("id");
                String slug = (String) document.get("slug");
                String permalink = generalSettingsEntity.getSiteAddress() + "/gallery/" + id + "/" + slug;
                object.append("$set", new BasicDBObject()
                        .append("permalink", permalink)
                        .append("hostname", generalSettingsEntity.getSiteAddress()));
                initBean.getGalleryColl().update(new BasicDBObject().append("id", id), object);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("success"), bundle.getString("success")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<ArticleAuthorsEntity> getAuthorsList() {
        List<ArticleAuthorsEntity> authors = new ArrayList<ArticleAuthorsEntity>();
        String sort = "firstname";
        String order = "asc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = initBean.getArticleAuthorsCollection().find(query).sort(sortCriteria);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            ArticleAuthorsEntity author = new ArticleAuthorsEntity();
            author.setId((Long) document.get("id"));
            author.setFirstname((String) document.get("firstname"));
            author.setLastname((String) document.get("lastname"));
            author.setEmail((String) document.get("email"));
            author.setFullname((String) document.get("fullname"));
            authors.add(author);
        }
        return authors;
    }

    public ArticleAuthorsEntity getAuthorById(Long id) {
        ArticleAuthorsEntity author = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = initBean.getArticleAuthorsCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                author = new ArticleAuthorsEntity();
                author.setId((Long) document.get("id"));
                author.setFirstname((String) document.get("firstname"));
                author.setLastname((String) document.get("lastname"));
                author.setEmail((String) document.get("email"));
                author.setFullname((String) document.get("fullname"));
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return author;
    }

    public void updateAuthor(ArticleAuthorsEntity articleAuthorsEntity) {
        BasicDBObject document = new BasicDBObject();
        document.append("$set", new BasicDBObject()
                .append("fullname", articleAuthorsEntity.getFirstname() + " " + articleAuthorsEntity.getLastname())
                .append("firstname", articleAuthorsEntity.getFirstname())
                .append("lastname", articleAuthorsEntity.getLastname())
                .append("email", articleAuthorsEntity.getEmail()));
        initBean.getArticleAuthorsCollection().update(new BasicDBObject().append("id", articleAuthorsEntity.getId()), document);
    }

    public Long addArticleAuthor(ArticleAuthorsEntity articleAuthorsEntity) {
        try {
            BasicDBObject document = new BasicDBObject();
            articleAuthorsEntity.setId(Util.longValue(getNextId(initBean.getDatabase(), "articleAuthorSeqGen")));
            document.put("id", articleAuthorsEntity.getId());
            document.put("fullname", articleAuthorsEntity.getFirstname() + " " + articleAuthorsEntity.getLastname());
            document.put("firstname", articleAuthorsEntity.getFirstname());
            document.put("lastname", articleAuthorsEntity.getLastname());
            document.put("email", articleAuthorsEntity.getEmail());
            initBean.getArticleAuthorsCollection().insert(document, WriteConcern.SAFE);
        } catch (Exception e) {
            e.getMessage();
        }
        return articleAuthorsEntity.getId();
    }

    private String replaceSpace(String s) {
        if (s.length() < 2) {
            if (s.equals(" ")) {
                return "-";
            } else {
                return s;
            }
        }
        if (s.charAt(0) == ' ') {
            return "-" + replaceSpace(s.substring(1).toLowerCase());
        } else {
            return s.substring(0, 1) + replaceSpace(s.substring(1).toLowerCase());
        }
    }

    public Long addNewArticle(ArticleEntity articleEntity) {
        try {
            BasicDBObject document = new BasicDBObject();
            articleEntity.setId(Util.longValue(getNextId(initBean.getDatabase(), "articleSeqGen")));
            document.put("id", articleEntity.getId());
            document.put("categoryId", articleEntity.getCategoryId());
            document.put("authorId", articleEntity.getAuthorId());
            ArticleAuthorsEntity author = this.getAuthorById(articleEntity.getAuthorId());
            document.put("authorName", author.getFullname());
            document.put("status", articleEntity.getStatus());
            ArticleCategoriesEntity category = articleCategoriesFacade.getCategoryById(articleEntity.getCategoryId());
            document.put("categoryName", category.getName());
            document.put("title", articleEntity.getTitle());
            document.put("header", articleEntity.getHeader());
            document.put("content", articleEntity.getContent());
            document.put("slug", this.replaceSpace(articleEntity.getSlug()));
            document.put("datePosted", new Date());
            document.put("languageCode", articleEntity.getLanguageCode());
            document.put("imageId", articleEntity.getImageId());
            document.put("authorName", articleEntity.getAuthorName());
            document.put("metaLocale", articleEntity.getMetaLocale());
            document.put("metaTitle", articleEntity.getMetaTitle());
            document.put("metaDescription", articleEntity.getMetaDescription());
            document.put("metaSiteName", articleEntity.getMetaSiteName());
            document.put("metaType", articleEntity.getMetaType());
            document.put("videoCode", articleEntity.getVideoCode());
            document.put("googleAds", articleEntity.getGoogleAds());
            document.put("top", articleEntity.getArticleTopStatus());
            document.put("viewCount", articleEntity.getViewCount());
            GeneralSettingsEntity settings = generalSettingsFacade.getSettings();
            articleEntity.setHostName(settings.getSiteAddress());
            String permalink = articleEntity.getHostName() + "/fullnews/" + articleEntity.getId() + "/" + this.replaceSpace(articleEntity.getSlug());
            document.put("hostname", articleEntity.getHostName());
            document.put("permalink", permalink);
            initBean.getArticleCollection().insert(document, WriteConcern.SAFE);

            if (articleEntity.getStatus().equals(StatusTypes.PUBLISHED)
                    && articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_TOP)) {
                articleTopBean.init();
                articleTopContinueBean.init();
            }

            if (articleEntity.getStatus().equals(StatusTypes.PUBLISHED)
                    && articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_LATEST)) {
                articleLatestNewsBean.init();
                articleLatestContinueBean.init();
            }

            if (articleEntity.getStatus().equals(StatusTypes.PUBLISHED)
                    && articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_SPECIAL)) {
                articleSpecialBean.init();
            }
            if (articleEntity.getStatus().equals(StatusTypes.PUBLISHED)
                    && articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_OPINION)) {
                articleOpinionBean.init();
                articleOpinionContinueBean.init();
            }

            if (articleEntity.getStatus().equals(StatusTypes.PUBLISHED)
                    && articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_REGULAR)) {
                articleRegularContinueBean.init();
            }
            if (articleEntity.getStatus().equals(StatusTypes.PUBLISHED)
                    && articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_IDEAS)) {
                articleIdeasContinueBean.init();
                articleIdeasBean.init();
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return articleEntity.getId();
    }

    public void updateArticleView(ArticleEntity articleEntity) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.append("$set", new BasicDBObject()
                    .append("title", articleEntity.getTitle())
                    .append("viewCount", articleEntity.getViewCount() + 1));
            initBean.getArticleCollection().update(new BasicDBObject().append("id", articleEntity.getId()), document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateArticle(ArticleEntity articleEntity) {
        try {
            BasicDBObject document = new BasicDBObject();
            GeneralSettingsEntity settings = generalSettingsFacade.getSettings();
            articleEntity.setHostName(settings.getSiteAddress());
            String permalink = articleEntity.getHostName() + "/fullnews/" + articleEntity.getId() + "/" + this.replaceSpace(articleEntity.getSlug());
            ArticleCategoriesEntity category = articleCategoriesFacade.getCategoryById(articleEntity.getCategoryId());

            ArticleAuthorsEntity author = this.getAuthorById(articleEntity.getAuthorId());
            if (articleEntity.isUpdate()) {
                document.append("$set", new BasicDBObject()
                        .append("categoryId", articleEntity.getCategoryId())
                        .append("authorId", articleEntity.getAuthorId())
                        .append("status", articleEntity.getStatus())
                        .append("authorName", author.getFullname())
                        .append("categoryName", category.getName())
                        .append("title", articleEntity.getTitle())
                        .append("header", articleEntity.getHeader())
                        .append("content", articleEntity.getContent())
                        .append("slug", this.replaceSpace(articleEntity.getSlug()))
                        .append("datePosted", new Date(System.currentTimeMillis()))
                        .append("languageCode", articleEntity.getLanguageCode())
                        .append("imageId", articleEntity.getImageId())
                        .append("metaLocale", articleEntity.getMetaLocale())
                        .append("metaTitle", articleEntity.getMetaTitle())
                        .append("metaDescription", articleEntity.getMetaDescription())
                        .append("metaSiteName", articleEntity.getMetaSiteName())
                        .append("videoCode", articleEntity.getVideoCode())
                        .append("top", articleEntity.getArticleTopStatus())
                        .append("hostname", articleEntity.getHostName())
                        .append("permalink", permalink)
                        .append("googleAds", articleEntity.getGoogleAds())
                        .append("metaType", articleEntity.getMetaType()));
            } else {
                document.append("$set", new BasicDBObject()
                        .append("categoryId", articleEntity.getCategoryId())
                        .append("authorId", articleEntity.getAuthorId())
                        .append("authorName", author.getFullname())
                        .append("status", articleEntity.getStatus())
                        .append("categoryName", category.getName())
                        .append("title", articleEntity.getTitle())
                        .append("header", articleEntity.getHeader())
                        .append("content", articleEntity.getContent())
                        .append("slug", this.replaceSpace(articleEntity.getSlug()))
                        .append("languageCode", articleEntity.getLanguageCode())
                        .append("imageId", articleEntity.getImageId())
                        .append("metaLocale", articleEntity.getMetaLocale())
                        .append("metaTitle", articleEntity.getMetaTitle())
                        .append("metaDescription", articleEntity.getMetaDescription())
                        .append("metaSiteName", articleEntity.getMetaSiteName())
                        .append("videoCode", articleEntity.getVideoCode())
                        .append("top", articleEntity.getArticleTopStatus())
                        .append("hostname", articleEntity.getHostName())
                        .append("permalink", permalink)
                        .append("googleAds", articleEntity.getGoogleAds())
                        .append("metaType", articleEntity.getMetaType()));
            }

            initBean.getArticleCollection().update(new BasicDBObject().append("id", articleEntity.getId()), document);

            if (articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_TOP)) {
                articleTopBean.init();
                articleTopContinueBean.init();
            }

            if (articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_LATEST)) {
                articleLatestNewsBean.init();
                articleLatestContinueBean.init();
            }

            if (articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_SPECIAL)) {
                articleSpecialBean.init();
            }
            if (articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_OPINION)) {
                articleOpinionBean.init();
                articleOpinionContinueBean.init();
            }

            if (articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_REGULAR)) {
                articleRegularContinueBean.init();
            }
            if (articleEntity.getArticleTopStatus().equals(StatusTypes.ARTICLE_IDEAS)) {
                articleIdeasContinueBean.init();
                articleIdeasBean.init();
            }

        } catch (Exception e) {
        }
    }

    public ArticleEntity getArticleById(Long id) {
        ArticleEntity article = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = initBean.getArticleCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                article = new ArticleEntity();
                article.setId((Long) document.get("id"));
                article.setCategoryId((Long) document.get("categoryId"));
                article.setAuthorId((Long) document.get("authorId"));                
                article.setCategoryName((String) document.get("categoryName"));
                article.setTitle((String) document.get("title"));
                article.setHeader((String) document.get("header"));
                article.setContent((String) document.get("content"));
                article.setSlug((String) document.get("slug"));
                article.setStatus((Integer) document.get("status"));
                article.setDatePosted((Date) document.get("datePosted"));
                article.setLanguageCode((String) document.get("languageCode"));
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
        } finally {
            cursor.close();
        }
        return article;
    }

    public List<ArticleEntity> getAllArticlesList(Integer skip, Integer limit) {
        List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();

        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(skip).limit(limit);
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            ArticleEntity article = new ArticleEntity();
            article.setId((Long) document.get("id"));
            article.setCategoryId((Long) document.get("categoryId"));
            article.setUserId((Long) document.get("userId"));
            article.setAuthorId((Long) document.get("authorId"));
            article.setAuthorName((String) document.get("authorName"));
            article.setStatus((Integer) document.get("status"));
            article.setCategoryName((String) document.get("categoryName"));
            article.setTitle((String) document.get("title"));
            article.setHeader((String) document.get("header"));
            article.setContent((String) document.get("content"));
            article.setSlug((String) document.get("slug"));
            article.setDatePosted((Date) document.get("datePosted"));
            article.setLanguageCode((String) document.get("languageCode"));
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
            articles.add(article);
        }
        return articles;
    }

    public Integer getArticlesCount() {
        Integer listCount = 0;
        try {
            BasicDBObject query = new BasicDBObject();
            DBCursor cursor = initBean.getArticleCollection().find(query);
            listCount = cursor.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCount;
    }

    public void removeArticle(Long id) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("id", id);
            if (document != null) {
                initBean.getArticleCollection().remove(document);
            }
            articleTopBean.init();
            articleTopContinueBean.init();
            articleLatestNewsBean.init();
            articleLatestContinueBean.init();
            articleSpecialBean.init();
            articleOpinionBean.init();
            articleOpinionContinueBean.init();
            articleRegularContinueBean.init();
            articleIdeasContinueBean.init();
            articleIdeasBean.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setArticleOpinionBean(ArticleOpinionBean articleOpinionBean) {
        this.articleOpinionBean = articleOpinionBean;
    }

    public void setArticleTopBean(ArticleTopBean articleTopBean) {
        this.articleTopBean = articleTopBean;
    }

    public void setUserManagerFacade(UserManagerFacade userManagerFacade) {
        this.userManagerFacade = userManagerFacade;
    }

    public void setArticleCategoriesFacade(ArticleCategoriesFacade articleCategoriesFacade) {
        this.articleCategoriesFacade = articleCategoriesFacade;
    }

    public void setGeneralSettingsFacade(GeneralSettingsFacade generalSettingsFacade) {
        this.generalSettingsFacade = generalSettingsFacade;
    }

    public void setArticleLatestNewsBean(ArticleLatestNewsBean articleLatestNewsBean) {
        this.articleLatestNewsBean = articleLatestNewsBean;
    }

    public void setArticleSpecialBean(ArticleSpecialBean articleSpecialBean) {
        this.articleSpecialBean = articleSpecialBean;
    }

    public void setArticleLatestContinueBean(ArticleLatestContinueBean articleLatestContinueBean) {
        this.articleLatestContinueBean = articleLatestContinueBean;
    }

    public void setArticleIdeasContinueBean(ArticleIdeasContinueBean articleIdeasContinueBean) {
        this.articleIdeasContinueBean = articleIdeasContinueBean;
    }

    public void setArticleOpinionContinueBean(ArticleOpinionContinueBean articleOpinionContinueBean) {
        this.articleOpinionContinueBean = articleOpinionContinueBean;
    }

    public void setArticleRegularContinueBean(ArticleRegularContinueBean articleRegularContinueBean) {
        this.articleRegularContinueBean = articleRegularContinueBean;
    }

    public void setArticleTopContinueBean(ArticleTopContinueBean articleTopContinueBean) {
        this.articleTopContinueBean = articleTopContinueBean;
    }

    public void setArticleIdeasBean(ArticleIdeasBean articleIdeasBean) {
        this.articleIdeasBean = articleIdeasBean;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }

    private static String getNextId(DB db, String seq_name) {
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
