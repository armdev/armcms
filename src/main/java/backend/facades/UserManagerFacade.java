package backend.facades;

import backend.connections.DbInitBean;
import backend.entities.ActivationKeyEntity;
import backend.entities.ForgotPasswordEntity;
import backend.entities.UserEntity;
import backend.types.StatusTypes;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import frontend.web.utils.Util;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author armen.arzumanyan@gmail.com
 */
@ManagedBean(name = "UserManagerFacade")
@ApplicationScoped
public class UserManagerFacade extends DbInitBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{MailNotificationService}")
    private MailNotificationService mailNotificationService;
//    @ManagedProperty("#{DbInitBean}")
//    private DbInitBean initBean;

    public UserManagerFacade() {
    }

    public void registerNewUser(UserEntity userEntity) {
        Date curDate = new Date(System.currentTimeMillis());
        try {
            BasicDBObject document = new BasicDBObject();
            userEntity.setId(Util.longValue(getNextId(getDatabase(), "userSeqGen")));
            document.put("id", userEntity.getId());
            document.put("email", userEntity.getEmail());
            document.put("firstname", userEntity.getFirstname());
            document.put("lastname", userEntity.getLastname());
            document.put("passwd", hashPassword(userEntity.getPasswd().trim()));
            document.put("userRole", StatusTypes.REGULAR_USER);
            document.put("status", StatusTypes.USER_DISABLED);
            document.put("registeredDate", new Date(System.currentTimeMillis()));

            if (userEntity.isAcceptSubscr()) {
                document.put("subscribe", StatusTypes.ACCEPT_SUBSCRB);
            } else {
                document.put("subscribe", StatusTypes.DECLINE_SUBSCRB);
            }
            getUserCollection().insert(document, WriteConcern.SAFE);
            String activationKey = hashPassword(userEntity.getEmail() + String.valueOf(System.currentTimeMillis()));
            BasicDBObject document1 = new BasicDBObject();
            Long id = Util.longValue(getNextId(getDatabase(), "activateSeqGen"));
            document1.put("id", id);
            document1.put("userId", userEntity.getId());
            document1.put("createDatetime", curDate);
            document1.put("key", activationKey);
            getActivationCollection().insert(document1, WriteConcern.SAFE);
            mailNotificationService.sendActivationLink(userEntity.getId(), userEntity.getEmail(), activationKey);
            mailNotificationService = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserEntity userLogin(String email, String passwd) {
        if (email == null || passwd == null) {
            return null;
        }
        UserEntity userEntity = null;
        BasicDBObject query = new BasicDBObject();
        query.put("email", email.trim());
        query.put("status", StatusTypes.USER_ENABLED);
        query.put("passwd", hashPassword(passwd.trim()));
        DBCursor cursor = getUserCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                userEntity = new UserEntity();
                userEntity.setId((Long) document.get("id"));
                userEntity.setEmail((String) document.get("email"));
                userEntity.setFirstname((String) document.get("firstname"));
                userEntity.setPasswd((String) document.get("passwd"));
                userEntity.setLastname((String) document.get("lastname"));
                userEntity.setRegisteredDate((Date) document.get("registeredDate"));
                userEntity.setUserRole((Integer) document.get("userRole"));
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
        return userEntity;
    }

    public Integer getUsersCount() {
        Integer listCount = 0;
        try {
            BasicDBObject query = new BasicDBObject();
            DBCursor cursor = getUserCollection().find(query);
            listCount = cursor.count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCount;
    }

    public boolean updateImageId(Long userId, String imageId) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.append("$set", new BasicDBObject().append("imageId", imageId));
            getUserCollection().update(new BasicDBObject().append("id", userId), document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<UserEntity> getUserList(Integer skip, Integer limit) {
        List<UserEntity> userList = new ArrayList<UserEntity>();
        String sort = "registeredDate";
        String order = "desc";
        DBObject sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = getUserCollection().find(query).sort(sortCriteria).skip(skip).limit(limit);
        try {
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                UserEntity userEntity = new UserEntity();
                userEntity.setId((Long) document.get("id"));
                userEntity.setEmail((String) document.get("email"));
                userEntity.setFirstname((String) document.get("firstname"));
                userEntity.setPasswd((String) document.get("passwd"));
                userEntity.setLastname((String) document.get("lastname"));
                userEntity.setStatus((Integer) document.get("status"));
                userEntity.setRegisteredDate((Date) document.get("registeredDate"));
                userEntity.setUserRole((Integer) document.get("userRole"));
                userList.add(userEntity);
            }
        } finally {
            cursor.close();
        }
        return userList;
    }

    public void updatePassword(Long id, String password) {
        BasicDBObject document = new BasicDBObject();
        document.append("$set", new BasicDBObject()
                .append("passwd", hashPassword(password)));
        getUserCollection().update(new BasicDBObject().append("id", id), document);
    }

    public UserEntity getUserByEmail(String email) {
        UserEntity userEntity = null;
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        DBCursor cursor = getUserCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                userEntity = new UserEntity();
                userEntity.setId((Long) document.get("id"));
                userEntity.setEmail((String) document.get("email"));
                userEntity.setFirstname((String) document.get("firstname"));
                userEntity.setPasswd((String) document.get("passwd"));
                userEntity.setLastname((String) document.get("lastname"));
                userEntity.setStatus((Integer) document.get("status"));
                userEntity.setRegisteredDate((Date) document.get("registeredDate"));
                userEntity.setUserRole((Integer) document.get("userRole"));
            }
        } finally {
            cursor.close();
        }
        return userEntity;
    }

    public UserEntity getUserById(Long id) {
        UserEntity userEntity = null;
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBCursor cursor = getUserCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                userEntity = new UserEntity();
                userEntity.setId((Long) document.get("id"));
                userEntity.setEmail((String) document.get("email"));
                userEntity.setFirstname((String) document.get("firstname"));
                userEntity.setPasswd((String) document.get("passwd"));
                userEntity.setLastname((String) document.get("lastname"));
                userEntity.setStatus((Integer) document.get("status"));
                userEntity.setRegisteredDate((Date) document.get("registeredDate"));
                userEntity.setUserRole((Integer) document.get("userRole"));
                userEntity.setSubscribe((Integer) document.get("subscribe"));
                if (userEntity.getSubscribe() != null && userEntity.getSubscribe().equals(StatusTypes.ACCEPT_SUBSCRB)) {
                    userEntity.setAcceptSubscr(true);
                } else {
                    userEntity.setAcceptSubscr(false);
                }
            }
        } finally {
            cursor.close();
        }
        return userEntity;
    }

    public void updateUserProfile(UserEntity userEntity) {
        BasicDBObject document = new BasicDBObject();

        if (userEntity.getPasswd() != null) {
            document.append("$set", new BasicDBObject()
                    .append("email", userEntity.getEmail())
                    .append("userRole", userEntity.getUserRole())
                    .append("subscribe", userEntity.isAcceptSubscr() ? StatusTypes.ACCEPT_SUBSCRB : StatusTypes.DECLINE_SUBSCRB)
                    .append("firstname", userEntity.getFirstname())
                    .append("lastname", userEntity.getLastname())
                    .append("passwd", hashPassword(userEntity.getPasswd() != null ? userEntity.getPasswd() : null))
                    .append("status", userEntity.getStatus()));
        } else {
            document.append("$set", new BasicDBObject()
                    .append("email", userEntity.getEmail())
                    .append("subscribe", userEntity.isAcceptSubscr() ? StatusTypes.ACCEPT_SUBSCRB : StatusTypes.DECLINE_SUBSCRB)
                    .append("userRole", userEntity.getUserRole())
                    .append("firstname", userEntity.getFirstname())
                    .append("lastname", userEntity.getLastname())
                    .append("status", userEntity.getStatus()));
        }

        getUserCollection().update(new BasicDBObject().append("id", userEntity.getId()), document);
    }

    public Long getUserIdByEmail(String email) {
        Long id = 0L;
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        DBCursor cursor = getUserCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                id = (Long) document.get("id");
            }
        } finally {
            cursor.close();
        }
        return id;
    }

    public boolean checkEmail(String email) {
        boolean retValue = false;
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        DBCursor cursor = getUserCollection().find(query);
        try {
            if (cursor == null) {
                return false;
            }
            if (cursor != null && cursor.count() > 0) {
                retValue = true;
            } else {
                return false;
            }
        } finally {
            cursor.close();
        }
        return retValue;
    }

    public boolean checkUsername(String username) {
        boolean retValue = false;
        BasicDBObject query = new BasicDBObject();
        query.put("username", username);
        DBCursor cursor = getUserCollection().find(query);
        try {
            if (cursor != null && cursor.count() > 0) {
                retValue = true;
            }
        } finally {
            cursor.close();
        }
        return retValue;
    }

    public boolean checkActivationKey(Long userId, String key) {
        boolean retValue = false;
        BasicDBObject query = new BasicDBObject();
        //  System.out.println("KEY " + key);
        // System.out.println("userId " + userId);
        //System.out.println("initBean.getActivationCollection() " + initBean.getActivationCollection());
        query.put("userId", userId);
        query.put("key", key);

        DBCursor cursor = null;
        try {
            cursor = getActivationCollection().find(query);
            if (cursor != null && cursor.count() > 0) {
                retValue = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return retValue;
    }

    public void activateAccount(Long id) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.append("$set", new BasicDBObject().append("status", StatusTypes.USER_ENABLED));
            getUserCollection().update(new BasicDBObject().append("id", id), document);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.removeActivationKey(id);
    }

    public void removeActivationKey(Long userId) {
        BasicDBObject document = new BasicDBObject();
        document.put("userId", userId);
        getActivationCollection().remove(document);
    }

    public boolean checkForgotPasswordKey(Long userId, String key) {
        boolean retValue = false;
        BasicDBObject query = new BasicDBObject();
        query.put("userId", userId);
        query.put("key", key);
        DBCursor cursor = getForgotCollection().find(query);
        try {
            if (cursor != null && cursor.count() > 0) {
                retValue = true;
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return retValue;
    }

    public void sendForgotPasswordLink(String email) {
        Date curDate = new Date(System.currentTimeMillis());
        UserEntity user = this.getUserByEmail(email);
        Long userId = user.getId();

        ForgotPasswordEntity record;
        if ((record = getForgotPasswordKey(userId)) != null) {
            BasicDBObject document = new BasicDBObject();
            document.put("userId", userId);
            getForgotCollection().remove(document);
        }

        String keyString = hashPassword(email + String.valueOf(System.currentTimeMillis()));
        BasicDBObject document = new BasicDBObject();
        Long id = Util.longValue(getNextId(getDatabase(), "forgotSeqGen"));
        document.put("id", id);
        document.put("userId", userId);
        document.put("createDatetime", curDate);
        document.put("key", keyString);
        getForgotCollection().save(document, WriteConcern.SAFE);

        mailNotificationService.sendForgotPasswordLink(userId, email, keyString);
        mailNotificationService = null;
    }

    private ForgotPasswordEntity getForgotPasswordKey(Long userId) {
        ForgotPasswordEntity retValue = null;
        BasicDBObject query = new BasicDBObject();
        query.put("userId", userId);
        DBCursor cursor = getForgotCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                retValue = new ForgotPasswordEntity();
                retValue.setId((Long) document.get("id"));
                retValue.setUserId((Long) document.get("userId"));
                retValue.setCreateDatetime((Date) document.get("createDatetime"));
                retValue.setKey((String) document.get("key"));

            }
        } finally {
            cursor.close();
        }
        return retValue;

    }

    public void removeForgotPasswordKey(Long userId) {
        BasicDBObject document = new BasicDBObject();
        document.put("userId", userId);
        getForgotCollection().remove(document);

    }

    protected ActivationKeyEntity getActivationRecordByUser(Long userId) {
        ActivationKeyEntity retValue = null;
        BasicDBObject query = new BasicDBObject();
        query.put("userId", userId);
        DBCursor cursor = getActivationCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                retValue = new ActivationKeyEntity();
                retValue.setId((Long) document.get("id"));
                retValue.setUserId((Long) document.get("userId"));
                retValue.setCreateDatetime((Date) document.get("createDatetime"));
                retValue.setKey((String) document.get("key"));

            }
        } finally {
            cursor.close();
        }
        return retValue;
    }

    protected ForgotPasswordEntity getForgotPasswordRecordByUser(Long userId) {
        ForgotPasswordEntity retValue = null;
        BasicDBObject query = new BasicDBObject();
        query.put("userId", userId);
        DBCursor cursor = getForgotCollection().find(query);
        try {
            if (cursor.count() > 0) {
                DBObject document = cursor.next();
                retValue = new ForgotPasswordEntity();
                retValue.setId((Long) document.get("id"));
                retValue.setUserId((Long) document.get("userId"));
                retValue.setCreateDatetime((Date) document.get("createDatetime"));
                retValue.setKey((String) document.get("key"));

            }
        } finally {
            cursor.close();
        }
        return retValue;
    }

    protected String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            byte[] bs;
            bs = messageDigest.digest(password.getBytes());
            for (int i = 0; i < bs.length; i++) {
                String hexVal = Integer.toHexString(0xFF & bs[i]);
                if (hexVal.length() == 1) {
                    sb.append("0");
                }
                sb.append(hexVal);
            }
        } catch (NoSuchAlgorithmException ex) {
            //log.debug(ex);
        }
        return sb.toString();
    }

    public void setMailNotificationService(MailNotificationService mailNotificationService) {
        this.mailNotificationService = mailNotificationService;
    }

//    public void setInitBean(DbInitBean initBean) {
//        this.initBean = initBean;
//    }
    protected static String getNextId(DB db, String seq_name) {
        String sequence_collection = "seq"; // the name of the sequence collection
        String sequence_field = "seq"; // the name of the field which holds the sequence


        DBCollection seq = db.getCollection(sequence_collection); // get the collection (this will create it if needed)               

        if (seq == null) {
            seq = db.createCollection(sequence_collection, null);
        }

        // this object represents your "query", its analogous to a WHERE clause in SQL
        DBObject query = new BasicDBObject();
        query.put("id", seq_name); // where id = the input sequence name

        // this object represents the "update" or the SET blah=blah in SQL
        DBObject change = new BasicDBObject(sequence_field, 1);
        DBObject update = new BasicDBObject("$inc", change); // the $inc here is a mongodb command for increment

        // Atomically updates the sequence field and returns the value for you
        DBObject res = seq.findAndModify(query, new BasicDBObject(), new BasicDBObject(), false, update, true, true);
        return res.get(sequence_field).toString();
    }
}
