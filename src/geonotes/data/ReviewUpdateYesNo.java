package geonotes.data;

import geonotes.data.model.Review;
import geonotes.data.model.ReviewVote;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Update a review.
 *
 * @author Brian Spiegel
 */
public class ReviewUpdateYesNo {

    /**
     * Update vote.
     *
     * @param aReview to update
     * @param aVote yes or no
     * @param aUser
     *
     * @since 1.0
     */
    public static void execute(Review aReview, String aVote, String aUser) {
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            // Update count
            Review review=ReviewGetSingle.getReview(pm,aReview.getKey().getId());
            if (review!=null){
                if (aVote.equals("yes")){
                  review.setYesVote(review.getYesVote()+1);
                }
            }

            // Record vote
            ReviewVote reviewVote=new ReviewVote();
            reviewVote.setReviewId(review.getKey().getId());
            reviewVote.setUser(review.getUser());
            pm.makePersistent(reviewVote);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
    
    /**
     * Check if voted.
     *
     * @param aReview to update
     * @param aVote yes or no
     * @param aUser
     * @return a boolean indicating if user has voted
     *
     * @since 1.0
     */
    public static boolean hasVoted(Review aReview, String aVote, String aUser) {
        PersistenceManager pm=null;
        Query query=null;
        try {
            pm=PMF.get().getPersistenceManager();
            query = pm.newQuery(ReviewVote.class);
            query.setFilter("(reviewId == reviewIdParam && user==userParam)");
            query.declareParameters("long reviewIdParam, String userParam");
            query.setRange(0,1);
            List<ReviewVote> results = (List<ReviewVote>) query.execute(aReview.getKey().getId(), aUser);
            if (!results.isEmpty()) {
                return true;
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return false;
    }
}
