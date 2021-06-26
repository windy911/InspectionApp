package com.pm.cameraui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.pm.cameraui.R;

@SuppressLint("AppCompatCustomView")
public class MyButton extends Button {


    private Paint mPaint = new Paint();



    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    Bitmap bitmap;
    private Rect mSrcRect, mDestRect;

    @Override
    protected void onDraw(Canvas canvas) {
        if (isFocused()) {
            Log.d("RAMBO", "onDraw 焦点背景");
//            //第一个矩形，大小跟此控件一样大，位于底层
//            mPaint.setColor(getResources().getColor(R.color.color_blue));
//            mPaint.setStrokeWidth((float) 4.0);
//            mPaint.setAntiAlias(false);
//            mPaint.setStyle(Paint.Style.STROKE);
//            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2-2, mPaint);

            if(bitmap==null){
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bg_btn_selected);
            }
            mDestRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredWidth());
            mSrcRect = new Rect(0, 0, bitmap.getWidth()-4, bitmap.getWidth()-4);

            canvas.drawBitmap(bitmap,mSrcRect,mDestRect,mPaint);
        }
        super.onDraw(canvas);
    }
}
