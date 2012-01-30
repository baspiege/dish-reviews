package geonotes.data;

import geonotes.data.model.Store;
import geonotes.utils.NumberUtils;
import geonotes.utils.RequestUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

/**
 * Get stores.
 *
 * @author Brian Spiegel
 */
public class StoreGetAll {

    public static String FILTER="(latitude2Decimal==latitudeParam && longitude2Decimal==longitudeParam)";
    public static String DECLARED_PARAMETERS="double latitudeParam, double longitudeParam";

    /**
     * Get stores.
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

                query = pm.newQuery(Store.class);
                query.setFilter(FILTER);
                query.declareParameters(DECLARED_PARAMETERS);
                
                List<Store> results = new ArrayList<Store>();

                // Center                
                System.out.println("center");                
                List<Store> resultsTemp = (List<Store>) query.execute(latitudeCenter, longitudeCenter);
                transferResults(results,resultsTemp);

                boolean latNeg=(latitude<0);
                
                // Lat inc
                boolean latInc=false;
                if ((latNeg && latitudeCenter-latitude<.0025) ||
                   (!latNeg && latitude-latitudeCenter>.0075)) {
                    System.out.println("latInc");
                    resultsTemp = (List<Store>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,.01), longitudeCenter);
                    transferResults(results,resultsTemp);
                    latInc=true;
                }
                
                // Lat dec
                boolean latDec=false;
                if ((latNeg && latitudeCenter-latitude>.0075) || 
                   (!latNeg && latitude-latitudeCenter<.0025)) {
                    System.out.println("latDec");
                    resultsTemp = (List<Store>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,-.01), longitudeCenter);
                    transferResults(results,resultsTemp);
                    latDec=true;
                }

                boolean longNeg=(longitude<0);
                
                // Long inc
                // Examples: rounded (center), actual, need to check, increment
                // a.) -8.00, -8.001, -7.99, +.01
                // b.) 8.00, 8.008, 8.01, +.01
                boolean longInc=false;
                if ((longNeg && longitudeCenter-longitude<.0025) ||
                   (!longNeg && longitude-longitudeCenter>.0075)) {
                    System.out.println("longInc");
                    resultsTemp = (List<Store>) query.execute(latitudeCenter, NumberUtils.addNumber2DecimalPrecision(longitudeCenter,.01));
                    transferResults(results,resultsTemp);
                    longInc=true;
                }

                // Long dec
                // Examples: rounded (center), actual, need to check, increment
                // a.) -8.00, -8.008, -8.01, -.01
                // b.) 8.00, 8.001, 7.99, -.01
                boolean longDec=false;
                if ((longNeg && longitudeCenter-longitude>.0075) || 
                   (!longNeg && longitude-longitudeCenter<.0025)) {
                    System.out.println("longDec");
                    resultsTemp = (List<Store>) query.execute(latitudeCenter, NumberUtils.addNumber2DecimalPrecision(longitudeCenter,-.01));
                    transferResults(results,resultsTemp);
                    longDec=true;
                }

                // Corners
                if (latDec && longDec) {
                    System.out.println("latDec longDec");
                    resultsTemp = (List<Store>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,-.01), NumberUtils.addNumber2DecimalPrecision(longitudeCenter,-.01));
                    transferResults(results,resultsTemp);
                } else if (latDec && longInc) {
                    System.out.println("latDec longInc");
                    resultsTemp = (List<Store>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,-.01), NumberUtils.addNumber2DecimalPrecision(longitudeCenter,.01));
                    transferResults(results,resultsTemp);
                } else if (latInc && longDec) {
                    System.out.println("latInc longDec");
                    resultsTemp = (List<Store>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,.01), NumberUtils.addNumber2DecimalPrecision(longitudeCenter,-.01));
                    transferResults(results,resultsTemp);
                } else if (latInc && longInc) {
                    System.out.println("latInc longInc");
                    resultsTemp = (List<Store>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,.01), NumberUtils.addNumber2DecimalPrecision(longitudeCenter,.01));
                    transferResults(results,resultsTemp);
                }

                // Set into request
                aRequest.setAttribute("stores", results);
            } finally {
                if (query!=null) {
                    query.closeAll();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    public void transferResults(List<Store> results, List<Store> resultsTemp) {    
        // Bug workaround.  Get size actually triggers the underlying database call.
        System.out.println( resultsTemp.size() );
        for (Store note: resultsTemp) {
          results.add(note);
        }
    }
}
