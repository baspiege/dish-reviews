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
     * @param aUser user
     * @param aStart The start index
     * @return the results
     * @since 1.0
     */
    public List<Review> execute(String aUser, Long aStart) {
        PersistenceManager pm=null;
        List<Review> results=null;
        try {
            pm=PMF.get().getPersistenceManager();
            Query query=null;
            try {
                query = pm.newQuery(Review.class);
                query.setFilter("user==userParam");
                query.declareParameters("String userParam");
                query.setOrdering("lastUpdateTime DESC");
                query.setRange(aStart, aStart+10);
                results = (List<Review>) query.execute(aUser);

                // Bug workaround.  Get size actually triggers the underlying database call.
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
