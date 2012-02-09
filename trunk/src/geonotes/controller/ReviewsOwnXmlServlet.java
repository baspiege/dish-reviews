package geonotes.controller;

import geonotes.data.ReviewsSingleUserGetAll;
import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;
import geonotes.view.xml.ReviewsXml;
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
        List<Review> reviews=ReviewsSingleUserGetAll.execute(user, start);
        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
        ReviewsXml.outputXml(reviews,request.getUserPrincipal().getName(),true,response.getOutputStream());
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