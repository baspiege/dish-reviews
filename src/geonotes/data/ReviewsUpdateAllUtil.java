package geonotes.data;

import geonotes.data.model.Review;
import geonotes.data.model.ReviewHistory;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Update all reviews.
 *
 * @author Brian Spiegel
 */
public class ReviewsUpdateAllUtil {

    /**
     * Update all reviews.
     *
     * @since 1.0
     */
    public static void execute() {
        PersistenceManager pm=null;
        List<Review> results=null;
        pm=PMF.get().getPersistenceManager();

        Query query=null;
        try {
            query = pm.newQuery(Review.class);
            results = (List<Review>) query.execute();
            // Bug workaround.  Get size actually triggers the underlying database call.
            results.size();
        } finally {
            if (query!=null) {
                query.closeAll();
            }
        }

        try {
            for (Review review:results) {
                //review.setUser("621566318");

                // History
                /*
                ReviewHistory reviewHistory=new ReviewHistory(review);
                pm.makePersistent(reviewHistory);
                */
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
