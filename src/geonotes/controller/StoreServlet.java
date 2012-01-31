package geonotes.controller;

import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Show store.
*/
public class StoreServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.STORE);
    }
    
    /**
    * No post for now.  Route to main page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestUtils.forwardTo(request,response,ControllerConstants.STORE_REDIRECT);
    }    
    
    /**
    * Set-up the data.
    */
    private void setUpData(HttpServletRequest request) {
        
        // Get store
        Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",true);
        Store store=null;
        if (storeId!=null) {
            store=RequestUtils.getStore(storeId);
            request.setAttribute(RequestUtils.STORE,store);
        }
        if (store==null) {
            throw new RuntimeException("Store not found: " + storeId);
        }
    }
}