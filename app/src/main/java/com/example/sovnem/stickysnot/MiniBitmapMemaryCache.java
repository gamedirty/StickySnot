package com.example.sovnem.stickysnot;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 缓存需要绘制的bitmap 和bitmap爆炸时候的爆炸对象
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
    private LruCache<String, Object> cache;

    public MiniBitmapMemaryCache() {
        cache = new LruCache<String, Object>(MAX_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Object value) {

                if (value instanceof Bitmap) {
                    return ((Bitmap) value).getWidth() * ((Bitmap) value).getHeight();
                } else {
                    Bitmap[] bms = (Bitmap[]) value;
                    int sum = 0;
                    for (Bitmap b : bms) {
                        sum += b.getWidth() * b.getHeight();
                    }
                    return sum;
                }

            }
        };
    }

    public void put(String key, Bitmap value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }
}
