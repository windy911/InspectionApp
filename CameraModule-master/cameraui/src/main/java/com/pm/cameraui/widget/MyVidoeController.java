package com.pm.cameraui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pm.cameraui.R;
import com.pm.cameraui.base.MyGestureListener;
import com.pm.cameraui.utils.TimeUtil;

public class MyVidoeController extends RelativeLayout {

    CameraController.ControllerCallback mCallback;
    Button btnStart, btnSwitchCamera, btnSwitchCamera2, btnStop, btnContinue, btnPause, btnExitApp;
    LinearLayout llRecording;
    LinearLayout llbtnContinue,llbtnPause;
    TextView tvRedDot, tvTimer;
    ProgressBar voiceProgress;
    LinearLayout llVoiceMark;
    boolean isTaskRecording = false;

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
        isTaskRecording = false;
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnContinue = findViewById(R.id.btnContinue);
        llbtnPause = findViewById(R.id.llbtnPause);
        llbtnContinue = findViewById(R.id.llbtnContinue);
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
        btnExitApp.setVisibility(isRecording ? View.GONE : View.VISIBLE);
        llRecording.setVisibility(isRecording ? View.VISIBLE : View.GONE);
        btnSwitchCamera.setVisibility(isRecording ? View.GONE : View.VISIBLE);
        btnPause.requestFocus();
        isTaskRecording = isRecording;
    }

    public void startInit() {
        btnStart.requestFocus();
    }

    public void setPreViewImage(Bitmap bitmap) {
//        imgPreview.setBackground(new BitmapDrawable(bitmap));
    }

    public void pauseRecording() {
        llbtnPause.setVisibility(View.GONE);
        llbtnContinue.setVisibility(View.VISIBLE);
        btnContinue.requestFocus();
    }

    public void continueRecording() {
        llbtnPause.setVisibility(View.VISIBLE);
        llbtnContinue.setVisibility(View.GONE);
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
        voiceProgress.setProgress(100-progress);
    }

    public void hideVoiceMarker() {
        llVoiceMark.setVisibility(View.GONE);
    }

    public void showBackgroundAction(boolean isShow) {
        btnSwitchCamera.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnExitApp.setVisibility(isShow ? View.VISIBLE : View.GONE);
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

    public void facousPauseOrContinue() {
        if (llbtnContinue.getVisibility() == View.VISIBLE) {
            btnContinue.requestFocus();
        } else if (llbtnPause.getVisibility() == View.VISIBLE) {
            btnPause.requestFocus();
        }
    }

    public void slideFocusChange(int DIR) {


        Log.d("RAMBO", "MyVideoController SlideChange = " + DIR);

        if (DIR == MyGestureListener.DIR_UP && btnStart.getVisibility() == View.GONE && llRecording.getVisibility()==View.GONE) {
            showOrHideControl(true);
            facousPauseOrContinue();
            return;
        }


        if (btnStart.isFocused()) {
            if (DIR == MyGestureListener.DIR_UP || DIR == MyGestureListener.DIR_LEFT) {
                btnExitApp.requestFocus();
            } else if (DIR == MyGestureListener.DIR_RIGHT) {
                btnSwitchCamera.requestFocus();
            }
        } else if (btnExitApp.isFocused()) {
            if (DIR == MyGestureListener.DIR_RIGHT) {
                if (btnSwitchCamera.getVisibility() == View.VISIBLE) {
                    btnSwitchCamera.requestFocus();
                } else {
                    if (llbtnPause.getVisibility() == View.VISIBLE) {
                        btnPause.requestFocus();
                    } else if (llbtnContinue.getVisibility() == View.VISIBLE) {
                        btnContinue.requestFocus();
                    }
                }
            } else if (DIR == MyGestureListener.DIR_DOWN) {
                if (btnStart.getVisibility() == VISIBLE) {
                    btnStart.requestFocus();
                } else {
                    if (llbtnContinue.getVisibility() == View.VISIBLE) {
                        btnContinue.requestFocus();
                    } else if (llbtnPause.getVisibility() == View.VISIBLE) {
                        btnPause.requestFocus();
                    }
                }
            }
        } else if (btnSwitchCamera.isFocused()) {
            if (DIR == MyGestureListener.DIR_LEFT) {
                btnExitApp.requestFocus();
            } else if (DIR == MyGestureListener.DIR_DOWN) {
                btnStart.requestFocus();
            }
        } else if (btnPause.isFocused()) {
            if (DIR == MyGestureListener.DIR_LEFT) {
                btnExitApp.requestFocus();
            } else if (DIR == MyGestureListener.DIR_UP) {
//                showOrHideControl(true);
            } else if (DIR == MyGestureListener.DIR_RIGHT) {
                btnStop.requestFocus();
            } else if (DIR == MyGestureListener.DIR_DOWN) {
                showOrHideControl(false);
            }
        } else if (btnContinue.isFocused()) {
            if (DIR == MyGestureListener.DIR_LEFT) {
                btnExitApp.requestFocus();
            } else if (DIR == MyGestureListener.DIR_UP) {
//                showOrHideControl(true);
            } else if (DIR == MyGestureListener.DIR_RIGHT) {
                btnStop.requestFocus();
            } else if (DIR == MyGestureListener.DIR_DOWN) {
                showOrHideControl(false);
            }
        } else if (btnStop.isFocused()) {
            if (DIR == MyGestureListener.DIR_LEFT) {
                if (llbtnContinue.getVisibility() == VISIBLE) {
                    btnContinue.requestFocus();
                } else if (llbtnPause.getVisibility() == VISIBLE) {
                    btnPause.requestFocus();
                }
            } else if (DIR == MyGestureListener.DIR_RIGHT) {
                btnSwitchCamera2.requestFocus();
            } else if (DIR == MyGestureListener.DIR_UP) {
//                btnExitApp.requestFocus();
//                showOrHideControl(true);
            } else if (DIR == MyGestureListener.DIR_DOWN) {
                showOrHideControl(false);
            }
        } else if (btnSwitchCamera2.isFocused()) {
            if (DIR == MyGestureListener.DIR_LEFT) {
                Log.d("RAMBO", "当前镜头2，点击左边按钮了");
                btnStop.requestFocus();
            } else if (DIR == MyGestureListener.DIR_UP) {
//                btnExitApp.requestFocus();
//                showOrHideControl(true);
            } else if (DIR == MyGestureListener.DIR_DOWN) {
                showOrHideControl(false);
            }
        }
    }

    public boolean isStarted() {
        return !(btnStart.getVisibility() == View.VISIBLE);
    }

    public void showOrHideControl(boolean isShow) {

        if (!isTaskRecording) {
            return;
        }


        // 初始化需要加载的动画资源
        Animation animation = AnimationUtils.loadAnimation(getContext(), isShow ? R.anim.view_translate_in : R.anim.view_translate_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                llRecording.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llRecording.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        llRecording.startAnimation(animation);
    }


}
