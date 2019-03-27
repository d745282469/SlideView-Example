package com.dong.customview.drawView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dong.customview.R;

/**
 * @author pd
 * time     2019/3/26 09:18
 */
public class FirstCustomView extends View {
    private Paint paint;
    private Context context;
    private boolean toBig = true;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (toBig) {
                radiu++;
            } else {
                radiu--;
            }

            if (radiu == 90){
                toBig = false;
            }else if (radiu == 0){
                toBig = true;
            }
            invalidate();
            start();
        }
    };

    private int radiu = 50;

    public FirstCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initPaint();
        setWillNotDraw(false);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.STROKE);//描边
        paint.setStrokeWidth(10);//边的宽度
        paint.setAntiAlias(true);//抗锯齿
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(100, 100, radiu, paint);
        start();
    }

    private void start() {
        removeCallbacks(runnable);
        postDelayed(runnable, 10);
    }

}
