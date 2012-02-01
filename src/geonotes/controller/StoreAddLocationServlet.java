package geonotes.controller;

import geonotes.utils.RequestUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Process store add location.
*/
public class StoreAddLocationServlet extends HttpServlet {

    /**
    * Display page.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.STORE_ADD_LOCATION);
    }
    
    /**
    * Display page.
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUpData(request);
        RequestUtils.forwardTo(request,response,ControllerConstants.STORE_ADD_LOCATION);
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
    }
}