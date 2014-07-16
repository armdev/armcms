package frontend.web.listeners;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class ResponseTimeListener implements PhaseListener {

    //private Logger log = Logger.getLogger(getClass());
    public long startTime;

    public ResponseTimeListener() {
        
    }    
    
    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        ExternalContext ex = context.getExternalContext();
        String viewId = null;        
        
        if (context.getViewRoot() != null && context.getViewRoot().getViewId() != null) {
            viewId = context.getViewRoot().getViewId();
        }

        String ajaxParam = null;
        Map requestParams = ex.getRequestParameterMap();
        ajaxParam = (String) requestParams.get("AJAXREQUEST");   

        if (ajaxParam == null) {            
           
                if (event.getPhaseId() == PhaseId.RENDER_RESPONSE) {                    
                    long endTime = System.nanoTime();
                    long diffMs = (long)((endTime - startTime));
                    System.out.println("Page " + viewId + " Execution Time = " 
                            + TimeUnit.NANOSECONDS.toMillis(diffMs)
                             + " miliseconds");                  
                }            
        }
        //System.out.println("Executed Phase " + event.getPhaseId());
    }

    public void beforePhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RENDER_RESPONSE) {           
            startTime = System.nanoTime();           
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
