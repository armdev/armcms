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
 * @author Armen Arzumanyan
 */
public class ActivateAccount extends HttpServlet {

    private String key;
    private String uid;
    boolean check = false;
    //@ManagedProperty("#{UserManagerFacade}")
    private UserManagerFacade userManager = new UserManagerFacade();

    @Override
    public void init() throws ServletException {
        super.init();


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doActivation(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    private void doActivation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FacesContext context = FacesUtil.getFacesContext(request, response);
        PrintWriter out = response.getWriter();
        key = (String) context.getExternalContext().getRequestParameterMap().get("key");
        uid = (String) context.getExternalContext().getRequestParameterMap().get("uid");
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Account Activation</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<CENTER>");
        // out.println("Key " + key);
        // out.println("Uid" + uid);
        out.println("</CENTER>");
        out.println("</BODY>");
        out.println("</HTML>");

        Long userId = Util.longValue(uid);

        try {            
            check = userManager.checkActivationKey(userId, key);
            if (check) {
                userManager.activateAccount(userId);
                response.sendRedirect("activation.jsf");
            } else {
                check = false;
                // manager.release();
                response.sendRedirect("fail.jsf");
                //   log.debug("Wrong key and uid");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setUserManager(UserManagerFacade userManager) {
        this.userManager = userManager;
    }
}
