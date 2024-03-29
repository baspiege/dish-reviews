package geonotes.controller;

import geonotes.data.StoreUpdate;
import geonotes.data.model.Store;
import geonotes.utils.MemCacheUtils;
import geonotes.utils.RequestUtils;
import geonotes.utils.StringUtils;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Process Store updates.
*/
public class StoreUpdateLocationServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.STORE_UPDATE_LOCATION);
    }

    /**
    * Update or delete Store.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        
        // Check if signed in
        boolean isSignedIn=request.getUserPrincipal().getName()!=null;
        if (!isSignedIn) {
            throw new SecurityException("User principal not found");
        }
        
        Store store=(Store)request.getAttribute(RequestUtils.STORE);
        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            if (action.equals(bundle.getString("updateLabel"))) {		
                // Fields
                Double latitude=RequestUtils.getNumericInputAsDouble(request,"latitude",bundle.getString("latitudeLabel"),true);
                Double longitude=RequestUtils.getNumericInputAsDouble(request,"longitude",bundle.getString("longitudeLabel"),true);
                store.setLatitude(latitude);
                store.setLongitude(longitude);
                if (!RequestUtils.hasEdits(request)) {
                    store=StoreUpdate.execute(store);
                }
            }
        }

        // If no edits, forward to dish.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("storeId",store.getKey().getId());
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_UPDATE_LOCATION);
        }
    }

    /**
    * Set-up the data.
    */
    private void setUpData(HttpServletRequest request) {

        // Get Store
        Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",true);
        Store store=null;
        if (storeId!=null) {
            store=MemCacheUtils.getStore(storeId);
        }
        if (store==null) {
            throw new RuntimeException("Store not found: " + storeId);
        }
        store.setUser(request.getUserPrincipal().getName());
        request.setAttribute(RequestUtils.STORE, store);
    }
}