package geonotes.data;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Delete review
 *
 * @author Brian Spiegel
 */
public class ReviewDelete {

    /**
     * Delete review.
     *
     * @param aReview review to delete
     *
     * @since 1.0
     */
    public static void execute(Review aReview) {

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Review review=ReviewGetSingle.getReview(pm,aReview.getKey().getId());

            if (review!=null){
                pm.deletePersistent(review);

                // Update count
                Dish dish=DishGetSingle.getDish(pm,review.getDishId());
                dish.setReviewCount(dish.getReviewCount()-1);

                // Reset last review
                Query query = pm.newQuery(Review.class);
                query.setFilter("(dishId == dishIdParam)");
                query.declareParameters("long dishIdParam");
                query.setRange(0,1);
                query.setOrdering("lastUpdateTime DESC");
                List<Review> results = (List<Review>) query.execute(dish.getKey().getId());
                if (!results.isEmpty()) {
                    review=(Review)results.get(0);
                    dish.setLastReview(review.getNote());
                    dish.setLastReviewUserId(review.getUser());
                } else {
                    dish.setLastReview(null);
                    dish.setLastReviewUserId(null);
                }

                // Set last image
                Review reviewImage=ReviewGetSingle.getLastReviewWithImage(pm,dish.getKey().getId());
                if (reviewImage!=null) {
                    dish.setLastReviewImageId(reviewImage.getKey().getId());
                } else {
                    dish.setLastReviewImageId(0l);
                }
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}
