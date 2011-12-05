package geonotes.data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Review;
import geonotes.data.model.ReviewVote;
import geonotes.utils.DisplayUtils;
import geonotes.utils.RequestUtils;

/**
 * Update a review.
 *
 * @author Brian Spiegel
 */
public class ReviewUpdateYesNo {

    /**
     * Update vote.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        Long reviewId=(Long)aRequest.getAttribute("reviewId");
        String vote=(String)aRequest.getAttribute("vote");
        String user=(String)aRequest.getAttribute("user");

        PersistenceManager pm=null;
        Query query=null;
        try {
            pm=PMF.get().getPersistenceManager();

            // If user has voted, create edit and return
            query = pm.newQuery(ReviewVote.class);
            query.setFilter("(reviewId == reviewIdParam && user==userParam)");
            query.declareParameters("long reviewIdParam, String userParam");
            query.setRange(0,1);
            List<ReviewVote> results = (List<ReviewVote>) query.execute(reviewId, user);
            if (!results.isEmpty()) {
                RequestUtils.addEditUsingKey(aRequest,"alreadyVotedEditMessage");
                return;
            }

            // Update vote
            Review review=ReviewGetSingle.getReview(aRequest,pm,reviewId.longValue());
            if (review!=null){
                if (vote.equals("yes")){
                  review.setYesVote(review.yesVote+1);
                }
            }
            
            // Record vote
            ReviewVote reviewVote=new ReviewVote();
            reviewVote.setReviewId(reviewId);
            reviewVote.setUser(user);
            pm.makePersistent(reviewVote);
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
