package geonotes.images;

import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import geonotes.data.ReviewGetSingle;
import geonotes.data.PMF;
import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;

/**
* Return the thumbnail image of the last review for a dish.
*/
public class ReviewLastThumbNailImage extends HttpServlet {

    /**
    * Process the request.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestUtils.setCacheHeaders(response, 30);
    
        // Get Id.
        Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Review review=ReviewGetSingle.getLastReviewWithImage(pm,dishId.longValue());

            if (review!=null && review.getImageThumbnail()!=null){
                response.setContentType("image/jpeg");
                response.getOutputStream().write(review.getImageThumbnail().getBytes());
            }
        } catch (Exception e) {
            System.err.println(ReviewThumbNailImage.class.getName() + ": " + e);
            e.printStackTrace();
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}