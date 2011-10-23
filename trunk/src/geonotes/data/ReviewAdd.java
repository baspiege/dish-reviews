package geonotes.data;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;

/**
 * Add a review.
 *
 * @author Brian Spiegel
 */
public class ReviewAdd {

    /**
     * Add a review.
     *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Note
        String note=(String)aRequest.getAttribute("note");
        Long dishId=(Long)aRequest.getAttribute("dishId");
        String user=(String)aRequest.getAttribute("user");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Review review=new Review();
            review.setNote(note);
            review.setLastUpdateTime(new Date());
            review.setDishId(dishId);
            review.setYes(0);
            review.setUser(user);
            
            // Save
            pm.makePersistent(review);
            
            // Update review count
            Dish dish=DishGetSingle.getDish(aRequest,pm,dishId.longValue());
            dish.setReviewCount(dish.reviewCount+1);
            
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
