package geonotes.data;

import geonotes.data.model.Dish;
import geonotes.data.model.DishVote;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Update a dish vote.
 *
 * @author Brian Spiegel
 */
public class DishUpdateUndoYesNo {

    /**
     * Update vote.
	   *
     * @param aDish to update
     * @param aVote yes or no
     * @param aUser
     *
     * @since 1.0
     */
    public void execute(Dish aDish, String aVote, String aUser) {

        PersistenceManager pm=null;
        Query query=null;
        try {
            pm=PMF.get().getPersistenceManager();

            /* TODO - Move this to separate method and call in controller.
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
            */

            // Update vote
            Dish dish=DishGetSingle.getDish(pm,aDish.getKey().getId());
            if (dish!=null){
                if (aVote.equals("yes")){
                  dish.setYesVote(dish.yesVote-1);
                }
            }
            
            // Delete old votes
            //pm.deletePersistentAll(results);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
