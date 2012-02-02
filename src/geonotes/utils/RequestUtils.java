package geonotes.utils;

import geonotes.data.DishGetSingle;
import geonotes.data.StoreGetSingle;
import geonotes.data.model.Dish;
import geonotes.data.model.Store;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Request utilities.
 *
 * @author Brian Spiegel
 */
public class RequestUtils {

    public static String EDITS="edits";
    public static String DISH="dish";
    public static String STORE="store";
    public static String REVIEW="review";

    // These are thread-safe.
    private static Pattern mNumbersPattern=Pattern.compile("[-]?[\\d]*[\\.]?[\\d]*");

    /**
    * Add edit.
    *
    * @param aRequest Servlet Request
    * @param aEditMessage edit message
    */
    public static void addEdit(HttpServletRequest aRequest, String aEditMessage) {
        getEdits(aRequest).add(aEditMessage);
    }

    /**
    * Add edit.
    *
    * @param aRequest Servlet Request
    * @param aKey key in Text ResourceBundle
    */
    public static void addEditUsingKey(HttpServletRequest aRequest, String aKey) {
        ResourceBundle bundle = ResourceBundle.getBundle("Text");
        getEdits(aRequest).add(bundle.getString(aKey));
    }

    /**
    * Get a String input and store into the request if there are no edits.
    *
    * @param aRequest Servlet Request to get input from
    * @param aFieldToCheck Field to check
    * @param aDescription Description of field for edit message
    * @param aRequired Indicates if required
    * @return the field if no edits
    */
    public static String getAlphaInput(HttpServletRequest aRequest, String aFieldToCheck, String aDescription, boolean aRequired) {
        String value=aRequest.getParameter(aFieldToCheck);
        if (isFieldEmpty(aRequest, value, aFieldToCheck, aDescription, aRequired)) {
            value="";
        } else if (value.length()>500) {
            value=value.substring(0,500);
            ResourceBundle bundle = ResourceBundle.getBundle("Text");
            String editMessage=aDescription + ": " + bundle.getString("alphaFieldMaxLengthEdit");
            addEdit(aRequest,editMessage);
        } else {
            value=value.trim();
        }

        return value;
    }

    /**
    * Get dish.
    *
    * @param aDishId dish Id
    * @return a dish
    */
    public static Dish getDish(long aDishId) {
        Dish dish=null;
        // Try cache.
        dish=MemCacheUtils.getDish(aDishId);
        if (dish==null) {
            dish=new DishGetSingle().execute(aDishId);
            MemCacheUtils.setDish(dish);
        }
        return dish;
    }

    /**
    * Get the edits.
    *
    * @return a list of edits
    */
    public static List<String> getEdits(HttpServletRequest aRequest) {
        List<String> edits=(List<String>)aRequest.getAttribute(EDITS);
        if (edits==null) {
            edits=new ArrayList<String>();
            aRequest.setAttribute(EDITS,edits);
        }

        return edits;
    }

    /**
    * Get a numeric input and store into the request if there are no edits.
    *
    * @param aRequest Servlet Request to get input from
    * @param aFieldToCheck Field to check
    * @param aDescription Description of field for edit message
    * @param aRequired Indicates if required
    * @return the field if no edits
    */
    public static Long getNumericInput(HttpServletRequest aRequest, String aFieldToCheck, String aDescription, boolean aRequired) {
        Long retValue=null;
        String value=aRequest.getParameter(aFieldToCheck);
        if (isFieldEmpty(aRequest, value, aFieldToCheck, aDescription, aRequired)) {
            // Do nothing
        } else if (!mNumbersPattern.matcher(value).matches()) {
            retValue=null;

            ResourceBundle bundle = ResourceBundle.getBundle("Text");
            String editMessage=aDescription + ": " + bundle.getString("numberFieldValidCharsEdit");
            addEdit(aRequest,editMessage);
        } else {
            try {
                retValue=new Long(value);
            } catch (NumberFormatException e) {
                retValue=null;
                ResourceBundle bundle = ResourceBundle.getBundle("Text");
                String editMessage=aDescription + ": " + bundle.getString("numberFieldNotValidEdit");
                addEdit(aRequest,editMessage);
            }
        }

        return retValue;
    }

