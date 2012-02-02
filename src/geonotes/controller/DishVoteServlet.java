package geonotes.controller;

import geonotes.data.DishUpdateYesNo;
import geonotes.data.DishUpdateUndoYesNo;
import geonotes.data.model.Dish;
import geonotes.utils.RequestUtils;
import geonotes.utils.StringUtils;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Process dish votes.
*/
public class DishVoteServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.DISH_VOTE);
    }
    
    /**
    * Update or remove a vote.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        Dish dish=(Dish)request.getAttribute(RequestUtils.DISH);
        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            String vote=RequestUtils.getAlphaInput(request,"vote","vote",true);
            String user=request.getUserPrincipal().getName();
            if (action.equals(bundle.getString("likeLabel"))) {
                if (!RequestUtils.hasEdits(request)) {
                    new DishUpdateYesNo().execute(dish,vote,user);
                }
            } else if (action.equals(bundle.getString("unlikeLabel"))) {		
                if (!RequestUtils.hasEdits(request)) {
                    new DishUpdateUndoYesNo().execute(dish,vote,user);
                }
            }
        }
        
        // If no edits, forward to store.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("storeId",dish.storeId);
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_VOTE);
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
           
        // Get dish
        Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);
        Dish dish=null;
        if (dishId!=null) {
            dish=RequestUtils.getDish(dishId);
        }
        if (dish==null) {
            throw new RuntimeException("Dish not found: " + dishId);
        }
        
        request.setAttribute(RequestUtils.DISH, dish);
    }
}