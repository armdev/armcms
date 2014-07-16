package backend.entities;

import java.io.Serializable;

/**
 *
 * @author armen
 */
public class MailerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String host;
    private String socketPort;
    private String smtpPort;
    private String fromAddress;
    private String email;
    private String passwd;

    public MailerEntity() {
    }

    public MailerEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MailerEntity)) {
            return false;
        }
        MailerEntity other = (MailerEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MailerEntity{" + "id=" + id + ", host=" + host + ", socketPort=" + socketPort + ", smtpPort=" + smtpPort + ", fromAddress=" + fromAddress + '}';
    }
}
