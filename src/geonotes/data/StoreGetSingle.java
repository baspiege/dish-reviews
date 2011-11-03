package geonotes.data;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;

/**
 * Get store.
 *
 * @author Brian Spiegel
 */
public class StoreGetSingle {

    /**
     * Get store.
     *
     * @param aRequest The request
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        
        // Get Id.
        Long storeId=(Long)aRequest.getAttribute("storeId");
        
        try {
            pm=PMF.get().getPersistenceManager();

            Store store=StoreGetSingle.getStore(aRequest,pm,storeId.longValue());

            // Set into request
            aRequest.setAttribute("store", store);

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e);
            e.printStackTrace();
            RequestUtils.addEditUsingKey(aRequest,"requestNotProcessedEditMsssage");
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
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
    public static Store getStore(HttpServletRequest aRequest, PersistenceManager aPm, long aStoreId) {
        Store store=null;

        Query query=null;
        try {
            // Get appts.
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
