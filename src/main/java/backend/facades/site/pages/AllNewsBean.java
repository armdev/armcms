package backend.facades.site.pages;

import backend.connections.DbInitBean;
import backend.entities.ArticleEntity;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import frontend.web.utils.PagingUtil;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author armen
 */
@ManagedBean(name = "AllNewsBean")
@ViewScoped
public class AllNewsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<ArticleEntity> newsList;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;
    private final Integer ITEMS_PER_PAGE = 10;
    private final Integer PAGES_IN_SECTION = 5;
    private Integer page = 1;
    private Integer projectCount = null;
    private Integer start;
    private Integer count;
    private PagingUtil pagingUtil;
    private FacesContext context = null;
    private ExternalContext ex = null;   

    public AllNewsBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("page")) {
            page = Util.integerValue(ex.getRequestParameterMap().get("page"));
            if (page == null) {
                page = 1;
            }
        } else {
            page = 1;
        }
    }

    public List<ArticleEntity> getNewsList() {
        try {
            start = (page - 1) * ITEMS_PER_PAGE;
            count = ITEMS_PER_PAGE;
            newsList = this.getAllNewsList(start, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }

    public Integer getNewsCount() {
        if (projectCount == null) {
            try {
                projectCount = this.getAllNewsCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return projectCount;
    }

    public Boolean getPagingUtil() {
        projectCount = this.getNewsCount();
        try {
            pagingUtil = new PagingUtil(page, projectCount, ITEMS_PER_PAGE, PAGES_IN_SECTION);
            if (page > pagingUtil.getPageCount()) {
                page = 1;
                pagingUtil = new PagingUtil(page, projectCount, ITEMS_PER_PAGE, PAGES_IN_SECTION);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Vector getPaging() {
        Vector paging = new Vector();
        for (int i = getCurrPage(); i <= ((getCurrPage() + PAGES_IN_SECTION - 1) < getPageCount() ? (getCurrPage() + PAGES_IN_SECTION - 1) : getPageCount()); i++) {
            paging.add(i);
        }
        return paging;
    }

    public int getCurrPage() {
        return page;
    }

    public int getPageCount() {
        if (pagingUtil == null) {
            return 1;
        }
        return pagingUtil.getPageCount();
    }

    public Boolean getHasPrevious() {
        if (pagingUtil == null) {
            return false;
        }
        return pagingUtil.hasPrevious();
    }

    public Boolean getHasNext() {
        if (pagingUtil == null) {
            return false;
        }
        return pagingUtil.hasNext();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    private Integer getAllNewsCount() {
        Integer listCount = 0;
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("status", StatusTypes.PUBLISHED);
            DBCursor cursor = initBean.getArticleCollection().find(query);
            listCount = cursor.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCount;
    }

    private List<ArticleEntity> getAllNewsList(Integer start, Integer count) {
        List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
        String sort = "datePosted";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getArticleCollection().find(query).sort(sortCriteria).skip(start).limit(count);
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
