package geonotes;

import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import geonotes.data.StoreGetSingle;
import geonotes.data.PMF;
import geonotes.data.model.Store;
import geonotes.utils.RequestUtils;

/**
* Return an image.
*/
public class StoreImage extends HttpServlet {

    /**
    * Process the request.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
        // Get Id.
        Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",true);

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
                        
            Store store=StoreGetSingle.getStore(request,pm,storeId.longValue());
            
            if (store!=null){
                response.setContentType("image/jpeg");
                response.getOutputStream().write(store.image.getBytes());
            }
        } catch (Exception e) {
            System.err.println(StoreImage.class.getName() + ": " + e);
            e.printStackTrace();
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}