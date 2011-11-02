package geonotes.utils;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;

/**
 * Mem cache utilities.
 *
 * @author Brian Spiegel
 */
public class MemCacheUtils
{
    public static String DISH="dish";

    /**
    * Get the dish from cache.
    *
    * @param aRequest Servlet Request
    */
    public static Dish getDish(HttpServletRequest aRequest, long aDishId)
    {
        Dish dish=null;
        MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
        dish=(Dish)memcache.get(aDishId + DISH);

        return dish;
    }

    /**
    * Set the dish into cache.
    *
    * @param aRequest Servlet Request
    * @param aDish dish
    */
    public static void setDish(HttpServletRequest aRequest, Dish aDish)
    {
        MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
        memcache.put(aDish.getKey().getId() + DISH, aDish);
    }
}
