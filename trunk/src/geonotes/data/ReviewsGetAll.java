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
public class ReviewsGetAll {

    /**
     * Get reviews.
     *
     * @param aDishId Dish Id
     * @param aStart The start index
     * @since 1.0
     */
    public void execute(Long aDishId, Long aStart) {
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            Query query=null;
            try {
                query = pm.newQuery(Review.class);
                query.setFilter("dishId==dishIdParam");
                query.declareParameters("long dishIdParam");
                query.setOrdering("lastUpdateTime DESC");
                query.setRange(aStart, aStart+10);

                List<Review> results = (List<Review>) query.execute(aDishId);

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
