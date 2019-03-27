package com.dong.customview.drawView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * @author pd
 * time     2019/3/26 08:41
 */
public class MoveImg extends android.support.v7.widget.AppCompatImageView {
    private boolean isRealMove = false;//是否改变拖动后imageView的真实位置

    public MoveImg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    int lastX = 0;
    int lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                if (isRealMove){
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
                    params.leftMargin = getLeft() + offsetX;
                    params.topMargin = getTop() + offsetY;
                    setLayoutParams(params);
                }else {
                    layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                }
                Log.d("dong","x = " + x + ",y = " + y +",lastX = " + lastX + ",lastY = " + lastY +
                        ",offsetX = " + offsetX + ",offsetY = " + offsetY );
                break;
        }

        return true;
    }

    public void setRealMove(boolean realMove) {
        isRealMove = realMove;
    }
}
