package geonotes.data;

import java.util.Date;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.utils.DisplayUtils;
import geonotes.utils.RequestUtils;

/**
 * Update a dish.
 *
 * @author Brian Spiegel
 */
public class DishUpdate {

    /**
     * Update a dish.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long dishId=(Long)aRequest.getAttribute("dishId");
        
        // Fields
        String note=(String)aRequest.getAttribute("note");
        String user=(String)aRequest.getAttribute("user");
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
                        
            Dish dish=DishGetSingle.getDish(aRequest,pm,dishId.longValue());
            
            /*
            if (dish.reviewCount>0) {
                RequestUtils.addEditUsingKey(aRequest,"dishesWithReviewsCantBeUpdatedEditMessage");
                return;
            }
            */
            
            if (dish!=null){
            
                if (note!=null) {
                    dish.setNote(note);
                }
                    
                dish.setLastUpdateTime(new Date());
                dish.setUser(user);
            }
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e);
            e.printStackTrace();
            RequestUtils.addEditUsingKey(aRequest,"requestNotProcessedEditMsssage");
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
