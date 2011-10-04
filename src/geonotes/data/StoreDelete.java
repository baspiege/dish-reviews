package geonotes.data;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Store;
import geonotes.utils.DisplayUtils;
import geonotes.utils.RequestUtils;

/**
 * Delete store
 *
 * @author Brian Spiegel
 */
public class StoreDelete {

    /**
     * Delete store.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long storeId=(Long)aRequest.getAttribute("id");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            Store store=StoreGetSingle.getStore(aRequest,pm,storeId.longValue());
            
            if (store!=null){
                pm.deletePersistent(store);
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
