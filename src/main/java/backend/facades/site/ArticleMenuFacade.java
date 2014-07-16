package backend.facades.site;

import backend.connections.DbInitBean;
import backend.entities.ArticleCategoriesEntity;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author armen
 */
@ManagedBean(name = "ArticleMenuFacade")
@ApplicationScoped
public class ArticleMenuFacade implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<ArticleCategoriesEntity> menuList;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;

    public ArticleMenuFacade() {
    }

    @PostConstruct
    public void init() {
        menuList = this.getMenuAsList();
    }

    public List<ArticleCategoriesEntity> getMenuList() {
        return menuList;
    }

    public List<ArticleCategoriesEntity> getMenuAsList() {
        List<ArticleCategoriesEntity> list = new ArrayList<ArticleCategoriesEntity>();
        ArticleCategoriesEntity book = null;
        String sort = "sortOrder";
        String order = "asc";
        DBObject sortCriteria = new BasicDBObject(sort, "asc".equals(order) ? 1 : -1);
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.PUBLISHED);
        DBCursor cursor = initBean.getCategoriesCollection().find(query).sort(sortCriteria);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                book = new ArticleCategoriesEntity();
                book.setId((Long) document.get("id"));
                book.setName((String) document.get("name"));
                list.add(book);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
}
