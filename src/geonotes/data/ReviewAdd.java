package geonotes.data;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Note;
import geonotes.utils.RequestUtils;

/**
 * Add a review.
 *
 * @author Brian Spiegel
 */
public class ReviewAdd {

    /**
     * Add a review.
     *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Note
        String note=(String)aRequest.getAttribute("note");
        Long type=(Long)aRequest.getAttribute("type");
        Long dishId=(Long)aRequest.getAttribute("dishId");
        //String user=(String)aRequest.getAttribute("user");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Note review=new Note();
            review.setNote(note);
            review.setLastUpdateTime(new Date());
            review.setDishId(dishId);
            // geoNote.setType(type.longValue());
            // dish.setYes(0);
            // dish.setUser(user);
            
            // Save
            pm.makePersistent(review);
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
