package backend.facades.site;

import backend.connections.DbInitBean;
import backend.entities.StaticPagesEntity;
import backend.facades.StaticPagesFacade;
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
@ManagedBean(name = "StaticPagesLoaderBean")
@ApplicationScoped
public class StaticPagesLoaderBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<StaticPagesEntity> pagesList;
    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;  
    
    public StaticPagesLoaderBean() {
    }

    @PostConstruct
    public void init() {
        pagesList = this.getStaticPagesList();
    }

    public List<StaticPagesEntity> getStaticPagesList() {
        List<StaticPagesEntity> list = new ArrayList<StaticPagesEntity>();
        StaticPagesEntity item = null;
        String sort = "sortOrder";
        String order = "asc";
        DBObject sortCriteria = new BasicDBObject(sort, "asc".equals(order) ? 1 : -1);
        BasicDBObject query = new BasicDBObject();
        query.put("status", StatusTypes.ACTIVE);
        DBCursor cursor = initBean.getStaticPagesColl().find(query).sort(sortCriteria);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                item = new StaticPagesEntity();
                item.setId((Long) document.get("id"));
                item.setTitle((String) document.get("title"));
                item.setContent((String) document.get("content"));
                list.add(item);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public List<StaticPagesEntity> getPagesList() {
        return pagesList;
    }

    public void setPagesList(List<StaticPagesEntity> pagesList) {
        this.pagesList = pagesList;
    }

  
    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
}
