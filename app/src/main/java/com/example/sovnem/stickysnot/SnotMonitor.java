package com.example.sovnem.stickysnot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcelable;
import android.view.View;


/**
 * A view panel show what the snot looks like at three states of the snot which is:
 * 1)The snot is dragged and hasn't been pulled apart;
 * 2)The snot is dragged and has been pulled apart but your finger hasn't left it;
 * 3)Your finger has left the snot alone,the snot is playing an boom animation;
 * <p/>
 * where the content of the view shows on the screen
 *
 * @author zjh
 * @description
 * @date 16/6/2.
 */
public class SnotMonitor extends View {

    private final long KICK_BACK_DURATION = 200;// 鼻涕回弹的时长 单位ms
    private final int BOOM_DURATION = 300;// 爆炸效果时长
    public float ORIX, ORIY;// “钉住”的鼻涕部分的中心点
    private int ORIR;// “钉住”的鼻涕部分的中心点

    public int MAX_DISTANCE;// 最大距离 超过这个距离鼻涕被扯断

    private float fingerX, fingerY;// 手指按住的点 坐标
    private int FINGERR;// 拖出来的园的半径
    private int SNOTCOLOR;// 鼻涕的颜色 默认红色
    private Paint snotPaint;// 鼻涕画笔

    private double newR;// 鼻涕被拖动时候 钉住部分的半径 变化的
    private double dist;// 手指和钉住点之间的距离

    // newR变化区间
    private int ORIRMAX;// “钉住”的鼻涕部分的最大半径
    private int ORIRMIN;// “钉住”的鼻涕部分做小半径
    private float textSize;// 文字的大小

    // 手指松开的一刻记录的坐标
    private float RECORDX;
    private float RECORDY;//

    private double SAFE_DISTANCE;// 安全距离
    volatile boolean hasCut;// 鼻涕是不是被扯断
    private boolean isAnimating;
    private float DOWNX, DOWNY;
    private Bitmap viewBitmap;
    private MiniBitmapMemaryCache cache;

    public SnotMonitor(Context context) {
        super(context);
        cache = new MiniBitmapMemaryCache();
    }

    private void initializeParams(int eX, int eY, View snotBall) {
        //基础尺寸
        int[] locs = new int[2];
        snotBall.getLocationInWindow(locs);
        int w = snotBall.getWidth();
        int h = snotBall.getHeight();

        //绘制主体的处理
        viewBitmap = cache.get(snotBall.toString());
        if (null == viewBitmap) {
            viewBitmap = Utils.convert2Bitmap(snotBall);
            cache.put(snotBall.toString(), viewBitmap);
        }

        //由基础尺寸衍生而来的必要尺寸
        ORIR = Math.min(w, h) / 2;
        ORIX = locs[0] + w / 2;
        ORIY = locs[1] + h / 2;
        FINGERR = ORIR * 5 / 7;
        ORIRMAX = ORIR;
        ORIRMIN = ORIR * 2 / 5;
        MAX_DISTANCE = ORIR * 6;
        SAFE_DISTANCE = ORIR * 5;

        //鼻涕颜色值 由bitmap5点取样而来
        SNOTCOLOR = Utils.getLightenColorOf(viewBitmap);
    }

    private void initPaint() {
        snotPaint = new Paint();
        snotPaint.setColor(SNOTCOLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isAnimating) {
            drawFrame(canvas);
        } else {
            drawSnot(canvas);
        }
    }

    /**
     * draw frames of animation
     *
     * @param canvas
     */
    private void drawFrame(Canvas canvas) {
    }

    /**
     * 画鼻涕
     * draw snot of ani states
     *
     * @param canvas
     */
    private void drawSnot(Canvas canvas) {

    }


    /**
     * Handle touch down event from snot panel
     * 处理按下操作
     * copy属性 设置是否显示等
     */
    public void handleFingerDown(int eX, int eY, View snotBall) {
        initializeParams(eX, eY, snotBall);
        initPaint();
        DOWNX = fingerX = eX;
        DOWNY = fingerY = eY;
    }

    /**
     * Handle touch move event from snot panel
     * 处理手指移动行为
     * 根据手指移动,处理鼻涕的移动等
     */
    public void handleFingerMove(int ex, int ey) {
        fingerX = ex;
        fingerY = ey;
    }

    /**
     * Handle touch up event from snot panel
     * 处理手指抬起行为，抬起设置显示等 复原状态等
     */
    public void handleFingerUp(int ex, int ey) {
        fingerX = ex;
        fingerY = ey;
    }


    /**
     * 爆炸动画
     * Play a boom animation,when your finger left the screen and the specified view should be dismissed;
     * the  boom animation can be a series of picture or a common animation of android views.
     */
    public void playBoomAnimation() {

    }


    @Override
    protected Parcelable onSaveInstanceState() {
        //TODO:啦啦啦
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        //TODO:啦啦啦
    }
}
