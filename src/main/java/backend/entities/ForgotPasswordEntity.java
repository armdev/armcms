package backend.entities;

import java.util.Date;

/**
 *
 * @author armen
 */
public class ForgotPasswordEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Date createDatetime;
    private String key;

    public ForgotPasswordEntity() {
    }

    public ForgotPasswordEntity(Long userId, Date createDatetime, String key) {
        this.userId = userId;
        this.createDatetime = createDatetime;
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        hash = 79 * hash + (this.key != null ? this.key.hashCode() : 0);
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
        final ForgotPasswordEntity other = (ForgotPasswordEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.userId != other.userId && (this.userId == null || !this.userId.equals(other.userId))) {
            return false;
        }
        if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ForgotPasswordEntity{" + "id=" + id + ", userId=" + userId + ", createDatetime=" + createDatetime + ", key=" + key + '}';
    }
}
