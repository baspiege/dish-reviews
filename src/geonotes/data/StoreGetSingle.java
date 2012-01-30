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
    public Store execute(Long aStoreId) {
        PersistenceManager pm=null;
        Store store=null;
        try {
            pm=PMF.get().getPersistenceManager();
            store=StoreGetSingle.getStore(pm,aStoreId);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
     * @param aRequest The request
     * @param aPm PersistenceManager
     * @param aStoreId Id
     * @return a store or null if not found
     *
     * @since 1.0
     */
    public static Store getStore(PersistenceManager aPm, long aStoreId) {
        Store store=null;
        Query query=null;
        try {
            query = aPm.newQuery(Store.class); 
            query.setFilter("(key == storeIdParam)"); 
            query.declareParameters("long storeIdParam");
            query.setRange(0,1);
            List<Store> results = (List<Store>) query.execute(aStoreId); 
            if (!results.isEmpty()) {
                store=(Store)results.get(0);
            }
        } finally {
            if (query!=null) {   
                query.closeAll(); 
            }
        }
        return store;
    }    
}
