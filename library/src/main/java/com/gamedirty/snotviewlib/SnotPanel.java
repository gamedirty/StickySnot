package com.gamedirty.snotviewlib;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * The container to combine your content views with a snot monitor;
 * <p/>
 * your content view should be a part of this container;
 *
 * @author zjh
 * @description
 * @date 16/6/2.
 */
public class SnotPanel extends RelativeLayout {

    /**
     * collection of views that had been made soft
     */
    private ArrayList<View> snots;
    /**
     * the selected soft view
     */
    private View mSelectedSnot;

    /**
     * The monitor to show the selected snot.
     * react to the finger event,show different pictures;
     */
    private SnotMonitor snotMonitor;

    private Context mContext;

    public SnotPanel(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        if (child instanceof SnotMonitor) {
            snotMonitor = (SnotMonitor) child;
            snotMonitor.bringToFront();
            snotMonitor.setVisibility(GONE);
        }
    }

    private int EXH;

    /**
     * 设置activity的根布局
     *
     * @param activity
     * @param layoutId
     * @return
     */
    public static SnotPanel attachToWindow(Activity activity, int layoutId) {
        SnotPanel snotPanel = new SnotPanel(activity);
        View root = View.inflate(activity, layoutId, null);
        snotPanel.addView(root, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        snotPanel.addView(new SnotMonitor(activity), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        activity.setContentView(snotPanel);
        return snotPanel;
    }

    /**
     * 设置activity的根布局
     *
     * @param activity
     * @return
     */
    public static SnotPanel attachToWindow(Activity activity) {
        SnotPanel snotPanel = new SnotPanel(activity);
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        View child = parent.getChildAt(0);
        parent.removeView(child);
        snotPanel.addView(child, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        snotPanel.addView(new SnotMonitor(activity), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        parent.addView(snotPanel);

        return snotPanel;
    }

    private int getExH(SnotPanel snotPanel) {
        int[] xy = new int[2];
        snotPanel.getLocationInWindow(xy);
        return xy[1];
    }

    /**
     * Make the target view soft.(After that the specified view can be dragged like snot)
     *
     * @param view
     */
    public void makeViewSoft(View view) {
        if (null == snots) {
            snots = new ArrayList<>();
        }
        snots.add(view);
    }

    public void makeViewSoft(Activity act, int snotId) {
        makeViewSoft(act.findViewById(snotId));
    }


    /**
     * Get the snot monitor if  one of the snot views has been touched.
     *
     * @param eX
     * @param eY
     * @return
     */
    public View getSelectSnot(int eX, int eY) {
        if (null == snots) return null;
        for (View snot : snots) {
            int[] loc = new int[2];

            L.s("snot的尺寸:", snot.getWidth(), snot.getHeight());
            snot.getLocationInWindow(loc);
            loc[1] -= EXH;
            L.s("snot的空间位置:", loc[0], loc[1]);
            boolean isIN = eX >= loc[0] && eX <= loc[0] + snot.getWidth() && eY >= loc[1] && eY <= loc[1] + snot.getHeight();
            L.i("是不是命中:" + isIN);
            if (isIN) {
                return snot;
            }
        }
        return null;
    }

    /**
     * 有被选中的snot 接下来的所有动作都交给view，直到手指起来
     * <p/>
     * 如果没有被选中的 则就走开
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int ex = (int) ev.getX();
        int ey = (int) ev.getY();
        L.i("按下:(" + ex + "," + ey + ")");
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                EXH = getExH(this);
                L.i("多余高度是：" + EXH);
                mSelectedSnot = getSelectSnot(ex, ey);


                if (null != mSelectedSnot) {
                    L.i("dispatchTouchEvent   ACTION_DOWN");
                    snotMonitor.handleFingerDown(ex, ey, mSelectedSnot, EXH);

                    return true;
                } else {
                    L.i("没有命中");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (null != mSelectedSnot) {
                    snotMonitor.handleFingerMove(ex, ey);
                    L.i("dispatchTouchEvent   ACTION_MOVE");
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (null != mSelectedSnot) {
                    snotMonitor.handleFingerUp(ex, ey);
                    L.i("dispatchTouchEvent   ACTION_UP");
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


}
