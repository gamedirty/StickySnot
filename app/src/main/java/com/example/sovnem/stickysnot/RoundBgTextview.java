package com.example.sovnem.stickysnot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class RoundBgTextview extends TextView {

    private int bgColor;
    private int w, h;
    private RectF bgRect;
    private Paint bgPaint;

    public RoundBgTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        bgRect = new RectF();
        bgPaint = new Paint();
        bgPaint.setStyle(Style.FILL_AND_STROKE);
    }

    public RoundBgTextview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundBgTextview(Context context) {
        this(context, null);
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        if (!(d instanceof ColorDrawable)) {
            throw new RuntimeException("This textview's background should be the type of ColorDrawable");
        }
        ColorDrawable cd = (ColorDrawable) d;
        bgColor = cd.getColor();
        invalidate();
        super.setBackgroundDrawable(null);
    }

    public int getBgColor() {
        return bgColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircleBackground(canvas);
        if (dif > 0)
            canvas.translate(dif / 2, 0);
        super.onDraw(canvas);
    }

    private void drawCircleBackground(Canvas canvas) {
        bgRect.left = 0;
        bgRect.right = w;
        bgRect.top = 0;
        bgRect.bottom = h;
        bgPaint.setColor(bgColor);
        canvas.drawRoundRect(bgRect, h / 2, h / 2, bgPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        invalidate();
    }

    int dif;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width < height) {
            dif = height - width;
            width = height;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left - dif / 2, top, right + dif / 2, bottom);
    }
}
