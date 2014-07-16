package backend.entities;

import java.io.Serializable;

/**
 *
 * @author armen
 */

public class PostVotingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;   
    private Long userId;
    private Long messageId;

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

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 47 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        hash = 47 * hash + (this.messageId != null ? this.messageId.hashCode() : 0);
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
        final PostVotingEntity other = (PostVotingEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.userId != other.userId && (this.userId == null || !this.userId.equals(other.userId))) {
            return false;
        }
        if (this.messageId != other.messageId && (this.messageId == null || !this.messageId.equals(other.messageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PostVotingEntity{" + "id=" + id + ", userId=" + userId + ", messageId=" + messageId + '}';
    }
    
    
   

   
    
}
