package geonotes.data;

import com.google.appengine.api.datastore.Blob;
import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.data.model.ReviewHistory;
import javax.jdo.PersistenceManager;

/**
 * Update an image.
 *
 * @author Brian Spiegel
 */
public class ReviewImageUpdate {

    /**
     * Update an image.
	   *
     * @param aReview the review which will have an updated image
     *
     * @since 1.0
     */
    public void execute(Review aReview) {

        // Fields
        // TODO - input into parameters
        Blob image=(Blob)aRequest.getAttribute("image");
        Blob imageThumbnail=(Blob)aRequest.getAttribute("imageThumbnail");
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            Review review=ReviewGetSingle.getReview(pm,aReview.getKey().getId());
            
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
