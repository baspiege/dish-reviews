package geonotes.data;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Review;
import geonotes.utils.NumberUtils;
import geonotes.utils.RequestUtils;

/**
 * Get reviews.
 *
 * @author Brian Spiegel
 */
public class ReviewsGetAll {

    /**
     * Get reviews.
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

                Long dishId=(Long)aRequest.getAttribute("dishId");
                
                query = pm.newQuery(Review.class);
                query.setFilter("dishId==dishIdParam");
                query.declareParameters("long dishIdParam");
                query.setOrdering("lastUpdateTime DESC");
                
                List<Review> results = (List<Review>) query.execute(dishId);
                
                // Bug workaround.  Get size actually triggers the underlying database call.
                results.size();
                
                // Set into request
                aRequest.setAttribute("reviews", results);
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
