package geonotes.data;

import geonotes.data.model.Dish;
import geonotes.utils.NumberUtils;
import geonotes.utils.RequestUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

/**
 * Get dishes.
 *
 * @author Brian Spiegel
 */
public class DishesGetAll {

    /**
     * Get dishes.
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
                Long start=(Long)aRequest.getAttribute("start");
                String sortBy=(String)aRequest.getAttribute("sortBy");
                
                query = pm.newQuery(Dish.class);
                query.setFilter("storeId==storeIdParam");
                query.declareParameters("long storeIdParam");
                
                // Sorting
                if (sortBy==null || sortBy.equalsIgnoreCase("name")){
                    query.setOrdering("noteLowerCase ASC");
                } else if (sortBy.equalsIgnoreCase("vote")){
                    query.setOrdering("yesVote DESC");
                }
                
                query.setRange(start, start+10);
                
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
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
