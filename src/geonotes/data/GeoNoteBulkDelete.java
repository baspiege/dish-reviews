package geonotes.data;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.GeoNote;
import geonotes.utils.RequestUtils;

/**
 * Delete all notes.
 *
 * @author Brian Spiegel
 */
public class GeoNoteBulkDelete {

    /**
     * Delete all notes.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        long count=1;
        try {
            pm=PMF.get().getPersistenceManager();
            while (count>0) {
                Query query = pm.newQuery(GeoNote.class);
                query.setRange(0,1000); 
                query.setFilter("(user==userParam)");
                query.declareParameters("String userParam");                
                List<GeoNote> notes=(List<GeoNote>) query.execute("CityDataPortal");
                count=notes.size();
                pm.deletePersistentAll(notes);
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
