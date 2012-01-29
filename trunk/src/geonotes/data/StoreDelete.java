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
    public void execute(HttpServletRequest aRequest, Store aStore) {

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            // TODO - Move this to controller?
            if (aStore.dishCount>0) {
                RequestUtils.addEditUsingKey(aRequest,"storesWithDishesCantBeDeletedEditMessage");
                return;
            }
            
            if (aStore!=null){
                pm.deletePersistent(aStore);
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
