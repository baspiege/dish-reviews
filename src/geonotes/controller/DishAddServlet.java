package geonotes.controller;

import geonotes.data.DishAdd;
import geonotes.data.StoreGetSingle;
import geonotes.data.model.Dish;
import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;
import geonotes.utils.StringUtils;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Process dish adds.
*/
public class DishAddServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        
        // Default note
        Dish dish=(Dish)request.getAttribute(RequestUtils.DISH);
        dish.setNote("");
        RequestUtils.forwardTo(request,response,ControllerConstants.DISH_ADD);
    }
    
    /**
    * Add dish.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");
        Dish dish=(Dish)request.getAttribute(RequestUtils.DISH);

        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            if (action.equals(bundle.getString("addLabel"))) {		
                // Fields
                String note=RequestUtils.getAlphaInput(request,"note",bundle.getString("noteLabel"),true);
                dish.setNote(note);
                if (!RequestUtils.hasEdits(request)) {
                    dish=new DishAdd().execute(dish);
                }
            }
        }
        
        // If no edits, forward to store.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("dishId",dish.getKey().getId());   
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_ADD);
        }
    }    
    
    /**
    * Set-up the data.
    */
    private void setUpData(HttpServletRequest request) {
    
        // Check if signed in
        boolean isSignedIn=request.getUserPrincipal().getName()!=null;
        if (!isSignedIn) {
            throw new RuntimeException("User principal not found");
        }
                
        // Check store       
        Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",true);
        Store store=null;
        if (storeId!=null) {
            store=new StoreGetSingle().execute(storeId);
        }
        if (store==null) {
            throw new RuntimeException("Store not found:" + storeId);
        }
        
        // Set dish
        Dish dish=new Dish();
        dish.setStoreId(storeId);
        dish.setUser(request.getUserPrincipal().getName());
        request.setAttribute(RequestUtils.DISH, dish);
    }
}