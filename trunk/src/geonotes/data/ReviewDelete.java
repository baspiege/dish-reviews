package geonotes.data;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

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
