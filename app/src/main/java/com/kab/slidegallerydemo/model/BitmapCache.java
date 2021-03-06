package com.kab.slidegallerydemo.model;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by Suxx on 01.06.2016.
 */
public class BitmapCache {
    private static LruCache<String, Bitmap> mMemoryCache;
    public BitmapCache() {
// Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
            Log.e("mMemoryCache", "mMemoryCache_is_null");
        }
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null&&bitmap != null ) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public  void clearBitmapToMemoryCache() {
            mMemoryCache.evictAll();
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


}
