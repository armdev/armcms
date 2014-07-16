package frontend.web.beans.articles;

import backend.entities.ArticleAuthorsEntity;
import backend.entities.ArticleCategoriesEntity;
import backend.entities.ArticleEntity;
import backend.entities.FileEntity;
import backend.entities.UserEntity;
import backend.facades.ArticleCategoriesFacade;
import backend.facades.ArticleManagerFacade;
import backend.facades.FileManagerFacade;
import backend.facades.UserManagerFacade;
import backend.types.StatusTypes;
import frontend.web.utils.UploadUtil;
import frontend.web.utils.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.myfaces.custom.fileupload.UploadedFile;

@ManagedBean(name = "MakeNewArticleBean")
@ViewScoped
public class MakeNewArticleBean implements Serializable {

    @ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userManagerFacade;
    @ManagedProperty("#{ArticleManagerFacade}")
    private ArticleManagerFacade articleManagerFacade;
    @ManagedProperty("#{ArticleCategoriesFacade}")
    private ArticleCategoriesFacade articleCategoriesFacade;
    @ManagedProperty("#{FileManagerFacade}")
    private FileManagerFacade fileManager;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private UserEntity userEntity;
    private ArticleEntity articleEntity;
    private List<ArticleEntity> articlesList;
    private UploadedFile upFile = null;
    private String imageId;
    private boolean fileTooBig = false;
    private Long articleId;
    private ResourceBundle bundle;
    private Locale locale;
    private boolean saveSuccess = false;
    private Long userId;
    private SelectItem item;
    private List<SelectItem> categoriesList = null;
    private List<SelectItem> authorsList = null;

    public MakeNewArticleBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        locale = context.getViewRoot().getLocale();
        bundle = ResourceBundle.getBundle("ConfirmationMessages", locale);

    }

    @PostConstruct
    public void init() {
        articleEntity = new ArticleEntity();
    }

    public List<SelectItem> getAuthorsList() {
        if (authorsList == null) {
            authorsList = new ArrayList<SelectItem>();
            List<ArticleAuthorsEntity> list = articleManagerFacade.getAuthorsList();
            if (list != null) {
                for (ArticleAuthorsEntity o : list) {
                    item = new SelectItem(o.getId(), o.getFullname());
                    authorsList.add(item);
                }
            }
        }
        return authorsList;
    }

    public List<SelectItem> getCategoriesList() {
        if (categoriesList == null) {
            categoriesList = new ArrayList<SelectItem>();
            List<ArticleCategoriesEntity> list = articleCategoriesFacade.getCategoriesList();
            if (list.size() > 0) {
                for (ArticleCategoriesEntity o : list) {
                    item = new SelectItem(o.getId(), o.getName());
                    categoriesList.add(item);
                }
            }
        }
        return categoriesList;
    }

    public String addArticle() {
        FileEntity file = new FileEntity();
        try {
            long size = 0;
            String uploadedFileName = "";
            if (getUpFile() != null && getUpFile().getName() != null) {
                uploadedFileName = UploadUtil.trimFilePath(getUpFile().getName());
            }
            if (getUpFile() != null) {
                size = getUpFile().getSize();
                long maxSize = 5 * 1000000;
                if (size > maxSize) {
                    setFileTooBig(true);
                    return null;
                }
                String content = getUpFile().getContentType();
                InputStream stream = getUpFile().getInputStream();
                byte[] contentBytes = new byte[(int) size];
                stream.read(contentBytes);
                file.setMimetype(content);
                file.setFilesize(size);
                file.setTitle(uploadedFileName);
                file.setContent(contentBytes);
                file.setUserId(userId);
                file.setType(StatusTypes.POST_PHOTO);
                if (!content.equalsIgnoreCase("image/jpeg")
                        && !content.equalsIgnoreCase("image/pjpeg")
                        && !content.equalsIgnoreCase("image/jpg")
                        && !content.equalsIgnoreCase("image/gif")
                        && !content.equalsIgnoreCase("image/x-png")
                        && !content.equalsIgnoreCase("image/png")) {
                    try {
                        System.out.println("File upload error");
                        setFileTooBig(true);
                        return null;
                    } catch (Exception e) {
                    }
                }
                imageId = fileManager.addFile(file);
            }
        } catch (Exception ioe) {
        }
        if (imageId != null) {
            articleEntity.setImageId(imageId);
        }
        articleManagerFacade.addNewArticle(articleEntity);
        return "articleslist";
    }

    public boolean isSaveSuccess() {
        return saveSuccess;
    }

    public void setSaveSuccess(boolean saveSuccess) {
        this.saveSuccess = saveSuccess;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isFileTooBig() {
        return fileTooBig;
    }

    public void setFileTooBig(boolean fileTooBig) {
        this.fileTooBig = fileTooBig;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    private static void redirect(FacesContext facesContext, String url) {
        try {
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }

    public void setArticleCategoriesFacade(ArticleCategoriesFacade articleCategoriesFacade) {
        this.articleCategoriesFacade = articleCategoriesFacade;
    }

    public ArticleEntity getArticleEntity() {
        return articleEntity;
    }

    public void setArticleEntity(ArticleEntity articleEntity) {
        this.articleEntity = articleEntity;
    }

    public void setUserManagerFacade(UserManagerFacade userManagerFacade) {
        this.userManagerFacade = userManagerFacade;
    }

    public void setArticleManagerFacade(ArticleManagerFacade articleManagerFacade) {
        this.articleManagerFacade = articleManagerFacade;
    }

    public void setFileManager(FileManagerFacade fileManager) {
        this.fileManager = fileManager;
    }

    public UploadedFile getUpFile() {
        return upFile;
    }

    public void setUpFile(UploadedFile upFile) {
        this.upFile = upFile;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
