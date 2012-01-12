package geonotes.data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.DishVote;
import geonotes.utils.DisplayUtils;
import geonotes.utils.RequestUtils;

/**
 * Update a dish vote.
 *
 * @author Brian Spiegel
 */
public class DishUpdateUndoYesNo {

    /**
     * Update vote.
	   *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {

        Long dishId=(Long)aRequest.getAttribute("dishId");
        String vote=(String)aRequest.getAttribute("vote");
        String user=(String)aRequest.getAttribute("user");

        PersistenceManager pm=null;
        Query query=null;
        try {
            pm=PMF.get().getPersistenceManager();

            // If user hasn't voted, create edit and return
            query = pm.newQuery(DishVote.class);
            query.setFilter("(dishId == dishIdParam && user==userParam)");
            query.declareParameters("long dishIdParam, String userParam");
            query.setRange(0,1);
            List<DishVote> results = (List<DishVote>) query.execute(dishId, user);
            if (results.isEmpty()) {
                RequestUtils.addEditUsingKey(aRequest,"haventVotedEditMessage");
                return;
            }

            // Update vote
            Dish dish=DishGetSingle.getDish(aRequest,pm,dishId.longValue());
            if (dish!=null){
                if (vote.equals("yes")){
                  dish.setYesVote(dish.yesVote-1);
                }
            }
            
            // Delete old votes
            pm.deletePersistentAll(results);
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