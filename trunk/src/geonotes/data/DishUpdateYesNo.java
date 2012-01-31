package geonotes.data;

import geonotes.data.model.Dish;
import geonotes.data.model.DishVote;
import java.util.Date;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Update a vote.
 *
 * @author Brian Spiegel
 */
public class DishUpdateYesNo {

    /**
     * Update vote.
	   *
     * @param aDish a dish
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

            /* TODO Move this
            // If user has voted, create edit and return
            query = pm.newQuery(DishVote.class);
            query.setFilter("(dishId == dishIdParam && user==userParam)");
            query.declareParameters("long dishIdParam, String userParam");
            query.setRange(0,1);
            List<DishVote> results = (List<DishVote>) query.execute(dishId, aUser);
            if (!results.isEmpty()) {
                RequestUtils.addEditUsingKey(aRequest,"alreadyVotedEditMessage");
                return;
            }
            */

            // Update vote
            Dish dish=DishGetSingle.getDish(pm,aDish.getKey().getId());
            if (dish!=null){
                if (aVote.equals("yes")){
                  dish.setYesVote(dish.yesVote+1);
                }
            }
            
            // Record vote
            DishVote dishVote=new DishVote();
            dishVote.setDishId(aDish.getKey().getId());
            dishVote.setUser(aUser);
            pm.makePersistent(dishVote);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}