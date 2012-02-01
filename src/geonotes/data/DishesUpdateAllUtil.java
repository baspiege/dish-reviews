package geonotes.data;

import geonotes.data.model.Dish;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Update all dishes.
 *
 * @author Brian Spiegel
 */
public class DishesUpdateAllUtil {

    /**
     * Update all dishes.
     *
     * @since 1.0
     */
    public void execute() {
        PersistenceManager pm=null;
        List<Dish> results=null;

        pm=PMF.get().getPersistenceManager();

        Query query=null;
        try {
            query = pm.newQuery(Dish.class);
            results = (List<Dish>) query.execute();               
            // Bug workaround.  Get size actually triggers the underlying database call.
            results.size();
        } finally {
            if (query!=null) {
                query.closeAll();
            }
        }
        
        try {            
            for (Dish dish:results) {
                //dish.setYesVote(dish.vote);
                dish.setYesVote(0l);                
            }            
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
