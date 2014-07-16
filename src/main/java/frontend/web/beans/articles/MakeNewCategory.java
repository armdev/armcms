package frontend.web.beans.articles;

import backend.entities.ArticleCategoriesEntity;
import backend.facades.ArticleCategoriesFacade;
import java.io.Serializable;
import javax.faces.bean.*;

@ManagedBean(name = "MakeNewCategory")
@ViewScoped
public class MakeNewCategory implements Serializable {

    @ManagedProperty("#{ArticleCategoriesFacade}")
    private ArticleCategoriesFacade articleCategoriesFacade;
    private ArticleCategoriesEntity articleCategoriesEntity;

    public MakeNewCategory() {
        articleCategoriesEntity = new ArticleCategoriesEntity();
    }

    public String makeCategory() {
        articleCategoriesFacade.addNewCategory(articleCategoriesEntity);
        return "articlecategories";
    }

    public void setArticleCategoriesFacade(ArticleCategoriesFacade articleCategoriesFacade) {
        this.articleCategoriesFacade = articleCategoriesFacade;
    }

    public ArticleCategoriesEntity getArticleCategoriesEntity() {
        return articleCategoriesEntity;
    }

    public void setArticleCategoriesEntity(ArticleCategoriesEntity articleCategoriesEntity) {
        this.articleCategoriesEntity = articleCategoriesEntity;
    }
}
