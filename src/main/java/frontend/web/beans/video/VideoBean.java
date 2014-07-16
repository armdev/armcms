package frontend.web.beans.video;

import backend.entities.VideoEntity;
import backend.facades.VideoManagerFacade;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "VideoBean")
@ViewScoped
public class VideoBean implements Serializable {

    @ManagedProperty("#{VideoManagerFacade}")
    private VideoManagerFacade videoManagerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private VideoEntity videoEntity;
    private String url = null;
    private DbListDataModel listDataModel;
    private Integer pageSize = 10;

    public VideoBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        videoEntity = new VideoEntity();
        listDataModel = new DbListDataModel();
        listDataModel.setPageSize(pageSize);
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    public class DbListDataModel extends LazyDataModel<VideoEntity> {

        public long total = 0;

        @Override
        public List<VideoEntity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
            List<VideoEntity> list = null;
            try {
                int count = (first + pageSize);
                total = videoManagerFacade.getVideoCount();
                list = videoManagerFacade.getVideoList(first, count);

            } catch (Exception es) {
                es.printStackTrace();
            }
            return list;
        }

        @Override
        public VideoEntity getRowData(String rowKey) {
            return null;
        }

        @Override
        public Object getRowKey(VideoEntity object) {
            return object.getId();
        }

        @Override
        public int getRowCount() {
            return (int) total;
        }
    }

    public DbListDataModel getListDataModel() {
        return listDataModel;
    }

    public String show(String videoUrl) {
        url = videoUrl;
        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
  
    public String addVideo() {
        videoManagerFacade.addVideo(videoEntity);
        return "videolist";
    }

    public String remove(Long id) {
        videoManagerFacade.removeVideo(id);
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
}
