package geonotes.data;

import java.util.Date;
import java.util.Map;
import javax.jdo.PersistenceManager;

import geonotes.data.model.Dish;
import geonotes.data.model.DishHistory;
import geonotes.utils.DisplayUtils;
import geonotes.utils.MemCacheUtils;

/**
 * Update a dish.
 *
 * @author Brian Spiegel
 */
public class DishUpdate {

    /**
     * Update a dish.
	   *
     * @param aDish dish
     * @return an updated dish
     *
     * @since 1.0
     */
    public Dish execute(Dish aDish) {
        
        Dish dish=null;
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
                        
            dish=DishGetSingle.getDish(pm,aDish.getKey().getId());
                        
            if (dish!=null){
            
                if (aDish.note!=null) {
                    dish.setNote(aDish.note);
                }
                    
                dish.setLastUpdateTime(new Date());
                
                // Reset cache
                MemCacheUtils.setDish(dish);
                
                // History
                DishHistory dishHistory=new DishHistory();
                dishHistory.setDishId(dish.getKey().getId());
                dishHistory.setNote(dish.note);
                dishHistory.setLastUpdateTime(dish.lastUpdateTime);
                dishHistory.setStoreId(dish.storeId);
                dishHistory.setYesVote(dish.yesVote);
                dishHistory.setUser(dish.user);
                pm.makePersistent(dishHistory);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return dish;
    }
}
