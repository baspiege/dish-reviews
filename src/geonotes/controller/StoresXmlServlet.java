package geonotes.controller;

import geonotes.data.StoreGetAll;
import geonotes.data.model.Store;
import geonotes.utils.MemCacheUtils;
import geonotes.utils.RequestUtils;
import geonotes.view.xml.StoresXml;
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
             Store store=MemCacheUtils.getStore(storeId);
             // Add to list.
             stores=new ArrayList<Store>();
             stores.add(store);
        } else {
            Double latitude=RequestUtils.getNumericInputAsDouble(request,"latitude",bundle.getString("latitudeLabel"),true);
            Double longitude=RequestUtils.getNumericInputAsDouble(request,"longitude",bundle.getString("longitudeLabel"),true);
            stores=StoreGetAll.execute(latitude,longitude);
        }
        
        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
        RequestUtils.setNoCacheHeaders(response);
        StoresXml.outputXml(stores,response.getOutputStream());
    }

    /**
    * No post for now.  Route to main page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
    }
}