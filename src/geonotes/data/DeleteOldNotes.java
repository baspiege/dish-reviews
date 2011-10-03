package geonotes.data;

import java.util.Calendar;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.GeoNote;
import geonotes.utils.RequestUtils;

/**
 * Delete old notes.
 *
 * @author Brian Spiegel
 */
public class DeleteOldNotes {

    /**
     * Delete notes.
     *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        Query query=null;
        try {
            pm=PMF.get().getPersistenceManager();

            query = pm.newQuery(GeoNote.class);
            query.setFilter("lastUpdateTime < lastUpdateTimeParam");
            query.declareParameters("java.util.Date lastUpdateTimeParam");

            // Set date.
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DATE, -90);  // 90 days in the past

            query.deletePersistentAll( calendar.getTime() );
        }
        catch (Exception e) {
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
