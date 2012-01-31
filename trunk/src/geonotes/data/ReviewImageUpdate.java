package geonotes.data;

import com.google.appengine.api.datastore.Blob;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.data.model.ReviewHistory;
import geonotes.utils.RequestUtils;

/**
 * Update an image.
 *
 * @author Brian Spiegel
 */
public class ReviewImageUpdate {

    /**
     * Update an image.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long reviewId=(Long)aRequest.getAttribute("reviewId");
        
        // Fields
        Blob image=(Blob)aRequest.getAttribute("image");
        Blob imageThumbnail=(Blob)aRequest.getAttribute("imageThumbnail");
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            Review review=ReviewGetSingle.getReview(pm,reviewId.longValue());
            
            if (review!=null){
                review.setImage(image);
                review.setImageThumbnail(imageThumbnail);
                review.setHasImage(Boolean.TRUE);
                
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

                // Set last image
                Dish dish=DishGetSingle.getDish(pm,review.dishId);
                dish.setLastReviewImageId(review.getKey().getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
