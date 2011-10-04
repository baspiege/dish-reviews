package geonotes.data;

import java.util.Date;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Store;
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
        Long storeId=(Long)aRequest.getAttribute("id");
        
        // Fields
        String note=(String)aRequest.getAttribute("note");
        Double longitude=(Double)aRequest.getAttribute("longitude");
        Double latitude=(Double)aRequest.getAttribute("latitude");
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
                        
            Store store=StoreGetSingle.getStore(aRequest,pm,storeId.longValue());
            
            if (store!=null){
            
                if (note!=null) {
                    store.setNote(note);
                }
                    
                store.setLastUpdateTime(new Date());

                if (longitude!=null) {
                    store.setLongitude(longitude.doubleValue());
                }
                
                if (latitude!=null) {
                    store.setLatitude(latitude.doubleValue());
                }
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
