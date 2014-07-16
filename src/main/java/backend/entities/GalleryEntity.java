package backend.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
public class GalleryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private Integer status;//active/disabled    
    private Date datePosted;
    private List<String> images = new ArrayList<String>();
    private String slug;
    private String hostName;
    private String permalink;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

  
    public GalleryEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 23 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 23 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 23 * hash + (this.datePosted != null ? this.datePosted.hashCode() : 0);
        hash = 23 * hash + (this.images != null ? this.images.hashCode() : 0);
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
        final GalleryEntity other = (GalleryEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if (this.status != other.status && (this.status == null || !this.status.equals(other.status))) {
            return false;
        }
        if (this.datePosted != other.datePosted && (this.datePosted == null || !this.datePosted.equals(other.datePosted))) {
            return false;
        }
        if (this.images != other.images && (this.images == null || !this.images.equals(other.images))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GalleryEntity{" + "id=" + id + ", title=" + title + ", status=" + status + ", datePosted=" + datePosted + ", images=" + images + '}';
    }
}
