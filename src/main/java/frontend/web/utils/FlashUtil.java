
package frontend.web.utils;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 *
 * @author Armen.Arzumanyan
 */
public class FlashUtil {    
   public static Flash flashScope (){
	return (FacesContext.getCurrentInstance().getExternalContext().getFlash());
   }
    
}
