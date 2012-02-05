package geonotes.controller;

import geonotes.data.DishDelete;
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
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.DISH_UPDATE);
    }

    /**
    * Update or delete dish.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);

        Dish dish=(Dish)request.getAttribute(RequestUtils.DISH);
        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            if (action.equals(bundle.getString("updateLabel"))) {		
                String note=RequestUtils.getAlphaInput(request,"note",bundle.getString("nameLabel"),true);
                dish.setNote(note);
                updateAction(request,response);
            } else if (action.equals(bundle.getString("deleteLabel"))) {		
                deleteAction(request,response);
            }
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
        }
    }

    /**
    * Update action.
    */
    private void updateAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Dish dish=(Dish)request.getAttribute(RequestUtils.DISH);
        if (!RequestUtils.hasEdits(request)) {
            dish=DishUpdate.execute(dish);
        }
        // If no edits, forward to dish.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("dishId",dish.getKey().getId());
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_UPDATE);
        }
    }

    /**
    * Delete action.
    */
    private void deleteAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Dish dish=(Dish)request.getAttribute(RequestUtils.DISH);
        if (dish.reviewCount>0) {
            RequestUtils.addEditUsingKey(request,"dishesWithReviewsCantBeDeletedEditMessage");
        }
        if (!RequestUtils.hasEdits(request)) {
            DishDelete.execute(dish);
        }
        // If no edits, forward to store.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("storeId",dish.storeId);
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_UPDATE);
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

        dish.setUser(request.getUserPrincipal().getName());
        request.setAttribute(RequestUtils.DISH, dish);
    }
}