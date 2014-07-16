package frontend.web.beans.articles;

import backend.entities.ArticleCategoriesEntity;
import backend.facades.ArticleCategoriesFacade;
import frontend.web.utils.Util;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "UpdateCategoryBean")
@ViewScoped
public class UpdateCategoryBean implements Serializable {

    @ManagedProperty("#{ArticleCategoriesFacade}")
    private ArticleCategoriesFacade categoryFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ArticleCategoriesEntity articleCategoriesEntity;
    private Long categoryId;

    public UpdateCategoryBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        articleCategoriesEntity = new ArticleCategoriesEntity();
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            categoryId = Util.longValue(ex.getRequestParameterMap().get("next"));
        }
    }

    @PostConstruct
    public void init() {
        articleCategoriesEntity = categoryFacade.getCategoryById(categoryId);

    }

    public String updateCategory() {
        articleCategoriesEntity.setId(categoryId);
        if (categoryId != null) {
            categoryFacade.updateCategory(articleCategoriesEntity);
        }
        return "articlecategories";
    }

    public void setCategoryFacade(ArticleCategoriesFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public ArticleCategoriesEntity getArticleCategoriesEntity() {
        return articleCategoriesEntity;
    }

    public void setArticleCategoriesEntity(ArticleCategoriesEntity articleCategoriesEntity) {
        this.articleCategoriesEntity = articleCategoriesEntity;
    }
}
