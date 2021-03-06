package com.gamedirty.snotviewlib;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
        Bitmap bitmap = Bitmap.createBitmap((int) (view.getMeasuredWidth() * 1.2f), (int) (view.getMeasuredHeight() * 1.2f), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return big(bitmap);
    }

    private static Bitmap big(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(1.03f, 1.03f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizeBmp != bitmap) bitmap.recycle();
        return resizeBmp;
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
        return frame.top;
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
        } else {
            Bitmap b = convert2Bitmap(view);
            int color = b.getPixel(b.getWidth() / 2, 0);
            b.recycle();
            return color;
        }
    }

    /**
     * 得到两点之间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     *
     * @return
     */
    public static double getDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    /**
     * 求出 点(x1,y1)和点(x2,y2)两点连线与x轴相交形成的锐角的余弦
     *
     * @param x1 第一个点的x坐标
     * @param y1 第一个点的y坐标
     * @param x2 第二个点的x坐标
     * @param y2 第二个点的y坐标
     *
     * @return
     */
    public static double getCons(float x1, float y1, float x2, float y2) {
        double dis = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        double side1 = Math.abs(y1 - y2);
        return side1 / dis;
    }
}
