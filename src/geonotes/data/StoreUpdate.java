package geonotes.data;

import java.util.Date;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Store;
import geonotes.data.model.StoreHistory;
import geonotes.utils.DisplayUtils;
import geonotes.utils.MemCacheUtils;
import geonotes.utils.RequestUtils;

/**
 * Update a store.
 *
 * @author Brian Spiegel
 */
public class StoreUpdate {

    /**
     * Update a store.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public Store execute(HttpServletRequest aRequest, Store aStore) {
        
        Store store=null;
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
                        
            store=StoreGetSingle.getStore(aRequest,pm,aStore.getKey().getId());
            
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
                MemCacheUtils.setStore(aRequest,store);

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
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e);
            e.printStackTrace();
            RequestUtils.addEditUsingKey(aRequest,"requestNotProcessedEditMsssage");
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        
        return store;
    }
}
