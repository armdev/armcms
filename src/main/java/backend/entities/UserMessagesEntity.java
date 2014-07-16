package backend.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author armen
 */
public class UserMessagesEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long senderId;
    private Long recipientId;
    private String subject;
    private String body;
    private Date sentDatetime;
    private Integer type;
    private Integer isRead;
    private Integer location;
    private UserEntity userEntity;

    public UserMessagesEntity() {
    }

    public UserMessagesEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    
    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getSentDatetime() {
        return sentDatetime;
    }

    public void setSentDatetime(Date sentDatetime) {
        this.sentDatetime = sentDatetime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
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
        if (!(object instanceof UserMessagesEntity)) {
            return false;
        }
        UserMessagesEntity other = (UserMessagesEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MessagesEntity{" + "id=" + id + ", senderId=" + senderId + ", recipientId=" + recipientId + ", subject=" + subject + ", body=" + body + ", sentDatetime=" + sentDatetime + ", type=" + type + ", isRead=" + isRead + ", location=" + location + '}';
    }
}
