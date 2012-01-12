package geonotes.data;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.DishHistory;
import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;

/**
 * Add a dish.
 *
 * @author Brian Spiegel
 */
public class DishAdd {

    /**
     * Add a dish.
     *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Note
        String note=(String)aRequest.getAttribute("note");
        Long storeId=(Long)aRequest.getAttribute("storeId");
        String user=(String)aRequest.getAttribute("user");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Dish dish=new Dish();
            dish.setNote(note);
            dish.setLastUpdateTime(new Date());
            dish.setStoreId(storeId);
            dish.setYesVote(0);
            dish.setUser(user);
            
            // Save
            pm.makePersistent(dish);
                        
            // Update count
            Store store=StoreGetSingle.getStore(aRequest,pm,storeId.longValue());
            store.setDishCount(store.dishCount+1);
            
            // History
            DishHistory dishHistory=new DishHistory();
            dishHistory.setDishId(dish.getKey().getId());
            dishHistory.setNote(dish.note);
            dishHistory.setLastUpdateTime(dish.lastUpdateTime);
            dishHistory.setStoreId(dish.storeId);
            dishHistory.setYesVote(dish.yesVote);
            dishHistory.setUser(dish.user);
            pm.makePersistent(dishHistory);

            aRequest.setAttribute("dish", dish);
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
