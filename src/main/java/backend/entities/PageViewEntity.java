package backend.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
public class PageViewEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String ip;
    private Long newsId;        
    private Date datePosted;

    public PageViewEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
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
        hash = 43 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 43 * hash + (this.ip != null ? this.ip.hashCode() : 0);
        hash = 43 * hash + (this.newsId != null ? this.newsId.hashCode() : 0);
        hash = 43 * hash + (this.datePosted != null ? this.datePosted.hashCode() : 0);
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
        final PageViewEntity other = (PageViewEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.ip == null) ? (other.ip != null) : !this.ip.equals(other.ip)) {
            return false;
        }
        if (this.newsId != other.newsId && (this.newsId == null || !this.newsId.equals(other.newsId))) {
            return false;
        }
        if (this.datePosted != other.datePosted && (this.datePosted == null || !this.datePosted.equals(other.datePosted))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PageViewEntity{" + "id=" + id + ", ip=" + ip + ", newsId=" + newsId + ", datePosted=" + datePosted + '}';
    }

   
}
