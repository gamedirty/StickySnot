package com.example.sovnem.stickysnot;

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

    /**
     * 设置activity的根布局
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


    /**
     * Get the snot monitor if  one of the snot views has been touched.
     *
     * @param eX
     * @param eY
     *
     * @return
     */
    public View getSelectSnot(int eX, int eY) {
        for (View snot : snots) {
            int[] loc = new int[2];
            snot.getLocationOnScreen(loc);
            if (eX >= loc[0] && eX <= loc[0] + snot.getWidth() && eY >= loc[1] && eY <= loc[1] + snot.getHeight()) {
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
        int ex = (int) ev.getRawX();
        int ey = (int) ev.getRawY();
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mSelectedSnot = getSelectSnot(ex, ey);
                if (null != mSelectedSnot) {
                    snotMonitor.handleFingerDown(ex, ey, mSelectedSnot);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (null != mSelectedSnot) {
                    snotMonitor.handleFingerMove(ex, ey);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (null != mSelectedSnot) {
                    snotMonitor.handleFingerUp(ex, ey);
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


}
