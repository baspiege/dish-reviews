package geonotes.data;

import geonotes.data.model.Store;
import javax.jdo.PersistenceManager;

/**
 * Delete store
 *
 * @author Brian Spiegel
 */
public class StoreDelete {

    /**
     * Delete store.
     *
     * @param aStore The store to delete
     *
     * @since 1.0
     */
    public static void execute(Store aStore) {
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            aStore=StoreGetSingle.getStore(pm,aStore.getKey().getId());
            if (aStore!=null){
                pm.deletePersistent(aStore);
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
