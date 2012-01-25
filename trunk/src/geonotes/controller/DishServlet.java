package geonotes.controller;

import geonotes.data.model.Dish;
import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Show dish.
*/
public class DishServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!setUpData(request)) {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.DISH);
        }
    }
    
    /**
    * No post for now.  Route to main page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestUtils.forwardTo(request,response,ControllerConstants.STORE_REDIRECT);
    }    
    
    /**
    * Set-up the data.
    *
    * @return a boolean indiciating success or failure.
    */
    private boolean setUpData(HttpServletRequest request) {
        
        // Get dish and store
        Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);
        Dish dish=null;
        Store store=null;
        if (dishId!=null) {
            dish=RequestUtils.getDish(request,dishId);
            store=RequestUtils.getStore(request,dish.storeId);
        }
        if (dish==null || store==null) {
            return false;
        }

        // Optional - reviewId
        Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",false);
        if (RequestUtils.hasEdits(request)){
            return false;
        }
    
        return true;
    }
}