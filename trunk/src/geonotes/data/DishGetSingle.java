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
        return aPm.getObjectById(Dish.class, aDishId);
    }    
}
