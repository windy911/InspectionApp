package com.pm.cameraui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pm.cameraui.R;
import com.pm.cameraui.utils.TimeUtil;

public class MyVidoeController extends RelativeLayout {

    CameraController.ControllerCallback mCallback;
    Button btnStart, btnSwitchCamera, btnSwitchCamera2, btnStop, btnContinue, btnPause, btnTakePic, btnExitApp;
    LinearLayout llRecording;
    TextView tvRedDot, tvTimer;
    ProgressBar voiceProgress;
    LinearLayout llVoiceMark;


    public MyVidoeController(Context context) {
        super(context);
        init(context, null);
    }

    public MyVidoeController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyVidoeController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet atts) {
        inflate(context, R.layout.layout_controller_view, this);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnContinue = findViewById(R.id.btnContinue);
        btnStop = findViewById(R.id.btnStop);
        llRecording = findViewById(R.id.llRecording);
        btnSwitchCamera = findViewById(R.id.btnSwitchCamera);
        btnSwitchCamera2 = findViewById(R.id.btnSwitchCamera2);
        btnTakePic = findViewById(R.id.btnTakePic);
        //暂时不需要显示该按钮在眼镜上由按键触发
        btnTakePic.setVisibility(View.GONE);
        //暂时隐藏该截图，业务上不需要显示
//        imgPreview.setVisibility(View.GONE);
        tvRedDot = findViewById(R.id.tvRedDot);
        tvTimer = findViewById(R.id.tvTimer);
        tvRedDot.setVisibility(View.GONE);
        llVoiceMark = findViewById(R.id.llVoiceMark);
        voiceProgress = findViewById(R.id.voiceProgress);
        btnExitApp = findViewById(R.id.btnExitApp);
        btnStart.setOnClickListener(view -> {
            if (mCallback != null) {
                if (!isValidClick()) return;
                if (mCallback != null) {
                    mCallback.recordStart();
                    setRecordDotShow(true);
                }
            }
        });
        btnStop.setOnClickListener(view -> {
            if (mCallback != null) {
                if (!isValidClick()) return;
                if (mCallback != null) {
                    mCallback.recordStop();
                    setRecordDotShow(false);
                }
            }
        });
        btnSwitchCamera.setOnClickListener(view -> {
            if (mCallback != null) {
                if (!isValidClick()) return;
                mCallback.onSwitchCamera();
            }
        });
        btnSwitchCamera2.setOnClickListener(view -> {
            if (mCallback != null) {
                if (!isValidClick()) return;
                mCallback.onSwitchCamera();
            }
        });
        btnPause.setOnClickListener(view -> {
            if (mCallback != null) {
                if (!isValidClick()) return;
                mCallback.recordPause();
                setRecordDotShow(false);
            }
        });
        btnContinue.setOnClickListener(view -> {
            if (mCallback != null) {
                if (!isValidClick()) return;
                mCallback.recordContinue();
                setRecordDotShow(true);
            }
        });
        btnTakePic.setOnClickListener(view -> {
            if (mCallback != null) {
                if (!isValidClick()) return;
                mCallback.takePicture();
            }
        });
        btnExitApp.setOnClickListener(view -> {
            if (mCallback != null) {
                mCallback.onExitApp();
            }
        });

    }

    private long lastActionClicked = System.currentTimeMillis();

    public boolean isValidClick() {
        if (System.currentTimeMillis() - lastActionClicked > 2000) {
            lastActionClicked = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }


    public void setControllerCallback(CameraController.ControllerCallback callback) {
        this.mCallback = callback;
    }

    public void refreshUIRecord(boolean isRecording) {
        btnStart.setVisibility(isRecording ? View.GONE : View.VISIBLE);
        llRecording.setVisibility(isRecording ? View.VISIBLE : View.GONE);
        btnSwitchCamera.setVisibility(isRecording ? View.GONE : View.VISIBLE);
    }

    public void setPreViewImage(Bitmap bitmap) {
//        imgPreview.setBackground(new BitmapDrawable(bitmap));
    }

    public void pauseRecording() {
        btnPause.setVisibility(View.GONE);
        btnContinue.setVisibility(View.VISIBLE);
    }

    public void continueRecording() {
        btnPause.setVisibility(View.VISIBLE);
        btnContinue.setVisibility(View.GONE);
    }

    public void setRecordDotShow(boolean isShow) {
        if (tvRedDot != null) {
            tvRedDot.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    public void refreshTimer(long time) {
        if (tvTimer != null) {
            tvTimer.setText(TimeUtil.formatTime(time));
        }
    }

    public void showVoiceMarker(int progress) {
        Log.d("RAMBO", "voice progress = " + progress);
        llVoiceMark.setVisibility(View.VISIBLE);
        voiceProgress.setMax(100);
        voiceProgress.setProgress(progress);
    }

    public void hideVoiceMarker() {
        llVoiceMark.setVisibility(View.GONE);
    }
}
