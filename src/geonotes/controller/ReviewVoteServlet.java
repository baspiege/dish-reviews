package geonotes.controller;

import geonotes.data.ReviewGetSingle;
import geonotes.data.ReviewUpdateYesNo;
import geonotes.data.ReviewUpdateUndoYesNo;
import geonotes.data.model.Review;
import geonotes.utils.RequestUtils;
import geonotes.utils.StringUtils;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Process review votes.
*/
public class ReviewVoteServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!setUpData(request)) {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_VOTE);
        }
    }
    
    /**
    * Update or remove a vote.
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
            RequestUtils.getAlphaInput(request,"vote","vote",true);
            if (action.equals(bundle.getString("agreeLabel"))) {
                if (!RequestUtils.hasEdits(request)) {
                    new ReviewUpdateYesNo().execute(request);
                }
            } else if (action.equals(bundle.getString("removeAgreeLabel"))) {		
                if (!RequestUtils.hasEdits(request)) {
                    new ReviewUpdateUndoYesNo().execute(request);
                }
            }
        }
        
        // If no edits, forward to dish.
        if (!RequestUtils.hasEdits(request)) {
            Review review=(Review)request.getAttribute(RequestUtils.REVIEW);
            request.setAttribute("dishId",review.dishId);
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_VOTE);
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
           
        // Get review
        Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",true);
        Review review=null;
        if (reviewId!=null) {
            new ReviewGetSingle().execute(request);
            review=(Review)request.getAttribute(RequestUtils.REVIEW);
        }
        if (review==null) {
            return false;
        }
        
        return true;
    }
}