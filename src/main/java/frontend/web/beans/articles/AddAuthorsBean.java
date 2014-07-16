package frontend.web.beans.articles;

import backend.entities.ArticleAuthorsEntity;
import backend.facades.ArticleManagerFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean(name = "AddAuthorsBean")
@RequestScoped
public class AddAuthorsBean implements Serializable {

    @ManagedProperty("#{ArticleManagerFacade}")
    private ArticleManagerFacade articleManagerFacade;
    private ArticleAuthorsEntity articleAuthorsEntity;

    public AddAuthorsBean() {
    }

    @PostConstruct
    public void init() {
        articleAuthorsEntity = new ArticleAuthorsEntity();
    }

    public String addAuthor() {
        articleManagerFacade.addArticleAuthor(articleAuthorsEntity);
        return "authorslist";
    }

    public List<ArticleAuthorsEntity> getAuthorsList() {
        return articleManagerFacade.getAuthorsList();
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
