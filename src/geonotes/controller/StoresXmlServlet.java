package geonotes.controller;

import geonotes.data.StoreGetAll;
import geonotes.data.StoreGetSingle;
import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Get stores in XML format.
*/
public class StoresXmlServlet extends HttpServlet {

    /**
    * Get data.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        ResourceBundle bundle = ResourceBundle.getBundle("Text");
        List<Store> stores = null;
        
        Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",false);
        if (storeId!=null) {
             Store store=new StoreGetSingle().execute(storeId);
             // Add to list.
             stores=new ArrayList<Store>();
             stores.add(store);
        } else { 
            Double latitude=RequestUtils.getNumericInputAsDouble(request,"latitude",bundle.getString("latitudeLabel"),true);
            Double longitude=RequestUtils.getNumericInputAsDouble(request,"longitude",bundle.getString("longitudeLabel"),true);
            stores=new StoreGetAll().execute(latitude,longitude);
        }
        request.setAttribute("stores", stores);

        RequestUtils.forwardTo(request,response,ControllerConstants.STORES_XML);
    }
    
    /**
    * No post for now.  Route to main page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
    }    
}