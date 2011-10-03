package geonotes.data;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.GeoNote;
import geonotes.utils.RequestUtils;

/**
 * Remove an image.
 *
 * @author Brian Spiegel
 */
public class GeoNoteImageRemove {

    /**
     * Remove an image.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Get Id.
        Long geoNoteId=(Long)aRequest.getAttribute("id");
        
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            GeoNote geoNote=GeoNoteGetSingle.getGeoNote(aRequest,pm,geoNoteId.longValue());
            
            if (geoNote!=null){
                geoNote.setImage(null);
                geoNote.setImageThumbnail(null);
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
