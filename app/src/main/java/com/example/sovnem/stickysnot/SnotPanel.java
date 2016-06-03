package com.example.sovnem.stickysnot;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
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
    private View selectedSnot;

    /**
     * The monitor to show the selected snot.
     * react to the finger event,show different pictures;
     */
    private SnotMonitor snotMonitor;


    public SnotPanel(Context context) {
        super(context);
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
     * make specified view soft so that you can drag it as a snot;
     *
     * @param viewId
     */
    public void makeViewSoft(int viewId) {

    }


    /**
     * Get the snot monitor if  one of the snot views has been touched.
     *
     * @param ex
     * @param ey
     * @return
     */
    public View getSelectSnot(int ex, int ey) {
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ex = (int) event.getRawX();
        int ey = (int) event.getRawY();
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
