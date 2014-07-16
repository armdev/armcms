package backend.entities;

import java.io.Serializable;

/**
 *
 * @author armen
 */
public class ArticleAuthorsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String fullname;
    private String firstname;
    private String lastname;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 19 * hash + (this.fullname != null ? this.fullname.hashCode() : 0);
        hash = 19 * hash + (this.firstname != null ? this.firstname.hashCode() : 0);
        hash = 19 * hash + (this.lastname != null ? this.lastname.hashCode() : 0);
        hash = 19 * hash + (this.email != null ? this.email.hashCode() : 0);
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
        final ArticleAuthorsEntity other = (ArticleAuthorsEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.fullname == null) ? (other.fullname != null) : !this.fullname.equals(other.fullname)) {
            return false;
        }
        if ((this.firstname == null) ? (other.firstname != null) : !this.firstname.equals(other.firstname)) {
            return false;
        }
        if ((this.lastname == null) ? (other.lastname != null) : !this.lastname.equals(other.lastname)) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ArticleAuthorsEntity{" + "id=" + id + ", fullname=" + fullname + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email + '}';
    }
}
