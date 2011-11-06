package geonotes.data;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Review;
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
        try {
            pm=PMF.get().getPersistenceManager();
            
            Review review=ReviewGetSingle.getReview(aRequest,pm,reviewId.longValue());
            
            if (review!=null){
                review.setImage(null);
                review.setImageThumbnail(null);
                review.setHasImage(Boolean.FALSE);
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