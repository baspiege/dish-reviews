package geonotes.data;

import geonotes.data.model.Dish;
import geonotes.utils.NumberUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Get dishes.
 *
 * @author Brian Spiegel
 */
public class DishesGetAll {

    /**
     * Get dishes.
     *
     * @param aStoreId
     * @param aStart starting position
     * @param aSortBy sort by
     * @since 1.0
     */
    public List<Dish> execute(Long aStoreId, Long aStart, String aSortBy) {
        PersistenceManager pm=null;
        List<Dish> results=null;
        try {
            pm=PMF.get().getPersistenceManager();
            Query query=null;
            try {
                query = pm.newQuery(Dish.class);
                query.setFilter("storeId==storeIdParam");
                query.declareParameters("long storeIdParam");

                // Sorting
                if (aSortBy==null || aSortBy.equalsIgnoreCase("name")){
                    query.setOrdering("noteLowerCase ASC");
                } else if (aSortBy.equalsIgnoreCase("vote")){
                    query.setOrdering("yesVote DESC");
                }

                query.setRange(aStart, aStart+10);

                results = (List<Dish>) query.execute(aStoreId);

                // Bug workaround.  Get size actually triggers the underlying database call.
                results.size();
            } finally {
                if (query!=null) {
                    query.closeAll();
                }
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return results;
    }
}
