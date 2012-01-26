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
        if (!setUpData(request)) {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
        } else {
            // Default note
            request.setAttribute("note","");
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_ADD);
        }
    }
    
    /**
    * Add dish.
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
                    new DishAdd().execute(request);
                }
            }
        }
        
        // If no edits, forward to dish.
        if (!RequestUtils.hasEdits(request)) {        
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH_ADD);
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
        
        // Check store       
        Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",true);
        Store store=null;
        if (storeId!=null) {
            new StoreGetSingle().execute(request);
            store=(Store)request.getAttribute(RequestUtils.STORE);
        }
        if (store==null) {
            return false;
        }
        
        return true;
    }
}