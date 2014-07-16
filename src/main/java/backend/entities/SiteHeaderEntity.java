package backend.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author armen
 */
public class SiteHeaderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String url;//ad code   
    private Date datePosted;

    public SiteHeaderEntity() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        final SiteHeaderEntity other = (SiteHeaderEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
            return false;
        }


        if (this.datePosted != other.datePosted && (this.datePosted == null || !this.datePosted.equals(other.datePosted))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SiteHeaderEntity{" + "id=" + id + ", title=" + title + ", url=" + url + '}';
    }
}
