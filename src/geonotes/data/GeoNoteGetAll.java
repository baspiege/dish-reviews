package geonotes.data;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.GeoNote;
import geonotes.utils.NumberUtils;
import geonotes.utils.RequestUtils;

/**
 * Get geo notes.
 *
 * @author Brian Spiegel
 */
public class GeoNoteGetAll {

    public static String FILTER="(latitude2Decimal==latitudeParam && longitude2Decimal==longitudeParam)";
    public static String DECLARED_PARAMETERS="double latitudeParam, double longitudeParam";

    /**
     * Get geo notes.
     *
     * @param aRequest The request
     * @since 1.0
     */
    public void execute(HttpServletRequest aRequest) {
        PersistenceManager pm=null;
        try {
            pm=PMF.get().getPersistenceManager();
            Query query=null;
            try {
                Double latitude=(Double)aRequest.getAttribute("latitude");
                Double longitude=(Double)aRequest.getAttribute("longitude");

                double latitudeCenter=NumberUtils.getNumber2DecimalPrecision(latitude);
                double longitudeCenter=NumberUtils.getNumber2DecimalPrecision(longitude);

                query = pm.newQuery(GeoNote.class);
                query.setFilter(FILTER);
                query.declareParameters(DECLARED_PARAMETERS);
                
                List<GeoNote> results = new ArrayList<GeoNote>();

                // Center                
                System.out.println("center");                
                List<GeoNote> resultsTemp = (List<GeoNote>) query.execute(latitudeCenter, longitudeCenter);
                transferResults(results,resultsTemp);

                // Left
                boolean left=false;
                if (latitude-latitudeCenter<.0025) {
                    System.out.println("left");
                    resultsTemp = (List<GeoNote>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,-.01), longitudeCenter);
                    transferResults(results,resultsTemp);
                    left=true;
                }

                // Right
                boolean right=false;
                if (latitude-latitudeCenter>.0075) {
                    System.out.println("right");
                    resultsTemp = (List<GeoNote>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,.01), longitudeCenter);
                    transferResults(results,resultsTemp);
                    right=true;
               }

                // Down
                boolean down=false;
                if (longitudeCenter-longitude<.0025) {
                    System.out.println("down");
                    resultsTemp = (List<GeoNote>) query.execute(latitudeCenter, NumberUtils.addNumber2DecimalPrecision(longitudeCenter,.01));
                    transferResults(results,resultsTemp);
                    down=true;
                }

                // Up
                boolean up=false;
                if (longitudeCenter-longitude>.0075) {
                    System.out.println("up");
                    resultsTemp = (List<GeoNote>) query.execute(latitudeCenter, NumberUtils.addNumber2DecimalPrecision(longitudeCenter,-.01));
                    transferResults(results,resultsTemp);
                    up=true;
                }

                // Corners
                if (left && up) {
                    System.out.println("left up");
                    resultsTemp = (List<GeoNote>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,-.01), NumberUtils.addNumber2DecimalPrecision(longitudeCenter,-.01));
                    transferResults(results,resultsTemp);
                } else if (left && down) {
                    System.out.println("left down");
                    resultsTemp = (List<GeoNote>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,-.01), NumberUtils.addNumber2DecimalPrecision(longitudeCenter,.01));
                    transferResults(results,resultsTemp);
                } else if (right && up) {
                    System.out.println("right up");
                    resultsTemp = (List<GeoNote>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,.01), NumberUtils.addNumber2DecimalPrecision(longitudeCenter,-.01));
                    transferResults(results,resultsTemp);
                } else if (right && down) {
                    System.out.println("right down");
                    resultsTemp = (List<GeoNote>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,.01), NumberUtils.addNumber2DecimalPrecision(longitudeCenter,.01));
                    transferResults(results,resultsTemp);
                }

                // Set into request
                aRequest.setAttribute("geoNotes", results);
            } finally {
                if (query!=null) {
                    query.closeAll();
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
     * Transfer results
     *
     * @param results results
     * @param resultsTemp results temp
     * @since 1.0
     */
    public void transferResults(List<GeoNote> results, List<GeoNote> resultsTemp) {    
        // Bug workaround.  Get size actually triggers the underlying database call.
        System.out.println( resultsTemp.size() );
        for (GeoNote note: resultsTemp) {
          results.add(note);
        }
    }
}
