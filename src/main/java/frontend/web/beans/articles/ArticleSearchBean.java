package frontend.web.beans.articles;

import backend.connections.DbInitBean;
import backend.entities.ArticleEntity;
import backend.facades.ArticleManagerFacade;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "ArticleSearchBean")
@ViewScoped
public class ArticleSearchBean implements Serializable {

    @ManagedProperty("#{DbInitBean}")
    private DbInitBean initBean;
    private String searchKey = null;
    private DbListDataModel listDataModel;
    private Integer pageSize = 10;
    @ManagedProperty("#{ArticleManagerFacade}")
    private ArticleManagerFacade articleManagerFacade;
    private ArticleEntity articleEntity;
    private String search;

    public ArticleSearchBean() {
        articleEntity = new ArticleEntity();
        listDataModel = new DbListDataModel();
        listDataModel.setPageSize(pageSize);
    }

    @PostConstruct
    public void init() {
    }

    public void search() {
        search = searchKey;
    }

    public class DbListDataModel extends LazyDataModel<ArticleEntity> {

        public long total = 0;

        @Override
        public List<ArticleEntity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
            List<ArticleEntity> list = null;
            try {
                int count = (first + pageSize);                
                if(search != null){
                total = articleManagerFacade.getSearchCount(search);
                list = articleManagerFacade.getResultList(search, first, count);
                }
            } catch (Exception es) {
                es.printStackTrace();
            }
            return list;
        }

        @Override
        public ArticleEntity getRowData(String rowKey) {
            return null;
        }

        @Override
        public Object getRowKey(ArticleEntity object) {
            return object.getId();
        }

        @Override
        public int getRowCount() {
            return (int) total;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public ArticleEntity getArticleEntity() {
        return articleEntity;
    }

    public void setArticleEntity(ArticleEntity articleEntity) {
        this.articleEntity = articleEntity;
    }

    public DbListDataModel getListDataModel() {
        return listDataModel;
    }

    public void setArticleManagerFacade(ArticleManagerFacade articleManagerFacade) {
        this.articleManagerFacade = articleManagerFacade;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setInitBean(DbInitBean initBean) {
        this.initBean = initBean;
    }
}
