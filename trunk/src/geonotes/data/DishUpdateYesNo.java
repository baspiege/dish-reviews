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
    public static void execute(Dish aDish, String aVote, String aUser) {
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            
            // Update vote count
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
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
    
    /**
     * Check if voted.
     *
     * @param aDish to update
     * @param aVote yes or no
     * @param aUser
     * @return a boolean indicating if user has voted
     *
     * @since 1.0
     */
    public static boolean hasVoted(Dish aDish, String aVote, String aUser) {
        PersistenceManager pm=null;
        Query query=null;
        try {
            pm=PMF.get().getPersistenceManager();
            query = pm.newQuery(DishVote.class);
            query.setFilter("(dishId == dishIdParam && user==userParam)");
            query.declareParameters("long dishIdParam, String userParam");
            query.setRange(0,1);
            List<DishVote> results = (List<DishVote>) query.execute(aDish.getKey().getId(), aUser);
            if (!results.isEmpty()) {
                return true;
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return false;
    }
}
