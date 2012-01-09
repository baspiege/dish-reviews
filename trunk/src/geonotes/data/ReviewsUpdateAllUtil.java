package geonotes.data;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;

/**
 * Update all reviews.
 *
 * @author Brian Spiegel
 */
public class ReviewsUpdateAllUtil {

    /**
     * Update all reviews.
     *
     * @param aRequest The request
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
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
            System.err.println(this.getClass().getName() + ": " + e);
            e.printStackTrace();
            RequestUtils.addEditUsingKey(aRequest,"requestNotProcessedEditMsssage");
        }
        
        try {            
            for (Review review:results) {
                review.setUser("621566318");                
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
