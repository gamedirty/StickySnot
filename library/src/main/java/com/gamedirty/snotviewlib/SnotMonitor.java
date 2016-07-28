package com.gamedirty.snotviewlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.OvershootInterpolator;


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

    private final long KICK_BACK_DURATION = 175;// 鼻涕回弹的时长 单位ms
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
    private int w, h;//绘制图形的尺寸
    private View currentSnot;
    private int EXH;
    private Path path;
    private float recordX, recordY;
    private Bitmap bitmap;
    private int[] imgs;

    public SnotMonitor(Context context) {
        super(context);
        cache = new MiniBitmapMemaryCache();
    }

    private void initializeParams(View snotBall) {
        //基础尺寸
        int[] locs = new int[2];
        currentSnot = snotBall;
        snotBall.getLocationInWindow(locs);
        w = snotBall.getWidth();
        h = snotBall.getHeight();
        L.i("被选中的snot的位置：" + "(" + locs[0] + "," + locs[1] + "),尺寸是:w:" + w + ",h:" + h);
        //绘制主体的处理
        viewBitmap = (Bitmap) cache.get(snotBall.toString());
        if (null == viewBitmap) {
            viewBitmap = Utils.convert2Bitmap(snotBall);
            cache.put(snotBall.toString(), viewBitmap);
            L.i("缓存起来view的bitmap转换视图");
        }

        //由基础尺寸衍生而来的必要尺寸
        ORIR = Math.min(w, h) / 2;
        ORIX = locs[0] + w / 2;
        ORIY = locs[1] + h / 2 - EXH;

        L.s("原始的view中心点", ORIX, ORIY);

        FINGERR = ORIR * 5 / 7;
        ORIRMAX = ORIR;
        ORIRMIN = ORIR * 2 / 5;
        MAX_DISTANCE = ORIR * 6;
        SAFE_DISTANCE = ORIR * 5;


        //鼻涕颜色值 由bitmap5点取样而来
        SNOTCOLOR = Utils.getBackgroundOf(snotBall);

        L.i("鼻涕的颜色:" + SNOTCOLOR);
        snotBall.setVisibility(View.INVISIBLE);
        setVisibility(View.VISIBLE);
    }

    private void initPaint() {
        snotPaint = new Paint();
        snotPaint.setColor(SNOTCOLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        L.i("snotview ---ondraw");
        if (isAnimating) {
            drawFrame(canvas);
        } else {
            drawSnot(canvas);
        }
    }

    /**
     * draw frames of animation
     * 画动画的一帧
     *
     * @param canvas
     */
    private void drawFrame(Canvas canvas) {
        if (bitmap != null)
            canvas.drawBitmap(bitmap, fingerX - ORIR, fingerY - ORIR, snotPaint);
    }

    /**
     * 画鼻涕
     * draw snot of ani states
     *
     * @param canvas
     */
    private void drawSnot(Canvas canvas) {
        if (!hasCut) {
            drawStickyPoint(canvas);
            drawStickyLine(canvas);
        }
        drawMovingPart(canvas);
    }

    /**
     * 画出 跟随手指移动的部分
     *
     * @param canvas
     */
    private void drawMovingPart(Canvas canvas) {
        L.i("画移动部分");
        canvas.drawBitmap(viewBitmap, fingerX - w / 2, fingerY - h / 2, snotPaint);
    }

    /**
     * 画中间的连接线
     *
     * @param canvas
     */
    private void drawStickyLine(Canvas canvas) {
        double cos = Utils.getCons(fingerX, fingerY, ORIX, ORIY);
        double sin = Math.sqrt(1 - cos * cos);
        double dX1 = newR * cos;
        double dY1 = newR * sin;
        double dX2 = FINGERR * cos;
        double dY2 = FINGERR * sin;
        Point[] p = new Point[2];
        Point[] p2 = new Point[2];

        Point[] c = new Point[2];
        c[0] = new Point((fingerX + ORIX) / 2, (fingerY + ORIY) / 2);
        c[1] = c[0];
        if ((fingerY >= ORIY && fingerX <= ORIX) || (fingerY <= ORIY && fingerX >= ORIX)) {
            p[0] = new Point(ORIX + dX1, ORIY + dY1);
            p[1] = new Point(ORIX - dX1, ORIY - dY1);

            p2[0] = new Point(fingerX + dX2, fingerY + dY2);
            p2[1] = new Point(fingerX - dX2, fingerY - dY2);

        } else if (fingerY >= ORIY && fingerX >= ORIX || (fingerY <= ORIY && fingerX <= ORIX)) {

            p[0] = new Point(ORIX - dX1, ORIY + dY1);
            p[1] = new Point(ORIX + dX1, ORIY - dY1);

            p2[0] = new Point(fingerX - dX2, fingerY + dY2);
            p2[1] = new Point(fingerX + dX2, fingerY - dY2);
        }


        if (path == null) {
            path = new Path();
        }
        path.reset();
        path.moveTo((float) p[0].x, (float) p[0].y);
        path.quadTo((float) c[0].x, (float) c[0].y, (float) p2[0].x, (float) p2[0].y);
        path.lineTo((float) p2[1].x, (float) p2[1].y);
        path.quadTo((float) c[1].x, (float) c[1].y, (float) p[1].x, (float) p[1].y);
        path.lineTo((float) p[0].x, (float) p[0].y);
        canvas.drawPath(path, snotPaint);
    }

    /**
     * 画黏住的固定的部分
     *
     * @param canvas
     */
    private void drawStickyPoint(Canvas canvas) {
        dist = Utils.getDistance(fingerX, fingerY, ORIX, ORIY);
        if (dist <= MAX_DISTANCE && !hasCut) {
            double factor = dist / MAX_DISTANCE;
            newR = ORIRMAX - (ORIRMAX - ORIRMIN) * factor;
            canvas.drawCircle(ORIX, ORIY, (float) newR, snotPaint);
        }
    }


    /**
     * @Description 回弹
     */
    private void kickback() {
        recordX = fingerX;
        recordY = fingerY;
        ValueAnimator backAnimator = ValueAnimator.ofFloat((float) dist, 0);
        OvershootInterpolator inter = new MyQQDragInterprator();
        // changeTension(inter, 4);
        backAnimator.setInterpolator(inter);
        backAnimator.setDuration(KICK_BACK_DURATION);
        backAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                doWhenKickback(animation);
            }
        });
        backAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetVisibilityState();
                hasCut = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        backAnimator.start();
    }

    private void resetVisibilityState() {
        setVisibility(View.INVISIBLE);
        if (currentSnot != null) {
            currentSnot.setVisibility(View.VISIBLE);
        }
    }

    protected void doWhenKickback(ValueAnimator animation) {
        float value = Float.parseFloat(animation.getAnimatedValue().toString());//
        final double cos = Utils.getCons(fingerX, fingerY, ORIX, ORIY);
        final double sin = Math.sqrt(1 - cos * cos);
        if (recordX >= ORIX && recordY >= ORIY) {
            fingerX = (float) (ORIX + value * sin);
            fingerY = (float) (ORIY + value * cos);
        } else if (recordX < ORIX && recordY > ORIY) {
            fingerX = (float) (ORIX - value * sin);
            fingerY = (float) (ORIY + value * cos);
        } else if (recordX > ORIX && recordY < ORIY) {
            fingerX = (float) (ORIX + value * sin);
            fingerY = (float) (ORIY - value * cos);
        } else {
            fingerX = (float) (ORIX - value * sin);
            fingerY = (float) (ORIY - value * cos);
        }
        postInvalidate();
    }

    public void setBoomSrc(int[] resids) {
        this.imgs = resids;
    }

    /**
     * @author monkey-d-wood
     * @Description: 回弹的时候的差值器
     */
    private class MyQQDragInterprator extends OvershootInterpolator {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            float answer1 = (float) Math.sin(Math.PI * 5 / 2 * t) * t;
            return 1 - answer1;
        }
    }

    /**
     * Handle touch down event from snot panel
     * 处理按下操作
     * copy属性 设置是否显示等
     */
    public void handleFingerDown(int eX, int eY, View snotBall, int EXH) {
        this.EXH = EXH;
        initializeParams(snotBall);
        initPaint();

        L.i("handleFingerDown:(" + eX + "," + eY + ")");
        DOWNX = fingerX = eX;
        DOWNY = fingerY = eY;

        invalidate();
    }


    /**
     * Handle touch move event from snot panel
     * 处理手指移动行为
     * 根据手指移动,处理鼻涕的移动等
     */
    public void handleFingerMove(int ex, int ey) {
        fingerX = ex;
        fingerY = ey;
        dist = Utils.getDistance(fingerX, fingerY, ORIX, ORIY);
        if (dist > MAX_DISTANCE) {
            hasCut = true;
        }
        invalidate();
        L.i("handleFingerMove:(" + ex + "," + ex + ")");
    }

    /**
     * Handle touch up event from snot panel
     * 处理手指抬起行为，抬起设置显示等 复原状态等
     */
    public void handleFingerUp(int ex, int ey) {
        fingerX = ex;
        fingerY = ey;


        if (!hasCut) {
            if (dist >= ORIR * 1.5)
                kickback();
            else {
                resetVisibilityState();
                hasCut = false;
            }
            L.i("回弹");
        } else {
            if (dist < SAFE_DISTANCE) {
                fingerX = ORIX;
                fingerY = ORIY;
                hasCut = false;
                postInvalidate();
                resetVisibilityState();
            } else {
                playBoomAnim();
            }
        }


        L.i("handleFingerUp:(" + ex + "," + ex + ")");
        invalidate();
    }

    /**
     * @Description 播放爆炸动画
     */
    private synchronized void playBoomAnim() {
        isAnimating = true;
        ValueAnimator boomAnim = ValueAnimator.ofInt(0, imgs.length - 1);
        boomAnim.setDuration(BOOM_DURATION);
        boomAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int id = Integer.parseInt(animation.getAnimatedValue().toString());
                bitmap = BitmapFactory.decodeResource(getResources(), imgs[id]);
                postInvalidate();
            }
        });

        boomAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                setVisibility(View.GONE);
                fingerX = ORIX;
                fingerY = ORIY;
                hasCut = false;
            }
        });
        boomAnim.start();
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
