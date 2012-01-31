package geonotes.data;

import geonotes.data.model.Review;
import geonotes.data.model.ReviewHistory;
import geonotes.utils.RequestUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Update all reviews.
 *
 * @author Brian Spiegel
 */
public class ReviewsUpdateAllUtil {

    /**
     * Update all reviews.
     *
     * @since 1.0
     */
    public void execute() {
        PersistenceManager pm=null;
        List<Review> results=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Query query=null;
            try {
                query = pm.newQuery(Review.class);
                results = (List<Review>) query.execute();               
                // Bug workaround.  Get size actually triggers the underlying database call.
                results.size();
            } finally {
                if (query!=null) {
                    query.closeAll();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        try {            
            for (Review review:results) {
                //review.setUser("621566318");                
                
                // History
                /*
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
                */
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