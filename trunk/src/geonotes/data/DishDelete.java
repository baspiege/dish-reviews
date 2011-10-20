package geonotes.data;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.Store;
import geonotes.utils.DisplayUtils;
import geonotes.utils.RequestUtils;

/**
 * Delete dish
 *
 * @author Brian Spiegel
 */
public class DishDelete {

    /**
     * Delete dish.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long dishId=(Long)aRequest.getAttribute("dishId");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            Dish dish=DishGetSingle.getDish(aRequest,pm,dishId.longValue());
            
            if (dish.reviewCount>0) {
                RequestUtils.addEditUsingKey(aRequest,"dishesWithReviewsCantBeDeletedEditMessage");
                return;
            }
            
            if (dish!=null){
                pm.deletePersistent(dish);
                
                // Update count
                Store store=StoreGetSingle.getStore(aRequest,pm,dish.storeId);
                store.setDishCount(store.dishCount-1);
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
