package com.gamedirty.snotviewlib;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

/**
 * @Description: 黏黏的view
 * @author 赵军辉
 * @date 2015-7-23 下午2:37:45
 */
public class SnotView extends View {
	private final long KICK_BACK_DURATION = 200;// 鼻涕回弹的时长 单位ms
	private final int BOOM_DURATION = 300;// 爆炸效果时长
	public float oriX, oriY;// “钉住”的鼻涕部分的中心点
	private int oriR;// “钉住”的鼻涕部分的中心点

	public int MAX_DISTANCE;// 最大距离 超过这个距离鼻涕被扯断

	private float fingerX, fingerY;// 手指按住的点 坐标
	private int fingerR;// 拖出来的园的半径
	private int snotColor;// 鼻涕的颜色 默认红色
	private Paint snotPaint;// 鼻涕画笔

	private Paint textPaint;// 文字画笔
	private int textColor;// 文字颜色 默认白色
	private String text;// 文字内容

	private double newR;// 鼻涕被拖动时候 钉住部分的半径 变化的
	private double dist;// 手指和钉住点之间的距离

	// newR变化区间
	private int oriRMax;// “钉住”的鼻涕部分的最大半径
	private int oriRMin;// “钉住”的鼻涕部分做小半径
	private float textSize;// 文字的大小

	// 手指松开的一刻记录的坐标
	private float recordX;
	private float recordY;//

	private double SAFE_DISTANCE;// 安全距离
	volatile boolean hasCut;// 鼻涕是不是被扯断
	private float width, height;// 鼻涕的宽高

	private int[] imgs ;
//			new int[] { R.drawable.idp, R.drawable.idq, R.drawable.idr, R.drawable.ids, R.drawable.idt };// 动画资源
	private boolean boombing;// 是不是正在播放爆炸动画
	private Bitmap bitmap;// 动画帧资源
	Handler handler = new Handler();
	private DragCallback callback;

	public void setDragCallback(DragCallback callback) {
		if (callback != null)
			this.callback = callback;
	}

	public interface DragCallback {
		/**
		 * 扯掉鼻涕调用 即鼻涕爆破
		 */
		public void onRemoveSnot();

		/**
		 * 没扯掉调用 包括回弹 和 重新黏上
		 */
		public void onFree();
	}

	public void setProperty(TextView view, int exHeigh) {
		copyPropertiesOf(view, exHeigh);
		initPaint();
	}

	private void copyPropertiesOf(TextView view, int exHeigh) {
		int[] location = new int[2];
		view.getLocationInWindow(location);
		textSize = view.getTextSize();
		ColorDrawable cDrawable = (ColorDrawable) view.getBackground();
		snotColor = cDrawable.getColor();
		ColorStateList clist = view.getTextColors();
		textColor = clist.getDefaultColor();
		width = view.getWidth();
		height = view.getHeight();
		oriR = view.getHeight() / 2;
		oriX = location[0] + view.getWidth() / 2;
		oriY = location[1] + oriR - exHeigh;
		text = view.getText().toString();
		fingerR = oriR * 5 / 7;
		oriRMax = oriR;
		oriRMin = oriR * 2 / 5;
		MAX_DISTANCE = oriR * 6;
		SAFE_DISTANCE = oriR * 5;
		boombing = false;
		hasCut = false;
		setBackgroundColor(Color.parseColor("#00000000"));
	}

	private void initPaint() {
		snotPaint = new Paint();
		snotPaint.setColor(snotColor);

		textPaint = new Paint();
		textPaint.setColor(textColor);
		textPaint.setTextSize(textSize);

	}

