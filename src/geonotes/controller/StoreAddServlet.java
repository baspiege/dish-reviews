package geonotes.controller;

import geonotes.data.StoreAdd;
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
* Process store adds.
*/
public class StoreAddServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);

        Store store=(Store)request.getAttribute(RequestUtils.STORE);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Note
        store.setNote("");

        // Coordinates
        Double latitude=RequestUtils.getNumericInputAsDouble(request,"latitude",bundle.getString("latitudeLabel"),true);
        store.setLatitude(latitude);
        Double longitude=RequestUtils.getNumericInputAsDouble(request,"longitude",bundle.getString("longitudeLabel"),true);
        store.setLongitude(longitude);
        
        RequestUtils.forwardTo(request,response,ControllerConstants.STORE_ADD);
    }

    /**
    * Add store.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        setUpData(request);

        Store store=(Store)request.getAttribute(RequestUtils.STORE);
        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            if (action.equals(bundle.getString("addLabel"))) {		
                // Fields
                String name=RequestUtils.getAlphaInput(request,"note",bundle.getString("nameLabel"),true);
                Double latitude=RequestUtils.getNumericInputAsDouble(request,"latitude",bundle.getString("latitudeLabel"),true);
                Double longitude=RequestUtils.getNumericInputAsDouble(request,"longitude",bundle.getString("longitudeLabel"),true);
                store.setNote(name);
                store.setLatitude(latitude);
                store.setLongitude(longitude);
                if (!RequestUtils.hasEdits(request)) {
                    store=StoreAdd.execute(store);
                }
            }
        }

        // If no edits, forward to store.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("storeId",store.getKey().getId());
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_ADD);
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

        // Set store
        Store store=new Store();
        store.setUser(request.getUserPrincipal().getName());
        request.setAttribute(RequestUtils.STORE, store);
    }
}