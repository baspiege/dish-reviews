package geonotes.data;

import java.util.Date;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

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
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long reviewId=(Long)aRequest.getAttribute("reviewId");
        
        // Fields
        String note=(String)aRequest.getAttribute("note");
        String user=(String)aRequest.getAttribute("user");
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
                        
            Review review=ReviewGetSingle.getReview(aRequest,pm,reviewId.longValue());
            
            if (review!=null){
            
                if (note!=null) {
                    review.setNote(note);
                    
                    // History
                    ReviewHistory reviewHistory=new ReviewHistory();
                    reviewHistory.setNote(review.note);
                    reviewHistory.setLastUpdateTime(review.lastUpdateTime);
                    reviewHistory.setDishId(review.dishId);
                    reviewHistory.setYesVote(review.yesVote);
                    reviewHistory.setUser(review.user);
                    pm.makePersistent(reviewHistory);
                    
                    // Last review
                    Dish dish=DishGetSingle.getDish(aRequest,pm,review.dishId);
                    dish.setLastReview(note);
                    dish.setLastReviewUserId(user);
                    
                    review.setLastUpdateTime(new Date());
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
