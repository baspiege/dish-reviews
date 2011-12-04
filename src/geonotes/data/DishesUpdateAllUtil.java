package geonotes.data;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.utils.NumberUtils;
import geonotes.utils.RequestUtils;

/**
 * Update all dishes.
 *
 * @author Brian Spiegel
 */
public class DishesUpdateAllUtil {

    /**
     * Update all dishes.
     *
     * @param aRequest The request
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            Query query=null;
            try {
                
                query = pm.newQuery(Dish.class);
                
                List<Dish> results = (List<Dish>) query.execute();
                
                // Bug workaround.  Get size actually triggers the underlying database call.
                results.size();
                
                for (Dish dish:results) {
                
                    //if (dish.yesVote==null) {
                        dish.setYesVote(dish.vote);
                    //}
                
                    // Reset note (to set lowercase)
                    //dish.setNote(dish.note);
                    
                    // Set last image
                    //Review review=ReviewGetSingle.getLastReviewWithImage(aRequest,pm,dish.getKey().getId());
                    //if (review!=null) {
                    //    dish.setLastReviewImageId(review.getKey().getId());
                    //}
                }
                
                // Set into request
                aRequest.setAttribute("dishes", results);
            } finally {
                if (query!=null) {
                    query.closeAll();
                }
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
