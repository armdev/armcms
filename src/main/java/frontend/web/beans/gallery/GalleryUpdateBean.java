package frontend.web.beans.gallery;

import backend.entities.FileEntity;
import backend.entities.GalleryEntity;
import backend.facades.FileManagerFacade;
import backend.facades.GalleryManagerFacade;
import backend.types.StatusTypes;
import frontend.web.utils.UploadUtil;
import frontend.web.utils.Util;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.myfaces.custom.fileupload.UploadedFile;

@ManagedBean(name = "GalleryUpdateBean")
@ViewScoped
public class GalleryUpdateBean implements Serializable {

    @ManagedProperty("#{GalleryManagerFacade}")
    private GalleryManagerFacade galleryManagerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private GalleryEntity galleryEntity;
    private Long id;
    @ManagedProperty("#{FileManagerFacade}")
    private FileManagerFacade fileManager;
    private String imageId;
    private List<String> images = new ArrayList<String>();
    private UploadedFile upFile = null;

    public GalleryUpdateBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        galleryEntity = new GalleryEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            id = Util.longValue(ex.getRequestParameterMap().get("next"));
        }
        galleryEntity = galleryManagerFacade.getGalleryById(id);
    }

    public void update() {
        FileEntity file = new FileEntity();
        try {
            long size = 0;
            String uploadedFileName = "";
            if (getUpFile() != null && getUpFile().getName() != null) {
                uploadedFileName = UploadUtil.trimFilePath(getUpFile().getName());
            }
            if (getUpFile() != null) {
                size = getUpFile().getSize();
                long maxSize = 5 * 2000000;
                if (size > maxSize) {
                    //setFileTooBig(true);
                    return;
                }
                String content = getUpFile().getContentType();
                InputStream stream = getUpFile().getInputStream();
                byte[] contentBytes = new byte[(int) size];
                stream.read(contentBytes);
                file.setMimetype(content);
                file.setFilesize(size);
                file.setTitle(uploadedFileName);
                file.setContent(contentBytes);
                file.setType(StatusTypes.POST_PHOTO);
                if (!content.equalsIgnoreCase("image/jpeg")
                        && !content.equalsIgnoreCase("image/pjpeg")
                        && !content.equalsIgnoreCase("image/jpg")
                        && !content.equalsIgnoreCase("image/gif")
                        && !content.equalsIgnoreCase("image/x-png")
                        && !content.equalsIgnoreCase("image/png")) {
                    try {
                        System.out.println("File upload error");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                imageId = fileManager.addFile(file);//id is new file name as String              
            }

            galleryEntity.setId(id);
            List<String> list = galleryEntity.getImages();
            if(imageId != null){
            list.add(imageId);
            }
            galleryEntity.setImages(list);
            boolean check = galleryManagerFacade.updateGallery(galleryEntity);


            FacesMessage msg1 = new FacesMessage(bundle.getString("success"), bundle.getString("success"));
            FacesContext.getCurrentInstance().addMessage(null, msg1);

        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

    }

    public UploadedFile getUpFile() {
        return upFile;
    }

    public void setUpFile(UploadedFile upFile) {
        this.upFile = upFile;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setFileManager(FileManagerFacade fileManager) {
        this.fileManager = fileManager;
    }

    public void setGalleryManagerFacade(GalleryManagerFacade galleryManagerFacade) {
        this.galleryManagerFacade = galleryManagerFacade;
    }

    public GalleryEntity getGalleryEntity() {
        return galleryEntity;
    }

    public void setGalleryEntity(GalleryEntity galleryEntity) {
        this.galleryEntity = galleryEntity;
    }
}
