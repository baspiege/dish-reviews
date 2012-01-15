package geonotes.data;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.utils.DisplayUtils;
import geonotes.utils.RequestUtils;

/**
 * Delete review
 *
 * @author Brian Spiegel
 */
public class ReviewDelete {

    /**
     * Delete review.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long reviewId=(Long)aRequest.getAttribute("reviewId");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            Review review=ReviewGetSingle.getReview(aRequest,pm,reviewId.longValue());
            
            if (review!=null){
                pm.deletePersistent(review);
                  
                // Update count
                Dish dish=DishGetSingle.getDish(aRequest,pm,review.dishId);
                dish.setReviewCount(dish.reviewCount-1);
                
                // Reset last review
                Query query = pm.newQuery(Review.class); 
                query.setFilter("(dishId == dishIdParam)"); 
                query.declareParameters("long dishIdParam");
                query.setRange(0,1);
                query.setOrdering("lastUpdateTime DESC");
                List<Review> results = (List<Review>) query.execute(dish.getKey().getId()); 
                if (!results.isEmpty()) {
                    review=(Review)results.get(0);
                    dish.setLastReview(review.note);
                    dish.setLastReviewUserId(review.user);
                } else {
                    dish.setLastReview(null);                
                    dish.setLastReviewUserId(null);
                }
                
                // Set last image
                Review reviewImage=ReviewGetSingle.getLastReviewWithImage(aRequest,pm,dish.getKey().getId());
                if (reviewImage!=null) {
                    dish.setLastReviewImageId(reviewImage.getKey().getId());
                } else {
                    dish.setLastReviewImageId(0l);
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
