package geonotes.data;

import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.data.model.ReviewHistory;
import javax.jdo.PersistenceManager;

/**
 * Remove an image.
 *
 * @author Brian Spiegel
 */
public class ReviewImageRemove {

    /**
     * Remove an image.
     *
     * @param aReview the review which the image will be removed from
     *
     * @since 1.0
     */
    public void execute(Review aReview) {

        PersistenceManager pm=null;
        Review review=null;
        try {
            pm=PMF.get().getPersistenceManager();
            review=ReviewGetSingle.getReview(pm,aReview.getKey().getId());

            if (review!=null){
                review.setImage(null);
                review.setImageThumbnail(null);
                review.setHasImage(Boolean.FALSE);

                // History
                ReviewHistory reviewHistory=new ReviewHistory(review);
                pm.makePersistent(reviewHistory);
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }

        try {
            pm=PMF.get().getPersistenceManager();

            if (review!=null){
                // Set last image
                Review reviewImage=ReviewGetSingle.getLastReviewWithImage(pm,review.dishId);
                Dish dish=DishGetSingle.getDish(pm,review.dishId);
                if (reviewImage!=null) {
                    //System.out.println(review.dishId + ": " + reviewImage.getKey().getId());
                    dish.setLastReviewImageId(reviewImage.getKey().getId());
                } else {
                    //System.out.println(review.dishId + ": null");
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
