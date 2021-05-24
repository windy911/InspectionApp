package com.pm.cameraui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pm.cameraui.R;
import com.pm.cameraui.utils.TimeUtil;

public class MyVidoeController extends RelativeLayout {

    CameraController.ControllerCallback mCallback;
    Button btnStart,btnCameraSwitch,btnStop,btnContinue,btnPause,btnTakePic;
    LinearLayout llRecording;
    ImageView imgPreview;
    TextView tvRedDot,tvTimer;


    public MyVidoeController(Context context) {
        super(context);
        init(context,null);
    }

    public MyVidoeController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyVidoeController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet atts){
        inflate(context, R.layout.layout_controller_view,this);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnContinue = findViewById(R.id.btnContinue);
        btnStop = findViewById(R.id.btnStop);
        llRecording = findViewById(R.id.llRecording);
        btnCameraSwitch = findViewById(R.id.btnSwitchCamera);
        btnTakePic = findViewById(R.id.btnTakePic);
        imgPreview = findViewById(R.id.imgPreview);
        tvRedDot = findViewById(R.id.tvRedDot);
        tvTimer = findViewById(R.id.tvTimer);
        tvRedDot.setVisibility(View.GONE);
        btnStart.setOnClickListener(view->{
            if(mCallback!=null){
                if (mCallback != null) {
                    mCallback.recordStart();
                    setRecordDotShow(true);
                }
            }
        });
        btnStop.setOnClickListener(view->{
            if(mCallback!=null){
                if (mCallback != null) {
                    mCallback.recordStop();
                    setRecordDotShow(false);
                }
            }
        });
        btnCameraSwitch.setOnClickListener(view->{
            if(mCallback!=null){
                mCallback.onSwitchCamera();
            }
        });
        btnPause.setOnClickListener(view->{
            if(mCallback!=null){
                mCallback.recordPause();
                setRecordDotShow(false);
            }
        });
        btnContinue.setOnClickListener(view->{
            if(mCallback!=null){
                mCallback.recordContinue();
                setRecordDotShow(true);
            }
        });
        btnTakePic.setOnClickListener(view->{
            if(mCallback!=null){
                mCallback.takePicture();
            }
        });


    }

    public void setControllerCallback(CameraController.ControllerCallback callback) {
        this.mCallback = callback;
    }

    public void refreshUIRecord(boolean isRecording){
        btnStart.setVisibility(isRecording? View.GONE:View.VISIBLE);
        llRecording.setVisibility(isRecording?View.VISIBLE:View.GONE);
    }

    public void setPreViewImage(Bitmap bitmap){
        imgPreview.setBackground(new BitmapDrawable(bitmap));
    }

    public void pauseRecording(){
        btnPause.setVisibility(View.GONE);
        btnContinue.setVisibility(View.VISIBLE);
    }

    public void continueRecording(){
        btnPause.setVisibility(View.VISIBLE);
        btnContinue.setVisibility(View.GONE);
    }

    public void setRecordDotShow(boolean isShow){
        if(tvRedDot!=null){
            tvRedDot.setVisibility(isShow?View.VISIBLE:View.GONE);
        }
    }

    public void refreshTimer(long time){
        if(tvTimer!=null){
            tvTimer.setText(TimeUtil.formatTime(time));
        }
    }
}