    /**
    * Get a numeric input and store into the request if there are no edits.
    *
    * @param aRequest Servlet Request to get input from
    * @param aFieldToCheck Field to check
    * @param aDescription Description of field for edit message
    * @param aRequired Indicates if required
    * @return the field if no edits
    */
    public static Double getNumericInputAsDouble(HttpServletRequest aRequest, String aFieldToCheck, String aDescription, boolean aRequired) {
        Double retValue=null;
        String value=aRequest.getParameter(aFieldToCheck);
        if (isFieldEmpty(aRequest, value, aFieldToCheck, aDescription, aRequired)) {
            // Do nothing
        } else if (!mNumbersPattern.matcher(value).matches()) {
            retValue=null;
            ResourceBundle bundle = ResourceBundle.getBundle("Text");
            String editMessage=aDescription + ": " + bundle.getString("numberFieldValidCharsEdit");
            addEdit(aRequest,editMessage);
        } else {
            try {
                retValue=new Double(value);
            } catch (NumberFormatException e) {
                retValue=null;
                ResourceBundle bundle = ResourceBundle.getBundle("Text");
                String editMessage=aDescription + ": " + bundle.getString("numberFieldNotValidEdit");
                addEdit(aRequest,editMessage);
            }
        }

        return retValue;
    }

    /**
    * Get store.
    *
    * @param aStoreId store Id
    * @return a store
    */
    public static Store getStore(long aStoreId) {
        Store store=null;
        // Try cache.
        store=MemCacheUtils.getStore(aStoreId);
        if (store==null) {
            store=new StoreGetSingle().execute(aStoreId);
            MemCacheUtils.setStore(store);
        }
        return store;
    }

   /**
    * Forward to.
    *
    * @param aRequest Servlet Request
    * @param aResponse Servlet Response
    */
    public static void forwardTo(HttpServletRequest aRequest, HttpServletResponse aResponse, String target) throws IOException, ServletException {
        RequestDispatcher rd=aRequest.getRequestDispatcher(target);
        rd.forward(aRequest, aResponse);
    }

    /**
    * Has edits.
    *
    * @param aRequest Servlet Request
    * @return a boolean indicating if there are edits
    */
    public static boolean hasEdits(HttpServletRequest aRequest) {
        boolean hasEdits=false;
        List<String> edits=(List<String>)aRequest.getAttribute(EDITS);
        if (edits!=null && edits.size()>0) {
            hasEdits=true;
        }

        return hasEdits;
    }

    /**
    * Check if empty.  If required, create an edit.
    *
    * @param aRequest Servlet Request to get input from
    * @param aValue value to check
    * @param aDescription Description of field for edit message
    * @param aRequired Indicates if required
    * @return a boolean indicating if the field is empty
    */
    private static boolean isFieldEmpty(HttpServletRequest aRequest, String aValue, String aFieldToCheck, String aDescription, boolean aRequired) {
        boolean isEmpty=false;

        if (aValue==null || aValue.trim().length()==0) {
            isEmpty=true;
            if (aRequired) {
                ResourceBundle bundle = ResourceBundle.getBundle("Text");
                String editMessage=aDescription + ": " + bundle.getString("fieldRequiredEdit");
                addEdit(aRequest,editMessage);
            }
        }

        return isEmpty;
    }

    /**
    * Remove edits.
    *
    * @param aRequest Servlet Request
    */
    public static void removeEdits(HttpServletRequest aRequest) {
        List<String> edits=(List<String>)aRequest.getAttribute(EDITS);
        if (edits!=null && edits.size()>0) {
            edits.clear();
        }
    }
}
