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
    public void execute(Review aReview, Blob aImage, Blob aImageThumbnail) {
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            Review review=ReviewGetSingle.getReview(pm,aReview.getKey().getId());
            
            if (review!=null){
                review.setImage(aImage);
                review.setImageThumbnail(aImageThumbnail);
                review.setHasImage(Boolean.TRUE);
                
                // History
                ReviewHistory reviewHistory=new ReviewHistory(review);
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
