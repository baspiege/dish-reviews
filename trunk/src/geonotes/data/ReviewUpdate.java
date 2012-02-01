package geonotes.data;

import java.util.Date;
import java.util.Map;
import javax.jdo.PersistenceManager;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.data.model.ReviewHistory;
import geonotes.utils.DisplayUtils;
import geonotes.utils.RequestUtils;

/**
 * Update a review.
 *
 * @author Brian Spiegel
 */
public class ReviewUpdate {

    /**
     * Update a review.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public Review execute(Review aReview) {
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
                        
            Review review=ReviewGetSingle.getReview(pm,aReview.getKey().getId());
            
            if (review!=null){
            
                if (aReview.note!=null) {
                    review.setNote(aReview.note);
                    review.setUser(aReview.user);
                    review.setLastUpdateTime(new Date());
                    
                    // History
                    ReviewHistory reviewHistory=new ReviewHistory(review);
                    pm.makePersistent(reviewHistory);
                    
                    // Last review
                    Dish dish=DishGetSingle.getDish(pm,aReview.getKey().getId());
                    dish.setLastReview(aReview.note);
                    dish.setLastReviewUserId(aReview.user);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return aReview;
    }
}
