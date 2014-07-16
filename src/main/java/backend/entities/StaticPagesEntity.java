package backend.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
public class StaticPagesEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String content;
    private String permalink;
    private Integer sortOrder;
    private Integer status;
    private Date datePosted;   
    

    public StaticPagesEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 79 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 79 * hash + (this.sortOrder != null ? this.sortOrder.hashCode() : 0);
        hash = 79 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 79 * hash + (this.datePosted != null ? this.datePosted.hashCode() : 0);
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
        final StaticPagesEntity other = (StaticPagesEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
            return false;
        }
        if (this.sortOrder != other.sortOrder && (this.sortOrder == null || !this.sortOrder.equals(other.sortOrder))) {
            return false;
        }
        if (this.status != other.status && (this.status == null || !this.status.equals(other.status))) {
            return false;
        }
        if (this.datePosted != other.datePosted && (this.datePosted == null || !this.datePosted.equals(other.datePosted))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StaticPagesEntity{" + "id=" + id + ", title=" + title + ", content=" + content + ", sortOrder=" + sortOrder + ", status=" + status + ", datePosted=" + datePosted + '}';
    }
}