	public SnotView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if (boombing) {
			if (bitmap != null)
				canvas.drawBitmap(bitmap, fingerX - oriR, fingerY - oriR, snotPaint);
		} else {
			drawNowOriCircleAndSnot(canvas);
			drawMovingObject(canvas);
		}
	}

	/**
	 * @Description 画跟随手指移动的部分
	 */
	private void drawMovingObject(Canvas canvas) {
		RectF rect1 = new RectF(fingerX - width / 2, fingerY - height / 2, fingerX + width / 2, fingerY + height / 2);
		canvas.drawRoundRect(rect1, oriR, oriR, snotPaint);
		float dX = (textPaint.measureText(text) / 2);
		float dY = -((textPaint.descent() + textPaint.ascent()) / 2);
		canvas.drawText(text, fingerX - dX, fingerY + dY, textPaint);
	}

	/**
	 * 画出移动状态的原始的圆形 和中间的鼻涕
	 * 
	 * @param canvas
	 */
	private void drawNowOriCircleAndSnot(Canvas canvas) {
		dist = getDistance(fingerX, fingerY, oriX, oriY);
		if (dist <= MAX_DISTANCE && !hasCut) {
			double factor = dist / MAX_DISTANCE;
			newR = oriRMax - (oriRMax - oriRMin) * factor;
			canvas.drawCircle(oriX, oriY, (float) newR, snotPaint);
			drawSide(canvas);
		}
	}

	/**
	 * 绘制两边略带弧度的线 即手指按点和原位置之间‘粘稠’的部分
	 * 
	 * @param canvas
	 */
	private void drawSide(Canvas canvas) {
		double cos = getCons(fingerX, fingerY, oriX, oriY);
		double sin = Math.sqrt(1 - cos * cos);
		double dX1 = newR * cos;
		double dY1 = newR * sin;
		double dX2 = fingerR * cos;
		double dY2 = fingerR * sin;
		Point[] p = new Point[2];
		Point[] p2 = new Point[2];

		Point[] c = new Point[2];
		c[0] = new Point((fingerX + oriX) / 2, (fingerY + oriY) / 2);
		c[1] = c[0];
		if ((fingerY >= oriY && fingerX <= oriX) || (fingerY <= oriY && fingerX >= oriX)) {
			p[0] = new Point(oriX + dX1, oriY + dY1);
			p[1] = new Point(oriX - dX1, oriY - dY1);

			p2[0] = new Point(fingerX + dX2, fingerY + dY2);
			p2[1] = new Point(fingerX - dX2, fingerY - dY2);

		} else if (fingerY >= oriY && fingerX >= oriX || (fingerY <= oriY && fingerX <= oriX)) {

			p[0] = new Point(oriX - dX1, oriY + dY1);
			p[1] = new Point(oriX + dX1, oriY - dY1);

			p2[0] = new Point(fingerX - dX2, fingerY + dY2);
			p2[1] = new Point(fingerX + dX2, fingerY - dY2);
		}
		drawStickyShape(canvas, p, p2, c);
	}

	/**
	 * 贝塞尔曲线围起来的梯形
	 */
	public void drawStickyShape(Canvas canvas, Point[] p, Point[] p2, Point[] c) {
		Path path = new Path();
		path.moveTo((float) p[0].x, (float) p[0].y);
		path.quadTo((float) c[0].x, (float) c[0].y, (float) p2[0].x, (float) p2[0].y);
		path.lineTo((float) p2[1].x, (float) p2[1].y);
		path.quadTo((float) c[1].x, (float) c[1].y, (float) p[1].x, (float) p[1].y);
		path.lineTo((float) p[0].x, (float) p[0].y);
		canvas.drawPath(path, snotPaint);
	}

	/**
	 * 方便管理坐标
	 */
	class Point {
		public Point(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}

		double x, y;
	}

	/**
	 * 求出 点(x1,y1)和点(x2,y2)两点连线与x轴相交形成的锐角的余弦
	 * 
	 * @param x1
	 *            第一个点的x坐标
	 * @param y1
	 *            第一个点的y坐标
	 * @param x2
	 *            第二个点的x坐标
	 * @param y2
	 *            第二个点的y坐标
	 * @return
	 */
	public double getCons(float x1, float y1, float x2, float y2) {
		double dis = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		double side1 = Math.abs(y1 - y2);
		double cos = side1 / dis;
		return cos;
	}

	private void handleFingerUp() {
		if (!hasCut) {// 还被鼻涕粘着 要回弹
			kickback();
		} else {// 鼻涕扯断 脱落
			// 分两种情况 安全区域内 复原
			// 安全区域外爆破
			if (dist < SAFE_DISTANCE) {
				fingerX = oriX;
				fingerY = oriY;
				hasCut = false;
				postInvalidate();
				setVisibility(View.GONE);
				if (callback != null)
					callback.onFree();
			} else {
				playBoomAnim();
			}

		}
	}

	/**
	 * @Description 播放爆炸动画
	 */
	private synchronized void playBoomAnim() {

		ValueAnimator boomAnim = ValueAnimator.ofInt(0, imgs.length - 1);
		boomAnim.setDuration(BOOM_DURATION);
		boomAnim.addUpdateListener(new AnimatorUpdateListener() {

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
				boombing = false;
				setVisibility(View.GONE);
				fingerX = oriX;
				fingerY = oriY;
				hasCut = false;
				if (callback != null)
					callback.onRemoveSnot();
			}
		});
		boomAnim.start();
	}

	/**
	 * 
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
		backAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				doWhenKickback(animation);
			}
		});
		backAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				setVisibility(View.GONE);
				if (callback != null)
					callback.onFree();
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		backAnimator.start();
	}

	protected void doWhenKickback(ValueAnimator animation) {
		float value = Float.parseFloat(animation.getAnimatedValue().toString());//
		final double cos = getCons(fingerX, fingerY, oriX, oriY);
		final double sin = Math.sqrt(1 - cos * cos);
		if (recordX >= oriX && recordY >= oriY) {
			fingerX = (float) (oriX + value * sin);
			fingerY = (float) (oriY + value * cos);
		} else if (recordX < oriX && recordY > oriY) {
			fingerX = (float) (oriX - value * sin);
			fingerY = (float) (oriY + value * cos);
		} else if (recordX > oriX && recordY < oriY) {
			fingerX = (float) (oriX + value * sin);
			fingerY = (float) (oriY - value * cos);
		} else {
			fingerX = (float) (oriX - value * sin);
			fingerY = (float) (oriY - value * cos);
		}
		postInvalidate();
	}

	/**
	 * @Description: 回弹的时候的差值器
	 * @author monkey-d-wood
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
	 * 得到两点之间的距离
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private double getDistance(float x1, float y1, float x2, float y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	private void doWhenFingerMove() {
		dist = getDistance(fingerX, fingerY, oriX, oriY);
		if (dist > MAX_DISTANCE) {
			hasCut = true;
		}
		postInvalidate();
	}

	View v;

	public synchronized void handlerTvTouchEvent2(MotionEvent event, View v, int exHeight) {
		float x = event.getX();
		float y = event.getY();

		this.fingerX = x;
		this.fingerY = y - exHeight;
		this.v = v;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setProperty((TextView) v, exHeight);
			setVisibility(View.VISIBLE);
			break;
		case MotionEvent.ACTION_UP:
			handleFingerUp();
			break;
		case MotionEvent.ACTION_MOVE:
			doWhenFingerMove();
			break;
		}
	}

}
