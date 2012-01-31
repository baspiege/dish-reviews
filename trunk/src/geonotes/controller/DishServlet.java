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
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.DISH);
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
    private void setUpData(HttpServletRequest request) {
        
        // Get dish
        Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);
        Dish dish=null;
        if (dishId!=null) {
            dish=RequestUtils.getDish(dishId);
        }
        if (dish==null) {
            throw new RuntimeException("Dish not found: " + dishId);
        } else {
            request.setAttribute(RequestUtils.DISH,dish);
        }
        
        // Get store
        Store store=RequestUtils.getStore(dish.storeId);
        if (store==null) {
            throw new RuntimeException("Store not found: " + dish.storeId);
        } else {
            request.setAttribute(RequestUtils.STORE,store);
        }

        // Optional - reviewId
        Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",false);
        //if (RequestUtils.hasEdits(request)){
        // TODO - Throw exception here?
        //}
    }
}