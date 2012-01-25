package geonotes.controller;

import geonotes.data.DishDelete;
import geonotes.data.DishGetSingle;
import geonotes.data.DishUpdate;
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
* Process dish updates.
*/
public class DishUpdateServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!setUpData(request)) {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_UPDATE);
        }
    }
    
    /**
    * Update or delete dish.
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
            if (action.equals(bundle.getString("updateLabel"))) {		
                // Fields
                RequestUtils.getAlphaInput(request,"note",bundle.getString("nameLabel"),true);
                if (!RequestUtils.hasEdits(request)) {
                    new DishUpdate().execute(request);
                }
            } else if (action.equals(bundle.getString("deleteLabel"))) {		
                if (!RequestUtils.hasEdits(request)) {
                    new DishDelete().execute(request);
                }
            }
        }
        
        // If no edits, forward to dish.
        if (!RequestUtils.hasEdits(request)) {
            Dish dish=(Dish)request.getAttribute(RequestUtils.DISH);
            request.setAttribute("dishId",dish.getKey().getId());
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_UPDATE);
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
           
        // Get dish
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