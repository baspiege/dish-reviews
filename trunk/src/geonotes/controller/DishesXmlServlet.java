package geonotes.controller;

import geonotes.data.DishesGetAll;
import geonotes.data.model.Dish;
import geonotes.utils.RequestUtils;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Get dishes in XML format.
*/
public class DishesXmlServlet extends HttpServlet {

    /**
    * Get data.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("Text");        
        
        // Fields
        RequestUtils.getNumericInput(request,"start",bundle.getString("startLabel"),true);
        RequestUtils.getNumericInput(request,"storeId",bundle.getString("storeId"),true);
        RequestUtils.getAlphaInput(request,"sortBy",bundle.getString("sortByLabel"),false);
        
        // Get data
        new DishesGetAll().execute(request);

        RequestUtils.forwardTo(request,response,ControllerConstants.DISHES_XML);
    }
    
    /**
    * No post for now.  Route to main page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
    }    
}