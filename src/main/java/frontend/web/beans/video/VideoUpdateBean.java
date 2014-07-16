package frontend.web.beans.video;

import backend.entities.VideoEntity;
import backend.facades.VideoManagerFacade;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "VideoUpdateBean")
@ViewScoped
public class VideoUpdateBean implements Serializable {

    @ManagedProperty("#{VideoManagerFacade}")
    private VideoManagerFacade videoManagerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private VideoEntity videoEntity;
    private Long videoId;

    public VideoUpdateBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        videoEntity = new VideoEntity();
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    @PostConstruct
    public void init() {
        if (ex != null && ex.getRequestParameterMap() != null && ex.getRequestParameterMap().containsKey("next")) {
            videoId = Util.longValue(ex.getRequestParameterMap().get("next"));
        }
        videoEntity = videoManagerFacade.getVideoById(videoId);
    }

    public String update() {
        videoEntity.setId(videoId);
        videoManagerFacade.updateVideo(videoEntity);
        FacesMessage msg = new FacesMessage(bundle.getString("videoupdated"), bundle.getString("videoupdated"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return null;
    }
  
    public void setVideoManagerFacade(VideoManagerFacade videoManagerFacade) {
        this.videoManagerFacade = videoManagerFacade;
    }

    public VideoEntity getVideoEntity() {
        return videoEntity;
    }

    public void setVideoEntity(VideoEntity videoEntity) {
        this.videoEntity = videoEntity;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

  
}
