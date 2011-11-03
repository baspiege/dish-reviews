package geonotes.utils;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import geonotes.data.model.Dish;
import geonotes.data.model.Store;

/**
 * Mem cache utilities.
 *
 * @author Brian Spiegel
 */
public class MemCacheUtils
{
    public static String DISH="dish";
    public static String STORE="store";

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
    * Get the store from cache.
    *
    * @param aRequest Servlet Request
    */
    public static Store getStore(HttpServletRequest aRequest, long aStoreId)
    {
        Store store=null;
        MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
        store=(Store)memcache.get(aStoreId + STORE);

        return store;
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
    
    /**
    * Set the store into cache.
    *
    * @param aRequest Servlet Request
    * @param aStore store
    */
    public static void setStore(HttpServletRequest aRequest, Store aStore)
    {
        MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
        memcache.put(aStore.getKey().getId() + STORE, aStore);
    }
}
