package geonotes.data;

import java.util.Date;
import java.util.Map;
import javax.jdo.PersistenceManager;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.data.model.ReviewHistory;

/**
 * Update a review.
 *
 * @author Brian Spiegel
 */
public class ReviewUpdate {

    /**
     * Update a review.
     *
     * @since 1.0
     */
    public static Review execute(Review aReview) {

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Review review=ReviewGetSingle.getReview(pm,aReview.getKey().getId());

            if (review!=null){

                if (aReview.getNote()!=null) {
                    review.setNote(aReview.getNote());
                    review.setUser(aReview.getUser());
                    review.setLastUpdateTime(new Date());

                    // History
                    ReviewHistory reviewHistory=new ReviewHistory(review);
                    pm.makePersistent(reviewHistory);

                    // Last review
                    Dish dish=DishGetSingle.getDish(pm,aReview.getDishId());
                    dish.setLastReview(aReview.getNote());
                    dish.setLastReviewUserId(aReview.getUser());
                }
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return aReview;
    }
}
