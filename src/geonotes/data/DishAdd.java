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
    public Dish execute(HttpServletRequest aRequest, Dish aDish) {

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            aDish.setLastUpdateTime(new Date());
            aDish.setYesVote(0);
            
            // Save
            pm.makePersistent(aDish);
                        
            // Update count
            Store store=StoreGetSingle.getStore(aRequest,pm,aDish.storeId);
            store.setDishCount(store.dishCount+1);
            
            // History
            DishHistory dishHistory=new DishHistory();
            dishHistory.setDishId(aDish.getKey().getId());
            dishHistory.setNote(aDish.note);
            dishHistory.setLastUpdateTime(aDish.lastUpdateTime);
            dishHistory.setStoreId(aDish.storeId);
            dishHistory.setYesVote(aDish.yesVote);
            dishHistory.setUser(aDish.user);
            pm.makePersistent(dishHistory);
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e);
            e.printStackTrace();
            RequestUtils.addEditUsingKey(aRequest,"requestNotProcessedEditMsssage");
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return aDish;
    }
}
