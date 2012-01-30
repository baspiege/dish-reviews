package geonotes.utils;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import geonotes.data.model.Dish;
import geonotes.data.model.Store;

/**
 * Mem cache utilities.
 *
 * @author Brian Spiegel
 */
public class MemCacheUtils {

    private static String DISH="dish";
    private static String STORE="store";

    /**
    * Get the dish from cache.
    *
    * @param aDishId dish Id
    */
    public static Dish getDish(long aDishId) {
        MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
        return (Dish)memcache.get(aDishId + DISH);
    }
    
    /**
    * Get the store from cache.
    *
    * @param aStoreId
    */
    public static Store getStore(long aStoreId) {
        MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
        return (Store)memcache.get(aStoreId + STORE);
    }

    /**
    * Set the dish into cache.
    *
    * @param aDish dish
    */
    public static void setDish(Dish aDish) {
        if (aDish!=null) {
            MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
            memcache.put(aDish.getKey().getId() + DISH, aDish);
        }
    }
    
    /**
    * Set the store into cache.
    *
    * @param aStore store
    */
    public static void setStore(Store aStore) {
        if (aStore!=null) {
            MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
            memcache.put(aStore.getKey().getId() + STORE, aStore);
        }
    }
}
