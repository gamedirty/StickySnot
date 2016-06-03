package com.example.sovnem.stickysnot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.LruCache;
import android.view.View;


/**
 * A view panel show what the snot looks like at three states of the snot which is:
 * 1)The snot is dragged and hasn't been pulled apart;
 * 2)The snot is dragged and has been pulled apart but your finger hasn't left it;
 * 3)Your finger has left the snot alone,the snot is playing an boom animation;
 * <p>
 * where the content of the view shows on the screen
 *
 * @author zjh
 * @description
 * @date 16/6/2.
 */
public class SnotMonitor extends View {

    /**
     * The size of cache
     * bitmap缓存的大小
     */
    private static final int MAX_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 20);
    /**
     * The current state of the snot monitor.
     */
    private int snotState;

    /**
     * Cache for bitmaps to be drawn.the key is the resource id of the view ,and the value is the bitmap which is created from view;
     */
    private LruCache<Integer, Bitmap> cache = new LruCache<Integer, Bitmap>(MAX_CACHE_SIZE) {
        @Override
        protected int sizeOf(Integer key, Bitmap value) {
            return value.getWidth() * value.getHeight();
        }
    };

    public SnotMonitor(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 鼻涕画笔
     */
    private static class SnotDrawer {


        static void drawOrisnot(Canvas canvas, float factor) {

        }

        static void drawStickyPart(Canvas canvas) {

        }

        static void drawMovingPart(Canvas canvas, int x, int y, Bitmap bitmap) {

        }

        static void drawBitmap(Canvas canvas, Bitmap bitmap, int x, int y) {

        }
    }


    /**
     * Handle touch down event from snot panel
     * 处理按下操作
     */
    public void handleFingerDown(int eX, int eY) {

    }

    /**
     * Handle touch move event from snot panel
     * 处理手指移动行为
     */
    public void handleFingerMove(int ex, int ey) {

    }

    /**
     * Handle touch up event from snot panel
     * 处理手指抬起行为
     */
    public void handleFingerUp(int ex, int ey) {

    }


    /**
     * 爆炸动画
     * Play a boom animation,when your finger left the screen and the specified view should be dismissed;
     */
    public void playBoomAnimation() {

    }

}
