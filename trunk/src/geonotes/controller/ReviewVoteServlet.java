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
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_VOTE);
    }

    /**
    * Update or remove a vote.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);

        Review review=(Review)request.getAttribute(RequestUtils.REVIEW);
        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            String vote=RequestUtils.getAlphaInput(request,"vote","vote",true);
            String user=request.getUserPrincipal().getName();
            if (action.equals(bundle.getString("agreeLabel"))) {
                // Check if already voted
                if (ReviewUpdateYesNo.hasVoted(review,vote,user)) {
                    RequestUtils.addEditUsingKey(request,"alreadyVotedEditMessage");
                }
                if (!RequestUtils.hasEdits(request)) {
                    new ReviewUpdateYesNo().execute(review,vote,user);
                }
            } else if (action.equals(bundle.getString("removeAgreeLabel"))) {	
                // Check if hasn't voted
                if (!ReviewUpdateYesNo.hasVoted(review,vote,user)) {
                    RequestUtils.addEditUsingKey(request,"haventVotedEditMessage");
                }
                if (!RequestUtils.hasEdits(request)) {
                    new ReviewUpdateUndoYesNo().execute(review,vote,user);
                }
            }
        }

        // If no edits, forward to dish.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("dishId",review.dishId);
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_VOTE);
        }
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

        // Get review
        Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",true);
        Review review=null;
        if (reviewId!=null) {
            review=new ReviewGetSingle().execute(reviewId);
            request.setAttribute(RequestUtils.REVIEW, review);
        }
        if (review==null) {
            throw new RuntimeException("Review not found: " + reviewId);
        }

        request.setAttribute(RequestUtils.REVIEW, review);
    }
}