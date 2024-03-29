package geonotes.controller;

import geonotes.data.StoreDelete;
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
public class StoreUpdateServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.STORE_UPDATE);
    }

    /**
    * Update or delete Store.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        Store store=(Store)request.getAttribute(RequestUtils.STORE);
        String action=RequestUtils.getAlphaInput(request,"action","Action",true);
        ResourceBundle bundle = ResourceBundle.getBundle("Text");

        // Process based on action
        if (!StringUtils.isEmpty(action)) {
            if (action.equals(bundle.getString("updateLabel"))) {		
                String name=RequestUtils.getAlphaInput(request,"note",bundle.getString("nameLabel"),true);
                store.setNote(name);
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
        Store store=(Store)request.getAttribute(RequestUtils.STORE);
        if (!RequestUtils.hasEdits(request)) {
            store=StoreUpdate.execute(store);
        }
        // If no edits, forward to store.
        if (!RequestUtils.hasEdits(request)) {
            request.setAttribute("storeId",store.getKey().getId());
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_UPDATE);
        }
    }

    /**
    * Delete action.
    */
    private void deleteAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Store store=(Store)request.getAttribute(RequestUtils.STORE);
        if (store.getDishCount()>0) {
            RequestUtils.addEditUsingKey(request,"storesWithDishesCantBeDeletedEditMessage");
        }
        if (!RequestUtils.hasEdits(request)) {
            StoreDelete.execute(store);
        }
        // If no edits, forward to stores.
        if (!RequestUtils.hasEdits(request)) {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORES_REDIRECT);
        } else {
            RequestUtils.forwardTo(request,response,ControllerConstants.STORE_UPDATE);
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

        // Get Store
        Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",true);
        Store store=null;
        if (storeId!=null) {
            store=MemCacheUtils.getStore(storeId);
        }
        if (store==null) {
            throw new RuntimeException("Store not found");
        }
        store.setUser(request.getUserPrincipal().getName());
        request.setAttribute(RequestUtils.STORE, store);
    }
}