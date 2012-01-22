package geonotes.controller;

import geonotes.data.PMF;
import geonotes.data.DishGetSingle;
import geonotes.data.ReviewGetSingle;
import geonotes.data.ReviewAdd;
import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;
import geonotes.utils.StringUtils;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Process review adds.
*/
public class ReviewAddServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!setUpData(request)) {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
        } else {
            // Default note
            request.setAttribute("note","");
            RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_ADD);
        }
    }
    
    /**
    * Add review.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        if (!setUpData(request)) {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
            return;
        }
        
        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");
     
        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            if (action.equals(bundle.getString("addLabel"))) {		
                // Fields
                RequestUtils.getAlphaInput(request,"note",bundle.getString("noteLabel"),true);
                if (!RequestUtils.hasEdits(request)) {
                    new ReviewAdd().execute(request);
                }
            }
        }
        
        // If no edits, forward to dish.
        if (!RequestUtils.hasEdits(request)) {
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_ADD);
        }
    }    
    
    /**
    * Set-up the data.
    *
    * @return a boolean indiciating success or failure.
    */
    private boolean setUpData(HttpServletRequest request) {
    
        // Check if signed in
        boolean isSignedIn=request.getUserPrincipal().getName()!=null;
        if (!isSignedIn) {
            return false;
        }
        
        // Check dish       
        Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);
        Dish dish=null;
        if (dishId!=null) {
            new DishGetSingle().execute(request);
            dish=(Dish)request.getAttribute(RequestUtils.DISH);
        }
        if (dish==null) {
            return false;
        }
        
        return true;
    }
}