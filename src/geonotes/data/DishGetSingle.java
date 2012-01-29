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
    public Dish execute(HttpServletRequest aRequest, Long aDishId) {
        PersistenceManager pm=null;
        Dish dish=null;
        try {
            pm=PMF.get().getPersistenceManager();
            dish=DishGetSingle.getDish(aRequest,pm,aDishId);
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e);
            e.printStackTrace();
            RequestUtils.addEditUsingKey(aRequest,"requestNotProcessedEditMsssage");
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return dish;
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
