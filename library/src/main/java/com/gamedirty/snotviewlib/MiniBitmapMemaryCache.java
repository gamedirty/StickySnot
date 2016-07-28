package com.gamedirty.snotviewlib;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.lang.ref.WeakReference;

/**
 * Created by sovnem on 16/6/5,00:34.
 */
public class MiniBitmapMemaryCache {
    /**
     * The size of cache
     * bitmap缓存的大小
     */
    private static final int MAX_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 20);
    /**
     * Cache for bitmaps to be drawn.the key is the resource id of the view ,and the value is the bitmap which is created from view;
     */
    private LruCache<String, WeakReference<Bitmap>> cache;

    public MiniBitmapMemaryCache() {
        cache = new LruCache<String, WeakReference<Bitmap>>(MAX_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, WeakReference<Bitmap> bm) {
                Bitmap value = bm.get();
                return value.getWidth() * value.getHeight();
            }
        };
    }

    public void put(String key, Bitmap value) {
        cache.put(key, new WeakReference<>(value));
    }

    public Bitmap get(String key) {
        return cache.get(key).get();
    }
}
