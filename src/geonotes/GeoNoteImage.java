package geonotes;

import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import geonotes.data.GeoNoteGetSingle;
import geonotes.data.PMF;
import geonotes.data.model.GeoNote;
import geonotes.utils.RequestUtils;

/**
* Return an image.
*/
public class GeoNoteImage extends HttpServlet {

    /**
    * Process the request.
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
        // Get Id.
        Long geoNoteId=RequestUtils.getNumericInput(request,"id","id",true);

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
                        
            GeoNote geoNote=GeoNoteGetSingle.getGeoNote(request,pm,geoNoteId.longValue());
            
            if (geoNote!=null){
                response.setContentType("image/jpeg");
                response.getOutputStream().write(geoNote.image.getBytes());
            }
        } catch (Exception e) {
            System.err.println(GeoNoteImage.class.getName() + ": " + e);
            e.printStackTrace();
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
}