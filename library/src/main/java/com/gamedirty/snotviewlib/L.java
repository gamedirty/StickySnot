package com.gamedirty.snotviewlib;

import android.util.Log;

/**
 * Created by sovnem on 16/6/19,11:08,23:04.
 */
@SuppressWarnings({"ALL", "WeakerAccess"})
public class L {

    private static final boolean isDebug = true;


    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void i(String msg) {
        if (isDebug) Log.i("zhjh", msg);
    }

    public static void s(String msg, int x, int y) {
        if (isDebug) Log.i("zhjh", msg + "(" + x + "," + y + ")");
    }

    public static void s(String msg, int[] locs) {
        if (isDebug) Log.i("zhjh", msg + "(" + locs[0] + "," + locs[1] + ")");
    }

    public static void s(String msg, float x, float y) {
        if (isDebug) Log.i("zhjhs", msg + "(" + x + "," + y + ")");
    }
}
