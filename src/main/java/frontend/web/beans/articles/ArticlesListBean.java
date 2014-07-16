package frontend.web.beans.articles;

import backend.entities.ArticleEntity;
import backend.facades.ArticleManagerFacade;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "ArticlesListBean")
@ViewScoped
public class ArticlesListBean implements Serializable {

    @ManagedProperty("#{ArticleManagerFacade}")
    private ArticleManagerFacade articleManagerFacade;  
    private ArticleEntity articleEntity;
    private List<ArticleEntity> articlesList;
    private DbListDataModel listDataModel;
    private Integer pageSize = 10;

    public ArticlesListBean() {        
        articleEntity = new ArticleEntity();
        listDataModel = new DbListDataModel();
        listDataModel.setPageSize(pageSize);
    }

    public class DbListDataModel extends LazyDataModel<ArticleEntity> {

        public long total = 0;

        @Override
        public List<ArticleEntity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
            List<ArticleEntity> list = null;
            try {
                int count = (first + pageSize);              
                total = articleManagerFacade.getArticlesCount();
                list = articleManagerFacade.getAllArticlesList(first, count);
               
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
    
    public String remove(Long id){
        articleManagerFacade.removeArticle(id);
        return null;
    }

    public DbListDataModel getListDataModel() {      
        return listDataModel;
    }


    public ArticleEntity getArticleEntity() {
        return articleEntity;
    }

    public void setArticleEntity(ArticleEntity articleEntity) {
        this.articleEntity = articleEntity;
    }

    public void setArticleManagerFacade(ArticleManagerFacade articleManagerFacade) {
        this.articleManagerFacade = articleManagerFacade;
    }
}
