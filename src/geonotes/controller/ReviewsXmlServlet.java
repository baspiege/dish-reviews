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
        RequestUtils.getNumericInput(request,"dishId",bundle.getString("dishId"),true);
        RequestUtils.getNumericInput(request,"start",bundle.getString("startLabel"),true);
        Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",false);
        
        List<Review> reviews=null;
        if (reviewId!=null) {
           new ReviewGetSingle().execute(request);
           // Add to list.
           reviews=new ArrayList<Review>();
           reviews.add((Review)request.getAttribute(RequestUtils.REVIEW));
           request.setAttribute("reviews",reviews);
        } else {   
            new ReviewsGetAll().execute(request);
        }
        
        RequestUtils.forwardTo(request,response,ControllerConstants.REVIEWS_XML);
    }
    
    /**
    * No post for now.  Route to main page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
    }    
}