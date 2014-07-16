package backend.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Armen
 */
public class ActivationKeyEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Date createDatetime;
    private String key;

    public ActivationKeyEntity() {
    }

    public ActivationKeyEntity(Long id) {
        this.id = id;
    }

    public ActivationKeyEntity(Long userId, Date createDatetime, String key) {
        this.userId = userId;
        this.createDatetime = createDatetime;
        this.key = key;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivationKeyEntity)) {
            return false;
        }
        ActivationKeyEntity other = (ActivationKeyEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ActivationKeyEntity{" + "id=" + id + ", userId=" + userId + ", createDatetime=" + createDatetime + ", key=" + key + '}';
    }
}
