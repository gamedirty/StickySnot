package com.example.sovnem.stickysnot;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Some utilities;
 * 一些工具方法
 *
 * @author zjh
 * @description
 * @date 16/6/2.
 */
public class Utils {

    /**
     * Convert a view to a bitmap object;
     *
     * @param view the view to be converted
     *
     * @return
     */
    public static Bitmap convert2Bitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    /**
     * Get the phone's status bar height
     *
     * @param act
     *
     * @return
     */
    public static int getStatusBarHeight(Activity act) {
        Rect frame = new Rect();
        act.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    /**
     * Return the actionbar height of the phone;
     *
     * @param act
     *
     * @return
     */
    public static int getActionBarHeight(Activity act) {
        ActionBar actionBar = act.getActionBar();
        int actH = 0;
        if (actionBar != null) {
            actH = actionBar.getHeight();
        }
        return actH;
    }

    /**
     * Return extra height of phone,contains actionbar' height and status bar's height
     *
     * @param act
     *
     * @return
     */
    public static int getExtraHeight(Activity act) {
        return getActionBarHeight(act) + getStatusBarHeight(act);
    }

    /**
     * Return the darken color of a bitmap;
     * <p/>
     * 5点取样法
     *
     * @param bitmap
     *
     * @return
     */
    public static int getDarkColorOf(Bitmap bitmap) {
        return 1;
    }

    /**
     * Get the lighten color of a bitmap;
     *
     * @param bitmap
     *
     * @return
     */
    public static int getLightenColorOf(Bitmap bitmap) {
        return 2;
    }

    public static int getBackgroundOf(View view) {
        Drawable bg = view.getBackground();
        if (bg instanceof ColorDrawable) {
            return ((ColorDrawable) bg).getColor();
        }
        return 0;
    }
}
