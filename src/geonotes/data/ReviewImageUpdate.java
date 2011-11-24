package geonotes.data;

import com.google.appengine.api.datastore.Blob;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
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
            
            Review review=ReviewGetSingle.getReview(aRequest,pm,reviewId.longValue());
            
            if (review!=null){
                review.setImage(image);
                review.setImageThumbnail(imageThumbnail);
                review.setHasImage(Boolean.TRUE);

                // Set last image
                Dish dish=DishGetSingle.getDish(aRequest,pm,review.dishId);
                dish.setLastReviewImageId(review.getKey().getId());
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
