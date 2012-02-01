package geonotes.data;

import geonotes.data.model.Dish;
import geonotes.data.model.Store;
import javax.jdo.PersistenceManager;

/**
 * Delete dish
 *
 * @author Brian Spiegel
 */
public class DishDelete {

    /**
     * Delete dish.
	   *
     * @param aDish the dish to delete
     *
     * @since 1.0
     */
    public void execute(Dish aDish) {

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            if (aDish!=null){
                aDish=DishGetSingle.getDish(pm,aDish.getKey().getId());
                pm.deletePersistent(aDish);

                // Update count
                Store store=StoreGetSingle.getStore(pm,aDish.storeId);
                store.setDishCount(store.dishCount-1);
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
