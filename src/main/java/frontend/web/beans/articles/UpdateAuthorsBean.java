package frontend.web.beans.articles;

import backend.entities.ArticleAuthorsEntity;
import backend.facades.ArticleManagerFacade;
import frontend.web.utils.Util;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "UpdateAuthorsBean")
@ViewScoped
public class UpdateAuthorsBean implements Serializable {

    @ManagedProperty("#{ArticleManagerFacade}")
    private ArticleManagerFacade articleManagerFacade;
    private ArticleAuthorsEntity articleAuthorsEntity;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private Long authorId;

    public UpdateAuthorsBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        articleAuthorsEntity = new ArticleAuthorsEntity();
         if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            authorId = Util.longValue(ex.getRequestParameterMap().get("next"));
        }
         articleAuthorsEntity = articleManagerFacade.getAuthorById(authorId);
    }

    public String updateAuthor() {
        articleAuthorsEntity.setId(authorId);
        articleManagerFacade.updateAuthor(articleAuthorsEntity);
        return "authorslist";
    }

    public ArticleAuthorsEntity getArticleAuthorsEntity() {
        return articleAuthorsEntity;
    }

    public void setArticleAuthorsEntity(ArticleAuthorsEntity articleAuthorsEntity) {
        this.articleAuthorsEntity = articleAuthorsEntity;
    }  
    

    public void setArticleManagerFacade(ArticleManagerFacade articleManagerFacade) {
        this.articleManagerFacade = articleManagerFacade;
    }
    
    
}
