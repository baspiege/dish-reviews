package geonotes.data;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.data.model.ReviewHistory;
import geonotes.utils.RequestUtils;

/**
 * Remove an image.
 *
 * @author Brian Spiegel
 */
public class ReviewImageRemove {

    /**
     * Remove an image.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long reviewId=(Long)aRequest.getAttribute("reviewId");
        
        PersistenceManager pm=null;
        Review review=null;
        try {
            pm=PMF.get().getPersistenceManager();
            review=ReviewGetSingle.getReview(aRequest,pm,reviewId.longValue());
            
            if (review!=null){
                review.setImage(null);
                review.setImageThumbnail(null);
                review.setHasImage(Boolean.FALSE);
                
                // History
                ReviewHistory reviewHistory=new ReviewHistory();
                reviewHistory.setNote(review.note);
                reviewHistory.setLastUpdateTime(review.lastUpdateTime);
                reviewHistory.setDishId(review.dishId);
                reviewHistory.setYesVote(review.yesVote);
                reviewHistory.setUser(review.user);
                reviewHistory.setImage(review.image);
                reviewHistory.setImageThumbnail(review.imageThumbnail);
                reviewHistory.setHasImage(review.hasImage);
                pm.makePersistent(reviewHistory);
            }
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e);
            e.printStackTrace();
            RequestUtils.addEditUsingKey(aRequest,"requestNotProcessedEditMsssage");
            return;
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        
        try {
            pm=PMF.get().getPersistenceManager();
                        
            if (review!=null){
                // Set last image
                Review reviewImage=ReviewGetSingle.getLastReviewWithImage(aRequest,pm,review.dishId);
                Dish dish=DishGetSingle.getDish(pm,review.dishId);
                if (reviewImage!=null) {
                    //System.out.println(review.dishId + ": " + reviewImage.getKey().getId());
                    dish.setLastReviewImageId(reviewImage.getKey().getId());
                } else {
                    //System.out.println(review.dishId + ": null");
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
