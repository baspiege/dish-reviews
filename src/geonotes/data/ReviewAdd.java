package geonotes.data;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.data.model.ReviewHistory;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Add a review.
 *
 * @author Brian Spiegel
 */
public class ReviewAdd {

    /**
     * Add a review.
     *
     * @since 1.0
     */
    public Review execute(Review aReview) {

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            aReview.setLastUpdateTime(new Date());
            aReview.setYesVote(0);
            pm.makePersistent(aReview);

            // History
            ReviewHistory reviewHistory=new ReviewHistory(aReview);
            pm.makePersistent(reviewHistory);

            // Update review count
            Dish dish=DishGetSingle.getDish(pm,aReview.dishId);
            dish.setReviewCount(dish.reviewCount+1);

            // Last review
            dish.setLastReview(aReview.note);
            dish.setLastReviewUserId(aReview.user);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return aReview;
    }
}
