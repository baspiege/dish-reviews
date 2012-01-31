package geonotes.data;

import geonotes.data.model.Dish;
import geonotes.data.model.DishHistory;
import geonotes.data.model.Store;
import java.util.Date;
import javax.jdo.PersistenceManager;

/**
 * Add a dish.
 *
 * @author Brian Spiegel
 */
public class DishAdd {

    /**
     * Add a dish.
     *
     * @param aDish a dish to add
     * @return the added dish
     *
     * @since 1.0
     */
    public Dish execute(Dish aDish) {

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            aDish.setLastUpdateTime(new Date());
            aDish.setYesVote(0);
            
            // Save
            pm.makePersistent(aDish);
            aDish=DishGetSingle.getDish(pm,aDish.getKey().getId());  // Get again so key is set
                        
            // Update count
            Store store=StoreGetSingle.getStore(pm,aDish.storeId);
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
            throw new RuntimeException(e);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return aDish;
    }
}
