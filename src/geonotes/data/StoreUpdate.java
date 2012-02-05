package geonotes.data;

import geonotes.data.model.Store;
import geonotes.data.model.StoreHistory;
import geonotes.utils.MemCacheUtils;
import java.util.Date;
import java.util.Map;
import javax.jdo.PersistenceManager;

/**
 * Update a store.
 *
 * @author Brian Spiegel
 */
public class StoreUpdate {

    /**
     * Update a store.
     *
     * @param aStore a store to update
     * @return the updated store
     *
     * @since 1.0
     */
    public static Store execute(Store aStore) {

        Store store=null;
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            store=StoreGetSingle.getStore(pm,aStore.getKey().getId());
            if (store!=null){
                if (aStore.note!=null) {
                    store.setNote(aStore.note);
                }
                if (aStore.longitude!=null) {
                    store.setLongitude(aStore.longitude);
                }
                if (aStore.latitude!=null) {
                    store.setLatitude(aStore.latitude);
                }
                store.setLastUpdateTime(new Date());

                // Reset cache
                MemCacheUtils.setStore(store);

                // History
                StoreHistory storeHistory=new StoreHistory();
                storeHistory.setStoreId(store.getKey().getId());
                storeHistory.setNote(store.note);
                storeHistory.setLastUpdateTime(store.lastUpdateTime);
                storeHistory.setLongitude(store.longitude);
                storeHistory.setLatitude(store.latitude);
                storeHistory.setYes(store.yes);
                storeHistory.setUser(store.user);
                pm.makePersistent(storeHistory);
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }

        return store;
    }
}
