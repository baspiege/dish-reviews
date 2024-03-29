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
    public static Dish execute(Dish aDish) {

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            aDish.setLastUpdateTime(new Date());
            aDish.setYesVote(0l);
            aDish.setReviewCount(0l);

            // Save
            pm.makePersistent(aDish);

            // Update count
            Store store=StoreGetSingle.getStore(pm,aDish.getStoreId());
            store.setDishCount(store.getDishCount()+1);

            // History
            DishHistory dishHistory=new DishHistory(aDish);
            pm.makePersistent(dishHistory);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return aDish;
    }
}
