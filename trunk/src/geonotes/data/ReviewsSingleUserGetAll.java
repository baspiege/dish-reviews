package geonotes.data;

import geonotes.data.model.Review;
import geonotes.utils.NumberUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Get reviews.
 *
 * @author Brian Spiegel
 */
public class ReviewsSingleUserGetAll {

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

                String user=(String)aRequest.getAttribute("user");
                Long start=(Long)aRequest.getAttribute("start");
                
                query = pm.newQuery(Review.class);
                query.setFilter("user==userParam");
                query.declareParameters("String userParam");
                query.setOrdering("lastUpdateTime DESC");
                query.setRange(start, start+10);
                
                List<Review> results = (List<Review>) query.execute(user);
                
                // Bug workaround.  Get size actually triggers the underlying database call.
                results.size();
                
                // Set into request
                aRequest.setAttribute("reviews", results);
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
