package geonotes.data;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;

/**
 * Get review.
 *
 * @author Brian Spiegel
 */
public class ReviewGetSingle {

    /**
     * Get review.
     *
     * @param aRequest The request
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        
        // Get Id.
        Long reviewId=(Long)aRequest.getAttribute("reviewId");
        
        try {
            pm=PMF.get().getPersistenceManager();

            Review review=ReviewGetSingle.getReview(aRequest,pm,reviewId.longValue());

            // Set into request
            aRequest.setAttribute("review", review);

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
    
    /**
     * Get a review.
     *
     * @param aRequest The request
     * @param aPm PersistenceManager
     * @param aReviewId review Id
     * @return a review null if not found
     *
     * @since 1.0
     */
    public static Review getReview(HttpServletRequest aRequest, PersistenceManager aPm, long aReviewId) {
        Review review=null;

        Query query=null;
        try {
            query = aPm.newQuery(Review.class); 
            query.setFilter("(key == reviewIdParam)"); 
            query.declareParameters("long reviewIdParam");
            query.setRange(0,1);

            List<Review> results = (List<Review>) query.execute(aReviewId); 

            if (!results.isEmpty()) {
                review=(Review)results.get(0);
            }
        } finally {
            if (query!=null) {   
                query.closeAll(); 
            }
        }

        return review;
    }    
    
    /**
     * Get the last review.
     *
     * @param aRequest The request
     * @param aPm PersistenceManager
     * @param aDishId dish Id
     * @return a review null if not found
     *
     * @since 1.0
     */
    public static Review getLastReview(HttpServletRequest aRequest, PersistenceManager aPm, long aDishId) {
        Review review=null;

        Query query=null;
        try {
            query = aPm.newQuery(Review.class); 
            query.setFilter("(dishId == dishIdParam)"); 
            query.declareParameters("long dishIdParam");
            query.setRange(0,1);
            query.setOrdering("lastUpdateTime DESC");

            List<Review> results = (List<Review>) query.execute(aDishId); 

            if (!results.isEmpty()) {
                review=(Review)results.get(0);
            }
        } finally {
            if (query!=null) {   
                query.closeAll(); 
            }
        }

        return review;
    }    
}
