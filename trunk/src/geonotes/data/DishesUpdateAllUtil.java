package geonotes.data;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.utils.RequestUtils;

/**
 * Update all dishes.
 *
 * @author Brian Spiegel
 */
public class DishesUpdateAllUtil {

    /**
     * Update all dishes.
     *
     * @param aRequest The request
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        List<Dish> results=null;
        try {
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
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e);
            e.printStackTrace();
            RequestUtils.addEditUsingKey(aRequest,"requestNotProcessedEditMsssage");
        }
        
        try {            
            for (Dish dish:results) {
                //dish.setYesVote(dish.vote);
                dish.setYesVote(0l);                
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
