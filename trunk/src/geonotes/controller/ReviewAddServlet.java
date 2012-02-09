package geonotes.controller;

import geonotes.data.ReviewAdd;
import geonotes.data.model.Dish;
import geonotes.data.model.Review;
import geonotes.utils.MemCacheUtils;
import geonotes.utils.RequestUtils;
import geonotes.utils.StringUtils;
import java.io.IOException;
import java.util.ResourceBundle;
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
        setUpData(request);

        Review review=(Review)request.getAttribute(RequestUtils.REVIEW);
        // Default note
        review.setNote("");
        RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_ADD);
    }

    /**
    * Add review.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);

        Review review=(Review)request.getAttribute(RequestUtils.REVIEW);
        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            if (action.equals(bundle.getString("addLabel"))) {		
                // Fields
                String note=RequestUtils.getAlphaInput(request,"note",bundle.getString("noteLabel"),true);
                review.setNote(note);
                if (!RequestUtils.hasEdits(request)) {
                    review=ReviewAdd.execute(review);
                }
            }
        }

        // If no edits, forward to dish.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("dishId",review.getDishId());
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.REVIEW_ADD);
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

        // Check dish
        Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);
        Dish dish=null;
        if (dishId!=null) {
            dish=MemCacheUtils.getDish(dishId);
        }
        if (dish==null) {
            throw new RuntimeException("Dish not found: " + dishId);
        }
        request.setAttribute(RequestUtils.DISH, dish);

        // Set review
        Review review=new Review();
        review.setDishId(dishId);
        review.setUser(request.getUserPrincipal().getName());
        request.setAttribute(RequestUtils.REVIEW, review);
    }
}