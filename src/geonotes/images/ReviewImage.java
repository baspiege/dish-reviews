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
* Return an image.
*/
public class ReviewImage extends HttpServlet {

    /**
    * Process the request.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get Id.
        Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",true);

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            Review review=ReviewGetSingle.getReview(pm,reviewId.longValue());

            if (review!=null){
                response.setContentType("image/jpeg");
                response.getOutputStream().write(review.getImage().getBytes());
            }
        } catch (Exception e) {
            System.err.println(ReviewImage.class.getName() + ": " + e);
            e.printStackTrace();
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}