package backend.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
public class ArticleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long categoryId;
    private Long userId;
    private Long authorId;
    private Integer status;
    private String categoryName;
    private String title;
    private String header;
    private String content;
    private String slug;
    private Date datePosted;
    private String languageCode;
    private String imageId;
    private String authorName;
    private String metaDocumentState = "Dynamic";
    private String metaLocale = "en_EN";
    private String metaTitle;
    private String metaDescription;
    private String metaSiteName;
    private String metaType = "article";
    private String videoCode;//youtube embeded code
    private String googleAds;//ads under article
    private String permalink;
    private String hostName;
    private Integer articleTopStatus;
    private boolean update = false;
    private Integer viewCount = 1;

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }  

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }    

    public Integer getArticleTopStatus() {
        return articleTopStatus;
    }

    public void setArticleTopStatus(Integer articleTopStatus) {
        this.articleTopStatus = articleTopStatus;
    }       

    public String getHostName() {        
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getVideoCode() {
        return videoCode;
    }

    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }

    public String getGoogleAds() {
        return googleAds;
    }

    public void setGoogleAds(String googleAds) {
        this.googleAds = googleAds;
    }

    public String getMetaDocumentState() {
        return metaDocumentState;
    }

    public void setMetaDocumentState(String metaDocumentState) {
        this.metaDocumentState = metaDocumentState;
    }

    public String getMetaLocale() {
        return metaLocale;
    }

    public void setMetaLocale(String metaLocale) {
        this.metaLocale = metaLocale;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaSiteName() {
        return metaSiteName;
    }

    public void setMetaSiteName(String metaSiteName) {
        this.metaSiteName = metaSiteName;
    }

    public String getMetaType() {
        return metaType;
    }

    public void setMetaType(String metaType) {
        this.metaType = metaType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 41 * hash + (this.categoryId != null ? this.categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArticleEntity other = (ArticleEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.categoryId != other.categoryId && (this.categoryId == null || !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ArticleEntity{" + "id=" + id + ", categoryId=" + categoryId + ", categoryName=" + categoryName + ", title=" + title + ", header=" + header + ", slug=" + slug + ", datePosted=" + datePosted + ", languageCode=" + languageCode + '}';
    }
}
