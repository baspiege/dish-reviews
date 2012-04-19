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
     * @return the results
     * @since 1.0
     */
    public static List<Review> execute(Long aDishId, Long aStart) {
        List<Review> results=null;
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
                results = (List<Review>) query.execute(aDishId);

                // Touch object to get data.  Size method triggers the underlying database call.
                results.size();
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
        return results;
    }
}
