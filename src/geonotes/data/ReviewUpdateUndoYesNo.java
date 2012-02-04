package geonotes.data;

import geonotes.data.model.Review;
import geonotes.data.model.ReviewVote;
import java.util.Date;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Update a review vote.
 *
 * @author Brian Spiegel
 */
public class ReviewUpdateUndoYesNo {

    /**
     * Update vote.
     *
     * @param aReview to update
     * @param aVote yes or no
     * @param aUser
     *
     * @since 1.0
     */
    public void execute(Review aReview, String aVote, String aUser) {
        PersistenceManager pm=null;
        Query query=null;
        try {
            pm=PMF.get().getPersistenceManager();

            // If user hasn't voted, create edit and return
            query = pm.newQuery(ReviewVote.class);
            query.setFilter("(reviewId == reviewIdParam && user==userParam)");
            query.declareParameters("long reviewIdParam, String userParam");
            query.setRange(0,1);
            List<ReviewVote> results = (List<ReviewVote>) query.execute(aReview.getKey().getId(), aUser);
            // Delete old votes
            pm.deletePersistentAll(results);

            // Update vote count
            Review review=ReviewGetSingle.getReview(pm,aReview.getKey().getId());
            if (review!=null){
                if (aVote.equals("yes")){
                  review.setYesVote(review.yesVote-1);
                }
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
