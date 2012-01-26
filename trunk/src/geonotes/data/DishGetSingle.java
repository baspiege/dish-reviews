package geonotes.data;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.utils.RequestUtils;

/**
 * Get dish.
 *
 * @author Brian Spiegel
 */
public class DishGetSingle {

    /**
     * Get dish.
     *
     * @param aRequest The request
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        
        // Get Id.
        Long dishId=(Long)aRequest.getAttribute("dishId");
        
        try {
            pm=PMF.get().getPersistenceManager();

            Dish dish=DishGetSingle.getDish(aRequest,pm,dishId.longValue());

            // Set into request
            aRequest.setAttribute(RequestUtils.DISH, dish);

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
    
    /**
     * Get a dish.
     *
     * @param aRequest The request
     * @param aPm PersistenceManager
     * @param aDishId dish Id
     * @return a dish null if not found
     *
     * @since 1.0
     */
    public static Dish getDish(HttpServletRequest aRequest, PersistenceManager aPm, long aDishId) {
        Dish dish=null;

        Query query=null;
        try {
            // Get appts.
            query = aPm.newQuery(Dish.class); 
            query.setFilter("(key == dishIdParam)"); 
            query.declareParameters("long dishIdParam");
            query.setRange(0,1);

            List<Dish> results = (List<Dish>) query.execute(aDishId); 

            if (!results.isEmpty()) {
                dish=(Dish)results.get(0);
            }
        } finally {
            if (query!=null) {   
                query.closeAll(); 
            }
        }

        return dish;
    }    
}
