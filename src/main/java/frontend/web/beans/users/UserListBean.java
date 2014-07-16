package frontend.web.beans.users;

import backend.entities.ArticleEntity;
import backend.entities.UserEntity;
import backend.facades.UserManagerFacade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "UserListBean")
@ViewScoped
public class UserListBean implements Serializable {

    @ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userManagerFacade;
    private FacesContext context = null;
    private ExternalContext ex = null;
    private UserEntity userEntity;
    private List<UserEntity> userList;
    private DbListDataModel listDataModel;
    private Integer pageSize = 10;

    public UserListBean() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        listDataModel = new DbListDataModel();
        listDataModel.setPageSize(pageSize);
    }
    
    
    public class DbListDataModel extends LazyDataModel<UserEntity> {
        public long total = 0;
        @Override
        public List<UserEntity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
            List<UserEntity> list = null;
            try {
                int count = (first + pageSize);              
                total = userManagerFacade.getUsersCount();
                list = userManagerFacade.getUserList(first, count);
               
            } catch (Exception es) {
                es.printStackTrace();
            }
            return list;
        }

        @Override
        public UserEntity getRowData(String rowKey) {
            return null;
        }

        @Override
        public Object getRowKey(UserEntity object) {
            return object.getId();
        }

        @Override
        public int getRowCount() {
            return (int) total;
        }
    }

    @PostConstruct
    public void init() {
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public DbListDataModel getListDataModel() {
        return listDataModel;
    }

    public List<UserEntity> getUserList() {
        if (userList == null) {
            userList = userManagerFacade.getUserList(null, null);
        }
        return userList;
    }

    public void setUserManagerFacade(UserManagerFacade userManagerFacade) {
        this.userManagerFacade = userManagerFacade;
    }
}
