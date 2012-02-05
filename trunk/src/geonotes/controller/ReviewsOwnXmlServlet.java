package geonotes.controller;

import geonotes.data.ReviewsSingleUserGetAll;
import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Get stores in XML format.
*/
public class ReviewsOwnXmlServlet extends HttpServlet {

    /**
    * Get data.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");
        String user=request.getUserPrincipal().getName();
        Long start=RequestUtils.getNumericInput(request,"start",bundle.getString("startLabel"),true);
        List<Review> results=ReviewsSingleUserGetAll.execute(user, start);
        request.setAttribute("reviews", results);
        RequestUtils.forwardTo(request,response,ControllerConstants.REVIEWS_OWN_XML);
    }

    /**
    * No post for now.  Route to main page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestUtils.forwardTo(request,response,ControllerConstants.STORE_REDIRECT);
    }

    /**
    * Set-up the data.
    */
    private void setUpData(HttpServletRequest request) {

        // Check if signed in
        boolean isSignedIn=request.getUserPrincipal().getName()!=null;
        if (!isSignedIn) {
           throw new SecurityException("User principal not found");
        }
    }
}