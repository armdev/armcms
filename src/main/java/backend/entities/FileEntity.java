package backend.entities;

import java.io.Serializable;

/**
 *
 * @author armdev
 */
public class FileEntity implements Serializable
{

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Long parentId;//blog/news....
    private String title;
    private String mimetype;
    private Long filesize;
    private String filepath;
    private Integer status;
    private Integer type;
    protected byte[] content;

    public FileEntity()
    {
    }

    public FileEntity(Long id, Long userId, Long parentId, String title, String mimetype, Long filesize, String filepath)
    {
        this.id = id;
        this.userId = userId;
        this.parentId = parentId;
        this.title = title;
        this.mimetype = mimetype;
        this.filesize = filesize;
        this.filepath = filepath;
    }

    public FileEntity(Long id, Long userId, String title, String mimetype, Long filesize, String filepath)
    {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.mimetype = mimetype;
        this.filesize = filesize;
        this.filepath = filepath;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public byte[] getContent()
    {
        return content;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    public FileEntity(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getMimetype()
    {
        return mimetype;
    }

    public void setMimetype(String mimetype)
    {
        this.mimetype = mimetype;
    }

    public Long getFilesize()
    {
        return filesize;
    }

    public void setFilesize(Long filesize)
    {
        this.filesize = filesize;
    }

    public String getFilepath()
    {
        return filepath;
    }

    public void setFilepath(String filepath)
    {
        this.filepath = filepath;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FileEntity))
        {
            return false;
        }
        FileEntity other = (FileEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "FileEntity{" + "id=" + id + ", userId=" + userId + ", parentId=" + parentId + ", title=" + title + ", mimetype=" + mimetype + ", filesize=" + filesize + '}';
    }
}
