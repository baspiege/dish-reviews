package geonotes.controller;

import geonotes.data.ReviewGetSingle;
import geonotes.data.ReviewsGetAll;
import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Get stores in XML format.
*/
public class ReviewsXmlServlet extends HttpServlet {

    /**
    * Get data.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("Text");
        Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",false);
        List<Review> reviews=null;
        // If review Id, just get that.  Else, get all.
        if (reviewId!=null) {
           new ReviewGetSingle().execute(reviewId);
           // Add to list.
           reviews=new ArrayList<Review>();
           reviews.add((Review)request.getAttribute(RequestUtils.REVIEW));
        } else {
            Long dishId=RequestUtils.getNumericInput(request,"dishId",bundle.getString("dishId"),true);
            Long start=RequestUtils.getNumericInput(request,"start",bundle.getString("startLabel"),true);
            reviews=new ReviewsGetAll().execute(dishId, start);
        }
        request.setAttribute("reviews",reviews);

        RequestUtils.forwardTo(request,response,ControllerConstants.REVIEWS_XML);
    }

    /**
    * No post for now.  Route to main page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
    }
}