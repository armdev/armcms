package frontend.web.beans.users;

import backend.entities.FileEntity;
import backend.entities.UserEntity;
import backend.facades.FileManagerFacade;
import backend.facades.UserManagerFacade;
import backend.types.StatusTypes;
import frontend.web.utils.UploadUtil;
import frontend.web.utils.Util;
import java.io.InputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.myfaces.custom.fileupload.UploadedFile;
/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "PhotoUploadBean")
@ViewScoped
public class PhotoUploadBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userFacade;
    @ManagedProperty("#{UserProfileBean}")
    private UserUpdateBean userProfileBean;
    private FileManagerFacade fileManager;
    private UserEntity userDetails;
    private UploadedFile upFile = null;
    private String imageId;
    private boolean fileTooBig = false;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private Long userId;

    public PhotoUploadBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        fileManager = new FileManagerFacade();
    }

    @PostConstruct
    public void init() {

        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("userId")) {
            userId = Util.longValue(ex.getRequestParameterMap().get("userId"));
        }

    }

    public String save() {
        FileEntity file = new FileEntity();
        try {
            long size = 0;
            String uploadedFileName = "";
            if (getUpFile() != null && getUpFile().getName() != null) {
                uploadedFileName = UploadUtil.trimFilePath(getUpFile().getName());
            }
            if (getUpFile() != null) {
                size = getUpFile().getSize();
                long maxSize = 5 * 1000000;
                if (size > maxSize) {
                    setFileTooBig(true);
                    return null;
                }
                String content = getUpFile().getContentType();
                InputStream stream = getUpFile().getInputStream();
                byte[] contentBytes = new byte[(int) size];
                stream.read(contentBytes);
                file.setMimetype(content);
                file.setFilesize(size);
                file.setTitle(uploadedFileName);
                file.setContent(contentBytes);
                file.setUserId(userId);
                file.setType(StatusTypes.POST_PHOTO);
                if (!content.equalsIgnoreCase("image/jpeg")
                        && !content.equalsIgnoreCase("image/pjpeg")
                        && !content.equalsIgnoreCase("image/jpg")
                        && !content.equalsIgnoreCase("image/gif")
                        && !content.equalsIgnoreCase("image/x-png")
                        && !content.equalsIgnoreCase("image/png")) {
                    try {
                        System.out.println("File upload error");
                        setFileTooBig(true);
                        return null;
                    } catch (Exception e) {
                       // e.printStackTrace();
                    }
                }
                imageId = fileManager.addFile(file);
                //  System.out.println("Image id " +imageId);
            }
        } catch (Exception ioe) {
            //ioe.printStackTrace();
        }
        try {
            userFacade.updateImageId(userId, imageId);
            // userProfileBean.init();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return "ulist?faces-redirect=true";
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isFileTooBig() {
        return fileTooBig;
    }

    public void setFileTooBig(boolean fileTooBig) {
        this.fileTooBig = fileTooBig;
    }

    public UploadedFile getUpFile() {
        return upFile;
    }

    public void setUpFile(UploadedFile upFile) {
        this.upFile = upFile;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }

    public void setUserFacade(UserManagerFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void setFileManager(FileManagerFacade fileManager) {
        this.fileManager = fileManager;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setUserProfileBean(UserUpdateBean userProfileBean) {
        this.userProfileBean = userProfileBean;
    }
}
