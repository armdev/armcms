package frontend.web.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Armen Arzumanyan, 2009
 */
public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void ensureAddErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
                .get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName,
            Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static <T> List<T> arrayToList(T[] arr) {
        if (arr == null) {
            return new ArrayList<T>();
        }
        return Arrays.asList(arr);
    }

    public static <T> Set<T> arrayToSet(T[] arr) {
        if (arr == null) {
            return new HashSet<T>();
        }
        return new HashSet<T>(Arrays.asList(arr));
    }

    public static Object[] collectionToArray(Collection<?> c) {
        if (c == null) {
            return new Object[0];
        }
        return c.toArray();
    }

    public static <T> List<T> setToList(Set<T> set) {
        return new ArrayList<T>(set);
    }

    public static String getAsConvertedString(Object object, Converter converter) {
        return converter.getAsString(FacesContext.getCurrentInstance(), null, object);
    }

    public static String getCollectionAsString(Collection<?> collection) {
        if (collection == null || collection.size() == 0) {
            return "(No Items)";
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Object item : collection) {
            if (i > 0) {
                sb.append("<br />");
            }
            sb.append(item);
            i++;
        }
        return sb.toString();
    }

    public static String getHttpRequestParameter(Object key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
                .get(key);
    }

    public static Long getHttpRequestParameterLong(Object key) {
        String param = getHttpRequestParameter(key);
        return param != null ? Long.parseLong(param) : null;
    }

    public static Integer getHttpRequestParameterInt(Object key) {
        String param = getHttpRequestParameter(key);
        return param != null ? Integer.parseInt(param) : null;
    }

    public static HttpServletRequest getRequest() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (HttpServletRequest) context.getExternalContext().getRequest();
    }

    public static HttpServletResponse getResponse() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (HttpServletResponse) context.getExternalContext().getResponse();
    }

    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        return request.getSession();
    }

    public static void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }
}
