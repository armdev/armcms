package backend.entities;

import java.io.Serializable;

/**
 *
 * @author armen
 */
public class GeneralSettingsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String siteTitle="This is my site title ";
    private String tagLine = "In a few words, explain what this site is about.";
    private String siteAddress = "http://mysite.com";
    private String ownerEmail = "myemail@gmail.com";

    public GeneralSettingsEntity() {
    }

    public GeneralSettingsEntity(Long id) {
        this.id = id;
    }

    public String getSiteTitle() {
        return siteTitle;
    }

    public void setSiteTitle(String siteTitle) {
        this.siteTitle = siteTitle;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 43 * hash + (this.siteTitle != null ? this.siteTitle.hashCode() : 0);
        hash = 43 * hash + (this.tagLine != null ? this.tagLine.hashCode() : 0);
        hash = 43 * hash + (this.siteAddress != null ? this.siteAddress.hashCode() : 0);
        hash = 43 * hash + (this.ownerEmail != null ? this.ownerEmail.hashCode() : 0);
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
        final GeneralSettingsEntity other = (GeneralSettingsEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.siteTitle == null) ? (other.siteTitle != null) : !this.siteTitle.equals(other.siteTitle)) {
            return false;
        }
        if ((this.tagLine == null) ? (other.tagLine != null) : !this.tagLine.equals(other.tagLine)) {
            return false;
        }
        if ((this.siteAddress == null) ? (other.siteAddress != null) : !this.siteAddress.equals(other.siteAddress)) {
            return false;
        }
        if ((this.ownerEmail == null) ? (other.ownerEmail != null) : !this.ownerEmail.equals(other.ownerEmail)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GeneralSettingsEntity{" + "id=" + id + ", siteTitle=" + siteTitle + ", tagLine=" + tagLine + ", siteAddress=" + siteAddress + ", ownerEmail=" + ownerEmail + '}';
    }
}
