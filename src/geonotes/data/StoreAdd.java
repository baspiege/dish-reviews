package geonotes.data;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Store;
import geonotes.data.model.StoreHistory;
import geonotes.utils.RequestUtils;

/**
 * Add a store.
 *
 * @author Brian Spiegel
 */
public class StoreAdd {

    /**
     * Add a store.
     *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        String note=(String)aRequest.getAttribute("note");
        Double longitude=(Double)aRequest.getAttribute("longitude");
        Double latitude=(Double)aRequest.getAttribute("latitude");
        String user=(String)aRequest.getAttribute("user");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Store store=new Store();
            store.setNote(note);
            store.setLastUpdateTime(new Date());
            store.setLongitude(longitude.doubleValue());
            store.setLatitude(latitude.doubleValue());
            store.setYes(0);
            store.setUser(user);
            
            // Save
            pm.makePersistent(store);
            
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
