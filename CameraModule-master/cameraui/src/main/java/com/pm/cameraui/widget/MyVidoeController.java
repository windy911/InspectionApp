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
    Button btnStart, btnSwitchCamera, btnSwitchCamera2, btnStop, btnContinue, btnPause, btnExitApp;
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

        btnExitApp.setOnClickListener(view -> {
            if (mCallback != null) {
                mCallback.onExitApp();
            }
        });

        btnStart.requestFocus();
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
        btnPause.requestFocus();
    }

    public void setPreViewImage(Bitmap bitmap) {
//        imgPreview.setBackground(new BitmapDrawable(bitmap));
    }

    public void pauseRecording() {
        btnPause.setVisibility(View.GONE);
        btnContinue.setVisibility(View.VISIBLE);
        btnContinue.requestFocus();
    }

    public void continueRecording() {
        btnPause.setVisibility(View.VISIBLE);
        btnContinue.setVisibility(View.GONE);
        btnPause.requestFocus();
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


    public void onClicked() {
        if (btnStart.isFocused()) {
            btnStart.performClick();
        } else if (btnSwitchCamera.isFocused()) {
            btnSwitchCamera.performClick();
        } else if (btnSwitchCamera2.isFocused()) {
            btnSwitchCamera2.performClick();
        } else if (btnExitApp.isFocused()) {
            btnExitApp.performClick();
        } else if (btnPause.isFocused()) {
            btnPause.performClick();
        } else if (btnContinue.isFocused()) {
            btnContinue.performClick();
        } else if (btnStop.isFocused()) {
            btnStop.performClick();
        }
    }

    public void slideFocusChange(int DIR) {
        Log.d("RAMBO", "MyVideoController SlideChange = " + DIR);
        if (btnStart.isFocused()) {
            if (DIR == TouchHandlerListener.DIR_UP || DIR == TouchHandlerListener.DIR_LEFT) {
                btnExitApp.requestFocus();
            } else if (DIR == TouchHandlerListener.DIR_RIGHT) {
                btnSwitchCamera.requestFocus();
            }
        } else if (btnExitApp.isFocused()) {
            if (DIR == TouchHandlerListener.DIR_RIGHT) {
                if (btnSwitchCamera.getVisibility() == View.VISIBLE) {
                    btnSwitchCamera.requestFocus();
                } else {
                    if (btnPause.getVisibility() == View.VISIBLE) {
                        btnPause.requestFocus();
                    } else if (btnContinue.getVisibility() == View.VISIBLE) {
                        btnContinue.requestFocus();
                    }
                }
            } else if (DIR == TouchHandlerListener.DIR_DOWN) {
                if (btnStart.getVisibility() == VISIBLE) {
                    btnStart.requestFocus();
                } else {
                    if (btnContinue.getVisibility() == View.VISIBLE) {
                        btnContinue.requestFocus();
                    } else if (btnPause.getVisibility() == View.VISIBLE) {
                        btnPause.requestFocus();
                    }
                }
            }
        } else if (btnSwitchCamera.isFocused()) {
            if (DIR == TouchHandlerListener.DIR_LEFT) {
                btnExitApp.requestFocus();
            } else if (DIR == TouchHandlerListener.DIR_DOWN) {
                btnStart.requestFocus();
            }
        } else if (btnPause.isFocused()) {
            if (DIR == TouchHandlerListener.DIR_LEFT || DIR == TouchHandlerListener.DIR_UP) {
                btnExitApp.requestFocus();
            } else if (DIR == TouchHandlerListener.DIR_RIGHT) {
                btnStop.requestFocus();
            }
        } else if (btnContinue.isFocused()) {
            if (DIR == TouchHandlerListener.DIR_LEFT || DIR == TouchHandlerListener.DIR_UP) {
                btnExitApp.requestFocus();
            } else if (DIR == TouchHandlerListener.DIR_RIGHT) {
                btnStop.requestFocus();
            }
        } else if (btnStop.isFocused()) {
            if (DIR == TouchHandlerListener.DIR_LEFT) {
                if (btnContinue.getVisibility() == VISIBLE) {
                    btnContinue.requestFocus();
                } else if (btnPause.getVisibility() == VISIBLE) {
                    btnPause.requestFocus();
                }
            } else if (DIR == TouchHandlerListener.DIR_RIGHT) {
                btnSwitchCamera2.requestFocus();
            } else if (DIR == TouchHandlerListener.DIR_UP) {
                btnExitApp.requestFocus();
            }
        } else if (btnSwitchCamera2.isFocused()) {
            if (DIR == TouchHandlerListener.DIR_LEFT) {
                Log.d("RAMBO","当前镜头2，点击左边按钮了");
                btnStop.requestFocus();
            } else if (DIR == TouchHandlerListener.DIR_UP) {
                btnExitApp.requestFocus();
            }
        }
    }

}
