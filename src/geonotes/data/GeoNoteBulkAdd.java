package geonotes.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.GeoNote;
import geonotes.utils.RequestUtils;

/**
 * Add notes.
 *
 * @author Brian Spiegel
 */
public class GeoNoteBulkAdd {

    /**
     * Add notes.
     *
     * @param aRequest The request
     *
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        String notesInput=(String)aRequest.getAttribute("notes");

        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();

            // Split input
            String notes[] = notesInput.split("\\r?\\n");
            DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
            for (int i=0;i<notes.length;i++) {
                try {
                    String parts[] = notes[i].split(",");
                    GeoNote geoNote=new GeoNote();

                    // If parts > 1
                    if (parts.length>3) {
                        // First is date.
                        geoNote.setLastUpdateTime((Date)formatter.parse(parts[0]));
                        // Second is type.
                        String typeString=(String)parts[1];
                        long type=getTypeBasedOnLabel(typeString);
                        geoNote.setType(type);
                        // Special types
                        if (parts.length==6){
                            // Example: 08/10/2011,Graffiti Removal,Brick - Painted,Front,41.93571207328131,-87.70261319521063                           
                            geoNote.setNote(parts[2] + " " + parts[3]);
                            geoNote.setLatitude(new Double(parts[4]).doubleValue());
                            geoNote.setLongitude(new Double(parts[5]).doubleValue());
                        } else if (parts.length==5) {
                            // Example: 08/10/2011,Tree Debris,Alley,41.93571207328131,-87.70261319521063                           
                            geoNote.setNote(parts[2]);
                            geoNote.setLatitude(new Double(parts[3]).doubleValue());
                            geoNote.setLongitude(new Double(parts[4]).doubleValue());
                        } else if (parts.length==4) {
                            // Example: 08/10/2011,Pot Hole in Street,41.93571207328131,-87.70261319521063                           
                            geoNote.setLatitude(new Double(parts[2]).doubleValue());
                            geoNote.setLongitude(new Double(parts[3]).doubleValue());
                        } else {
                            throw new RuntimeException("Parts not valid: " + notes[i]);
                        }
                        geoNote.setYes(0);
                        geoNote.setUser("CityDataPortal");
                        pm.makePersistent(geoNote);
                    }
                } catch (Exception e) {
                        System.err.println("Error processing: " + notes[i] + ": " + e);
                }
            }
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": " + e);
            e.printStackTrace();
            RequestUtils.addEditUsingKey(aRequest,"requestNotProcessedEditMsssage");
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
    }
    
    /**
     * Get the type based on the label.
     *
     * @param aType Label of type
     * @return an id of the type
     * @since 1.0
     */
    public long getTypeBasedOnLabel(String aType) {
        long type=0;
        
        /*
        type_2=Garbage Cart
        type_3=Graffiti Removal
        type_4=Pothole
        type_5=Rodent Baiting
        type_6=Sanitation Code Complaint
        type_7=Street Lights
        type_8=Tree Debris
        */
        
        if (aType.equalsIgnoreCase("Garbage Cart Black Maintenance")) {
            type=2;
        } else if (aType.equalsIgnoreCase("Graffiti Removal")) {
            type=3;        
        } else if (aType.equalsIgnoreCase("Pot Hole in Street")) {
            type=4;
        } else if (aType.equalsIgnoreCase("Rodent Baiting/Rat Complaint")) {
            type=5;
        } else if (aType.equalsIgnoreCase("Sanitation Code Violation")) {
            type=6;
        } else if (aType.equalsIgnoreCase("Street Lights - All/Out")) {
            type=7;
        } else if (aType.equalsIgnoreCase("Tree Debris")) {
            type=8;
        } else {
            throw new RuntimeException("Type not valid: " + type);
        }
        
        return type;
    }
}
