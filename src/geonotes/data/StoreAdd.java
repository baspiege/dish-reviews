package geonotes.data;

import geonotes.data.model.Store;
import geonotes.data.model.StoreHistory;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Add a store.
 *
 * @author Brian Spiegel
 */
public class StoreAdd {

    /**
     * Add a store.
     *
     * @param aStore store to add
     * @return the added store
     *
     * @since 1.0
     */
    public Store execute(Store aStore) {

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            aStore.setLastUpdateTime(new Date());
            aStore.setYes(0);

            // Save
            aStore=pm.makePersistent(aStore);

            // History
            StoreHistory storeHistory=new StoreHistory();
            storeHistory.setStoreId(aStore.getKey().getId());
            storeHistory.setNote(aStore.note);
            storeHistory.setLastUpdateTime(aStore.lastUpdateTime);
            storeHistory.setLongitude(aStore.longitude);
            storeHistory.setLatitude(aStore.latitude);
            storeHistory.setYes(aStore.yes);
            storeHistory.setUser(aStore.user);
            pm.makePersistent(storeHistory);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return aStore;
    }
}
