package geonotes.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import geonotes.data.DishGetSingle;
import geonotes.data.StoreGetSingle;
import geonotes.data.model.Dish;
import geonotes.data.model.Store;

/**
 * Request utilities.
 *
 * @author Brian Spiegel
 */
public class RequestUtils
{
    public static String FORWARDED="forwarded";
    public static String EDITS="edits";
    public static String DISH="dish";
    public static String STORE="store";

    // These are thread-safe.
    private static Pattern mNumbersPattern=Pattern.compile("[-]?[\\d]*[\\.]?[\\d]*");
    
    /**
    * Add edit.
    *
    * @param aRequest Servlet Request
    * @param aEditMessage edit message
    */
    public static void addEdit(HttpServletRequest aRequest, String aEditMessage)
    {
        getEdits(aRequest).add(aEditMessage);
    }

    /**
    * Add edit.
    *
    * @param aRequest Servlet Request
    * @param aKey key in Text ResourceBundle
    */
    public static void addEditUsingKey(HttpServletRequest aRequest, String aKey)
    {
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
    public static String getAlphaInput(HttpServletRequest aRequest, String aFieldToCheck, String aDescription, boolean aRequired)
    {
        String value=aRequest.getParameter(aFieldToCheck);
        if (isFieldEmpty(aRequest, value, aFieldToCheck, aDescription, aRequired))
        {
            value="";
            aRequest.setAttribute(aFieldToCheck,value);
        }
        else if (value.length()>500)
        {
            value=value.substring(0,500);
            aRequest.setAttribute(aFieldToCheck,value);

            ResourceBundle bundle = ResourceBundle.getBundle("Text");
            String editMessage=aDescription + ": " + bundle.getString("alphaFieldMaxLengthEdit");
            addEdit(aRequest,editMessage);
        }
        else
        {
            value=value.trim();
            aRequest.setAttribute(aFieldToCheck,value);
        }

        return value;
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
    public static String getBulkAlphaInput(HttpServletRequest aRequest, String aFieldToCheck, String aDescription, boolean aRequired)
    {
        String value=aRequest.getParameter(aFieldToCheck);
        if (isFieldEmpty(aRequest, value, aFieldToCheck, aDescription, aRequired))
        {
            value="";
            aRequest.setAttribute(aFieldToCheck,value);
        }
        else
        {
            value=value.trim();
            aRequest.setAttribute(aFieldToCheck,value);
        }

        return value;
    }
    
    /**
    * Get dish.
    *
    * @param aRequest Servlet Request
    */
    public static Dish getDish(HttpServletRequest aRequest, long aDishId)
    {
        Dish dish=null;
        
        // Try cache.
        dish=MemCacheUtils.getDish(aRequest, aDishId);
        if (dish!=null)
        {
            // Set into request.
            aRequest.setAttribute(DISH,dish);
        }
        else
        {
            // Get from the datastore which sets into the request.
            // And put into the cache.
            aRequest.setAttribute("dishId",aDishId);
            new DishGetSingle().execute(aRequest);
            dish=(Dish)aRequest.getAttribute(DISH);
            MemCacheUtils.setDish(aRequest,dish);
        }

        return dish;
    }
    
    /**
    * Get the edits.
    *
    * @return a list of edits
    */
    public static List<String> getEdits(HttpServletRequest aRequest)
    {
        List<String> edits=(List<String>)aRequest.getAttribute(EDITS);
        if (edits==null)
        {
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
    public static Long getNumericInput(HttpServletRequest aRequest, String aFieldToCheck, String aDescription, boolean aRequired)
    {
        Long retValue=null;
        String value=aRequest.getParameter(aFieldToCheck);
        if (isFieldEmpty(aRequest, value, aFieldToCheck, aDescription, aRequired))
        {
            // Do nothing
        }
        else if (!mNumbersPattern.matcher(value).matches())
        {
            retValue=null;

            ResourceBundle bundle = ResourceBundle.getBundle("Text");
            String editMessage=aDescription + ": " + bundle.getString("numberFieldValidCharsEdit");
            addEdit(aRequest,editMessage);
        }
        else
        {
            try
            {
                retValue=new Long(value);
                aRequest.setAttribute(aFieldToCheck,retValue);
            }
            catch (NumberFormatException e)
            {
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
    public static Double getNumericInputAsDouble(HttpServletRequest aRequest, String aFieldToCheck, String aDescription, boolean aRequired)
    {
        Double retValue=null;
        String value=aRequest.getParameter(aFieldToCheck);
        if (isFieldEmpty(aRequest, value, aFieldToCheck, aDescription, aRequired))
        {
            // Do nothing
        }
        else if (!mNumbersPattern.matcher(value).matches())
        {
            retValue=null;

            ResourceBundle bundle = ResourceBundle.getBundle("Text");
            String editMessage=aDescription + ": " + bundle.getString("numberFieldValidCharsEdit");
            addEdit(aRequest,editMessage);
        }
        else
        {
            try
            {
                retValue=new Double(value);
                aRequest.setAttribute(aFieldToCheck,retValue);
            }
            catch (NumberFormatException e)
            {
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
    * @param aRequest Servlet Request
    */
    public static Store getStore(HttpServletRequest aRequest, long aStoreId)
    {
        Store store=null;
        
        // Try cache.
        store=MemCacheUtils.getStore(aRequest, aStoreId);
        if (store!=null)
        {
            // Set into request.
            aRequest.setAttribute(STORE,store);
        }
        else
        {
            // Get from the datastore which sets into the request.
            // And put into the cache.
            aRequest.setAttribute("storeId",aStoreId);
            new StoreGetSingle().execute(aRequest);
            store=(Store)aRequest.getAttribute(STORE);
            MemCacheUtils.setStore(aRequest,store);
        }

        return store;
    }
    
    /**
    * Has edits.
    *
    * @param aRequest Servlet Request
    * @return a boolean indicating if there are edits
    */
    public static boolean hasEdits(HttpServletRequest aRequest)
    {
        boolean hasEdits=false;
        List<String> edits=(List<String>)aRequest.getAttribute(EDITS);
        if (edits!=null && edits.size()>0)
        {
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
    private static boolean isFieldEmpty(HttpServletRequest aRequest, String aValue, String aFieldToCheck, String aDescription, boolean aRequired)
    {
        boolean isEmpty=false;

        if (aValue==null || aValue.trim().length()==0)
        {
            isEmpty=true;

            if (aRequired)
            {
                ResourceBundle bundle = ResourceBundle.getBundle("Text");
                String editMessage=aDescription + ": " + bundle.getString("fieldRequiredEdit");
                addEdit(aRequest,editMessage);
            }
            else
            {
                aRequest.setAttribute(aFieldToCheck,null);
            }
        }

        return isEmpty;
    }
    
    /**
    * Check if forwarded.
    *
    * @param aRequest Servlet Request
    */
    public static boolean isForwarded(HttpServletRequest aRequest)
    {
        Boolean value=(Boolean)aRequest.getAttribute(FORWARDED);

        if (value!=null && value.booleanValue())
        {
            return true;
        }
        return false;
    }
    
    /**
    * Remove edits.
    *
    * @param aRequest Servlet Request
    */
    public static void removeEdits(HttpServletRequest aRequest)
    {
        List<String> edits=(List<String>)aRequest.getAttribute(EDITS);
        if (edits!=null && edits.size()>0)
        {
            edits.clear();
        }
    }

    
    /**
    * Reset action.
    *
    * @param aRequest Servlet Request
    */
    public static void resetAction(HttpServletRequest aRequest)
    {
        aRequest.setAttribute(FORWARDED,new Boolean(true));
    }
}
