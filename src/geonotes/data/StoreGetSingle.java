package geonotes.data;

import geonotes.data.model.Store;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Get store.
 *
 * @author Brian Spiegel
 */
public class StoreGetSingle {

    /**
     * Get store.
     *
     * @param aStoreId the store Id
     * @return a store
     * @since 1.0
     */
    public static Store execute(Long aStoreId) {
        PersistenceManager pm=null;
        Store store=null;
        try {
            pm=PMF.get().getPersistenceManager();
            store=StoreGetSingle.getStore(pm,aStoreId);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return store;
    }

    /**
     * Get a Store.
     *
     * @param aPm PersistenceManager
     * @param aStoreId Id
     * @return a store or null if not found
     *
     * @since 1.0
     */
    public static Store getStore(PersistenceManager aPm, long aStoreId) {
        return aPm.getObjectById(Store.class, aStoreId);
    }
}
