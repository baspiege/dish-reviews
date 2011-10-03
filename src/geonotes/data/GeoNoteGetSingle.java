package geonotes.data;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.GeoNote;
import geonotes.utils.RequestUtils;

/**
 * Get geo note.
 *
 * @author Brian Spiegel
 */
public class GeoNoteGetSingle {

    /**
     * Get geo notes.
     *
     * @param aRequest The request
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        
        // Get Id.
        Long geoNoteId=(Long)aRequest.getAttribute("id");
        
        try {
            pm=PMF.get().getPersistenceManager();

            GeoNote geoNote=GeoNoteGetSingle.getGeoNote(aRequest,pm,geoNoteId.longValue());

            // Set into request
            aRequest.setAttribute("geoNote", geoNote);

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
     * Get a GeoNote.
     *
     * @param aRequest The request
     * @param aPm PersistenceManager
     * @param aGeoNoteId geo note Id
     * @return a geo note or null if not found
     *
     * @since 1.0
     */
    public static GeoNote getGeoNote(HttpServletRequest aRequest, PersistenceManager aPm, long aGeoNoteId) {
        GeoNote geoNote=null;

        Query query=null;
        try {
            // Get appts.
            query = aPm.newQuery(GeoNote.class); 
            query.setFilter("(key == geoNoteIdParam)"); 
            query.declareParameters("long geoNoteIdParam");
            query.setRange(0,1);

            List<GeoNote> results = (List<GeoNote>) query.execute(aGeoNoteId); 

            if (!results.isEmpty()) {
                geoNote=(GeoNote)results.get(0);
            }
        } finally {
            if (query!=null) {   
                query.closeAll(); 
            }
        }

        return geoNote;
    }    
}
