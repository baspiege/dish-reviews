package geonotes.utils;

import geonotes.data.DishGetSingle;
import geonotes.data.StoreGetSingle;
import geonotes.data.model.Dish;
import geonotes.data.model.Store;
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
    * @return a dish
    */
    public static Dish getDish(long aDishId) {
        MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
        Dish dish=(Dish)memcache.get(aDishId + DISH);
        if (dish==null) {
            dish=new DishGetSingle().execute(aDishId);
            MemCacheUtils.setDish(dish);
        }
        return dish;
    }

    /**
    * Get the store from cache.
    *
    * @param aStoreId
    * @return a store
    */
    public static Store getStore(long aStoreId) {
        MemcacheService memcache=MemcacheServiceFactory.getMemcacheService();
        Store store=(Store)memcache.get(aStoreId + STORE);
        if (store==null) {
            store=new StoreGetSingle().execute(aStoreId);
            MemCacheUtils.setStore(store);
        }
        return store;
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
