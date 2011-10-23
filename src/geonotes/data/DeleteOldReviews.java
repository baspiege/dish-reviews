package geonotes.data;

import java.util.Calendar;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Review;
import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;

/**
 * Delete old reviews.
 *
 * @author Brian Spiegel
 */
public class DeleteOldReviews {

    /**
     * Delete reviews.
     *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        Query query=null;
        try {
            pm=PMF.get().getPersistenceManager();

            query = pm.newQuery(Review.class);
            query.setFilter("lastUpdateTime < lastUpdateTimeParam");
            query.declareParameters("java.util.Date lastUpdateTimeParam");

            // Set date.
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DATE, -180);  // 180 days in the past
            
            // TODO - Also decrement review count from dishes...

            query.deletePersistentAll( calendar.getTime() );
        }
        catch (Exception e) {
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
