package geonotes.data;

import com.google.appengine.api.datastore.Blob;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;

/**
 * Update an image.
 *
 * @author Brian Spiegel
 */
public class StoreImageUpdate {

    /**
     * Update a note.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long storeId=(Long)aRequest.getAttribute("id");
        
        // Fields
        Blob image=(Blob)aRequest.getAttribute("image");
        Blob imageThumbnail=(Blob)aRequest.getAttribute("imageThumbnail");
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            Store store=StoreGetSingle.getStore(aRequest,pm,storeId.longValue());
            
            if (store!=null){
                store.setImage(image);
                store.setImageThumbnail(imageThumbnail);
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
