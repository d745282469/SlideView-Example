package com.dong.customview.drawView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author pd
 * time     2019/3/26 11:02
 */
public class SecCustomView extends View {
    private Paint paint;
    private Path path;
    private int rotation;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setRotation(rotation++);
            if (rotation == 360){
                rotation = 0;
            }
            invalidate();
            start();
        }
    };

    public SecCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint(){
        paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.moveTo((float)(getRight()-getLeft())/2,(float)(getBottom()-getTop())/2);
        path.rLineTo(100,100);
        canvas.drawPath(path,paint);
        start();
    }

    private void start(){
        removeCallbacks(runnable);
        postDelayed(runnable,5);
    }
}
