package geonotes.data;

import geonotes.data.model.Dish;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Get dish.
 *
 * @author Brian Spiegel
 */
public class DishGetSingle {

    /**
     * Get dish.
     *
     * @param aDishId dish Id
     * @return a dish
     * @since 1.0
     */
    public Dish execute(Long aDishId) {
        PersistenceManager pm=null;
        Dish dish=null;
        try {
            pm=PMF.get().getPersistenceManager();
            dish=DishGetSingle.getDish(pm,aDishId);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
     * @param aPm PersistenceManager
     * @param aDishId dish Id
     * @return a dish null if not found
     *
     * @since 1.0
     */
    public static Dish getDish(PersistenceManager aPm, long aDishId) {
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
