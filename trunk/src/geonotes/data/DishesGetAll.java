package geonotes.data;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.utils.NumberUtils;
import geonotes.utils.RequestUtils;

/**
 * Get dishes.
 *
 * @author Brian Spiegel
 */
public class DishesGetAll {

    /**
     * Get geo notes.
     *
     * @param aRequest The request
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            Query query=null;
            try {

                Long storeId=(Long)aRequest.getAttribute("storeId");
                
                query = pm.newQuery(Dish.class);
                query.setFilter("storeId==storeIdParam");
                query.declareParameters("long storeIdParam");
                
                List<Dish> results = (List<Dish>) query.execute(storeId);
                
                // Bug workaround.  Get size actually triggers the underlying database call.
                results.size();
                
                // Set into request
                aRequest.setAttribute("dishes", results);
            } finally {
                if (query!=null) {
                    query.closeAll();
                }
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
