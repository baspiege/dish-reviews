package geonotes.data;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.utils.RequestUtils;

/**
 * Add a dish.
 *
 * @author Brian Spiegel
 */
public class DishAdd {

    /**
     * Add a dish.
     *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        // Note
        String note=(String)aRequest.getAttribute("note");
        Long type=(Long)aRequest.getAttribute("type");
        Long storeId=(Long)aRequest.getAttribute("storeId");
        //String user=(String)aRequest.getAttribute("user");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Dish dish=new Dish();
            dish.setNote(note);
            dish.setLastUpdateTime(new Date());
            dish.setStoreId(storeId);
            // geoNote.setType(type.longValue());
            // dish.setYes(0);
            // dish.setUser(user);
            
            // Save
            pm.makePersistent(dish);
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
