package backend.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
public class VideoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String url;
    private Integer status;//active/disabled
    private Integer location;//on top page, news page    
    private Date datePosted;
    private String slug;
    private String hostName;
    private String permalink;

    public VideoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 37 * hash + (this.url != null ? this.url.hashCode() : 0);
        hash = 37 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 37 * hash + (this.location != null ? this.location.hashCode() : 0);
        hash = 37 * hash + (this.datePosted != null ? this.datePosted.hashCode() : 0);
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
        final VideoEntity other = (VideoEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
            return false;
        }
        if (this.status != other.status && (this.status == null || !this.status.equals(other.status))) {
            return false;
        }
        if (this.location != other.location && (this.location == null || !this.location.equals(other.location))) {
            return false;
        }
        if (this.datePosted != other.datePosted && (this.datePosted == null || !this.datePosted.equals(other.datePosted))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VideoEntity{" + "id=" + id + ", title=" + title + ", url=" + url + ", status=" + status + ", location=" + location + ", datePosted=" + datePosted + '}';
    }
}
