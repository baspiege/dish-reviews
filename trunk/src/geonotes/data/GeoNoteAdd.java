package geonotes.data;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.GeoNote;
import geonotes.utils.RequestUtils;

/**
 * Add a geo note.
 *
 * @author Brian Spiegel
 */
public class GeoNoteAdd {

    /**
     * Add a note.
     *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Note
        String note=(String)aRequest.getAttribute("note");
        Double longitude=(Double)aRequest.getAttribute("longitude");
        Double latitude=(Double)aRequest.getAttribute("latitude");
        Long type=(Long)aRequest.getAttribute("type");
        String user=(String)aRequest.getAttribute("user");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            GeoNote geoNote=new GeoNote();
            geoNote.setNote(note);
            geoNote.setLastUpdateTime(new Date());
            geoNote.setLongitude(longitude.doubleValue());
            geoNote.setLatitude(latitude.doubleValue());
            geoNote.setType(type.longValue());
            geoNote.setYes(0);
            geoNote.setUser(user);
            
            // Save
            pm.makePersistent(geoNote);
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
