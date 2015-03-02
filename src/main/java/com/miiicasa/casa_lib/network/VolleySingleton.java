package com.miiicasa.casa_lib.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.miiicasa.Config;
import com.miiicasa.casa_lib.utils.Logger;

import java.io.File;

/**
 * Created by showsky on 15/3/2.
 */
public class VolleySingleton {

    private final static String TAG = VolleySingleton.class.getSimpleName();
    public final static String DIRECTORY_NAME = "volley";
    public final static String PREFIX = "#W0#H0";    //NOTICE: This is Volley memory cache key prefix
    private static VolleySingleton instance = null;
    private RequestQueue mRequestQueue = null;
    private ImageLoader mImageLoader = null;
    private MemoryCache memoryCache = null;

    private VolleySingleton(Context context) {
        File cacheDir = new File(context.getCacheDir(), DIRECTORY_NAME);
        mRequestQueue = new RequestQueue(
            new DiskBasedCache(cacheDir, Config.STORAGE_CACHE_SIZE),
            new BasicNetwork(new OkHttpStack())
        );
        mRequestQueue.start();
        memoryCache = new MemoryCache();
        mImageLoader = new ImageLoader(this.mRequestQueue, memoryCache);
    }

    public static VolleySingleton getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }

    public MemoryCache getMemoryCache() {
        return memoryCache;
    }

    public void removeCache(String url) {
        memoryCache.remove(VolleySingleton.PREFIX + url);
        mRequestQueue.getCache().remove(url);
    }

    public void deleteAllCache() {
        Logger.w(TAG, "Delete all cache");
        memoryCache.evictAll();
        getRequestQueue().getCache().clear();
    }

    public static class MemoryCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

        public MemoryCache() {
            super(Config.MEMORY_CACHE_SIZE);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return 1;
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
    }
}
