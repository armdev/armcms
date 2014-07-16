package frontend.web.beans.articles;

import backend.entities.ArticleCategoriesEntity;
import backend.facades.ArticleCategoriesFacade;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.*;

@ManagedBean(name = "ArticleCategoriesBean")
@ViewScoped
public class ArticleCategoriesBean implements Serializable {

    @ManagedProperty("#{ArticleCategoriesFacade}")
    private ArticleCategoriesFacade articleCategoriesFacade;
    private List<ArticleCategoriesEntity> categoriesList;

    public ArticleCategoriesBean() {
    }

    public List<ArticleCategoriesEntity> getCategoriesList() {
        categoriesList = articleCategoriesFacade.getCategoriesList();
        return categoriesList;
    }

    public void remove(Long id) {
        articleCategoriesFacade.removeCategory(id);
    }

    public void setArticleCategoriesFacade(ArticleCategoriesFacade articleCategoriesFacade) {
        this.articleCategoriesFacade = articleCategoriesFacade;
    }
    
    
}
