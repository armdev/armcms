package backend.facades;

import frontend.web.utils.FacesUtil;
import frontend.web.utils.Util;
import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class ForgotPassword extends HttpServlet {

    static String sessionmap;
    private String key;
    private String uid;
    boolean check = false;
    private UserManagerFacade userManager = new UserManagerFacade();

    @Override
    public void init() throws ServletException {
        super.init();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doChangePassword(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    private void doChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FacesContext context = FacesUtil.getFacesContext(request, response);
        PrintWriter out = response.getWriter();
        key = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("key");
        uid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("uid");
        Long userId = Util.longValue(uid);
        try {
            check = userManager.checkForgotPasswordKey(userId, key);
            if (check) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", userId);
                response.sendRedirect("changepassword.jsf");
                return;
            } else {
                check = false;
                response.sendRedirect("fail.jsf");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserManager(UserManagerFacade userManager) {
        this.userManager = userManager;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
