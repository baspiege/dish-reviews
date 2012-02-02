package geonotes.data;

import geonotes.data.model.Review;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Get review.
 *
 * @author Brian Spiegel
 */
public class ReviewGetSingle {

    /**
     * Get review.
     *
     * @since 1.0
     */
    public Review execute(Long aReviewId) {
        PersistenceManager pm=null;

        Review review=null;
        try {
            pm=PMF.get().getPersistenceManager();
            review=ReviewGetSingle.getReview(pm,aReviewId);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return review;
    }

    /**
     * Get a review.
     *
     * @param aPm PersistenceManager
     * @param aReviewId review Id
     * @return a review null if not found
     *
     * @since 1.0
     */
    public static Review getReview(PersistenceManager aPm, long aReviewId) {
        return aPm.getObjectById(Review.class, aReviewId);
    }

    /**
     * Get the last review.
     *
     * @param aPm PersistenceManager
     * @param aDishId dish Id
     * @return a review null if not found
     *
     * @since 1.0
     */
    public static Review getLastReview(PersistenceManager aPm, long aDishId) {
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

    /**
     * Get the last review with image.
     *
     * @param aPm PersistenceManager
     * @param aDishId dish Id
     * @return a review null if not found
     *
     * @since 1.0
     */
    public static Review getLastReviewWithImage(PersistenceManager aPm, long aDishId) {
        Review review=null;

        Query query=null;
        try {
            query = aPm.newQuery(Review.class);
            query.setFilter("(dishId == dishIdParam) && (hasImage==true)");
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
