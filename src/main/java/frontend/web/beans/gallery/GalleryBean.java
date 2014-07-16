package frontend.web.beans.gallery;

import backend.entities.GalleryEntity;
import backend.facades.GalleryManagerFacade;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "GalleryBean")
@ViewScoped
public class GalleryBean implements Serializable {

    @ManagedProperty("#{GalleryManagerFacade}")
    private GalleryManagerFacade galleryManagerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private ResourceBundle bundle;
    private GalleryEntity galleryEntity;
    private String url = null;
    private DbListDataModel listDataModel;
    private Integer pageSize = 10;

    public GalleryBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        galleryEntity = new GalleryEntity();
        listDataModel = new DbListDataModel();
        listDataModel.setPageSize(pageSize);
        bundle = ResourceBundle.getBundle("ConfirmationMessages");
    }

    public String show(String feedUrl) {
        url = feedUrl;
        return null;
    }

    public class DbListDataModel extends LazyDataModel<GalleryEntity> {

        public long total = 0;

        @Override
        public List<GalleryEntity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
            List<GalleryEntity> list = null;
            try {
                int count = (first + pageSize);
                total = galleryManagerFacade.getGalleryCount();
                list = galleryManagerFacade.getGalleryList(first, count);

            } catch (Exception es) {
                es.printStackTrace();
            }
            return list;
        }

        @Override
        public GalleryEntity getRowData(String rowKey) {
            return null;
        }

        @Override
        public Object getRowKey(GalleryEntity object) {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public List<GalleryEntity> getGalleryList() {
//        return galleryManagerFacade.getGalleryList();
//    }
    public String addGallery() {
        galleryManagerFacade.createNewGallery(galleryEntity);
        return "galleries";
    }

    public String remove(Long id) {
        galleryManagerFacade.removeGallery(id);
        return null;
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
