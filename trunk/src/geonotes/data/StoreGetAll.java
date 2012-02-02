package geonotes.data;

import geonotes.data.model.Store;
import geonotes.utils.NumberUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

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
     * @param aLatitude
     * @param aLongitude
     * @since 1.0
     */
    public List<Store> execute(double aLatitude, double aLongitude) {
        PersistenceManager pm=null;
        List<Store> results=null;
        try {
            pm=PMF.get().getPersistenceManager();
            Query query=null;
            try {
                double latitudeCenter=NumberUtils.getNumber2DecimalPrecision(aLatitude);
                double longitudeCenter=NumberUtils.getNumber2DecimalPrecision(aLongitude);

                query = pm.newQuery(Store.class);
                query.setFilter(FILTER);
                query.declareParameters(DECLARED_PARAMETERS);

                results = new ArrayList<Store>();

                // Center
                System.out.println("center");
                List<Store> resultsTemp = (List<Store>) query.execute(latitudeCenter, longitudeCenter);
                transferResults(results,resultsTemp);

                boolean latNeg=(aLatitude<0);

                // Lat inc
                boolean latInc=false;
                if ((latNeg && latitudeCenter-aLatitude<.0025) ||
                   (!latNeg && aLatitude-latitudeCenter>.0075)) {
                    System.out.println("latInc");
                    resultsTemp = (List<Store>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,.01), longitudeCenter);
                    transferResults(results,resultsTemp);
                    latInc=true;
                }

                // Lat dec
                boolean latDec=false;
                if ((latNeg && latitudeCenter-aLatitude>.0075) ||
                   (!latNeg && aLatitude-latitudeCenter<.0025)) {
                    System.out.println("latDec");
                    resultsTemp = (List<Store>) query.execute(NumberUtils.addNumber2DecimalPrecision(latitudeCenter,-.01), longitudeCenter);
                    transferResults(results,resultsTemp);
                    latDec=true;
                }

                boolean longNeg=(aLongitude<0);

                // Long inc
                // Examples: rounded (center), actual, need to check, increment
                // a.) -8.00, -8.001, -7.99, +.01
                // b.) 8.00, 8.008, 8.01, +.01
                boolean longInc=false;
                if ((longNeg && longitudeCenter-aLongitude<.0025) ||
                   (!longNeg && aLongitude-longitudeCenter>.0075)) {
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
                if ((longNeg && longitudeCenter-aLongitude>.0075) ||
                   (!longNeg && aLongitude-longitudeCenter<.0025)) {
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
            } finally {
                if (query!=null) {
                    query.closeAll();
                }
            }
        } finally {
            if (pm!=null) {
                pm.close();
            }
        }
        return results;
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
