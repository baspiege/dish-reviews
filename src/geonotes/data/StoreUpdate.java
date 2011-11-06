package geonotes.data;

import java.util.Date;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Store;
import geonotes.data.model.StoreHistory;
import geonotes.utils.DisplayUtils;
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
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long storeId=(Long)aRequest.getAttribute("storeId");
        
        // Fields
        String note=(String)aRequest.getAttribute("note");
        Double longitude=(Double)aRequest.getAttribute("longitude");
        Double latitude=(Double)aRequest.getAttribute("latitude");
        String user=(String)aRequest.getAttribute("user");
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
                        
            Store store=StoreGetSingle.getStore(aRequest,pm,storeId.longValue());
            
            if (store!=null){

                /*
                if (store.dishCount>0) {
                    RequestUtils.addEditUsingKey(aRequest,"storesWithDishesCantBeUpdatedEditMessage");
                    return;
                }
                */
            
                if (note!=null) {
                    store.setNote(note);
                }
                    
                if (longitude!=null) {
                    store.setLongitude(longitude.doubleValue());
                }
                
                if (latitude!=null) {
                    store.setLatitude(latitude.doubleValue());
                }
                
                store.setLastUpdateTime(new Date());
                store.setUser(user);

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
    }
}
