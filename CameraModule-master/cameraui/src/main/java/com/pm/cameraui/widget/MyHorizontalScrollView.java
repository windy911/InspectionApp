package com.pm.cameraui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {

    private int iDownX;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public boolean onTouchEvent(MotionEvent e) {

        int iAction = e.getAction();

        switch (iAction) {

            case MotionEvent.ACTION_DOWN:

                iDownX = (int) e.getX();

                break;

            case MotionEvent.ACTION_MOVE:

                int iMoveX = (int) e.getX();
                if (Math.abs(iMoveX - iDownX) < 1500) {
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:

                int iUpX = (int) e.getX();
                if (Math.abs(iUpX - iDownX) < 1500) {
                    return true;
                }

                break;
            default:
                break;
        }

        return super.onTouchEvent(e);

    }

}