package com.pm.cameraui.widget;

import android.app.Activity;
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
import android.widget.Toast;

import com.pm.cameraui.Constants;
import com.pm.cameraui.R;
import com.pm.cameraui.base.MyGestureListener;
import com.pm.cameraui.utils.DeviceUtil;
import com.pm.cameraui.utils.TimeUtil;
import com.pm.cameraui.utils.ToastUtils;

public class MyVidoeController extends RelativeLayout {

    CameraController.ControllerCallback mCallback;
    Button btnStart, btnSwitchCamera, btnSwitchCamera2, btnStop, btnContinue, btnPause, btnExitApp;
    LinearLayout llRecording;
    LinearLayout llbtnContinue,llbtnPause;
    TextView tvRedDot, tvTimer;
    ProgressBar voiceProgress;
    LinearLayout llVoiceMark;
    LinearLayout llMarkSuccess;
    LinearLayout llImageMarking;
    boolean isTaskRecording = false;

    boolean isAniVoiceMarkerShowing = false;

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
        llMarkSuccess = findViewById(R.id.llMarkSuccess);
        llImageMarking = findViewById(R.id.llImageMarking);
        voiceProgress = findViewById(R.id.voiceProgress);
        btnExitApp = findViewById(R.id.btnExitApp);
        btnStart.setOnClickListener(view -> {

            if(!DeviceUtil.checkDiskSize()){
                Toast.makeText(getContext(),"存储空间不足，请清理设备后使用!",Toast.LENGTH_SHORT).show();
                return;
            }

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

        btnStart.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                btnStart.setAlpha(b?0.95f:0.5f);
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

        Animation setAnim = AnimationUtils.loadAnimation(getContext(), R.anim.view_translate_top_in);
        setAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {//动画开始的时候的监听，

            }

            @Override
            public void onAnimationRepeat(Animation animation) {//动画重复的时候监听
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {//动画结束的时候监听

            }
        });

        if(!isAniVoiceMarkerShowing){
            isAniVoiceMarkerShowing =true;
            llVoiceMark.startAnimation(setAnim);
        }

    }

    public void hideVoiceMarker() {
        llVoiceMark.setVisibility(View.GONE);
        isAniVoiceMarkerShowing =false;
    }

    public void showBackgroundAction(boolean isShow) {
        btnSwitchCamera.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnExitApp.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnStart.setVisibility(isShow?View.VISIBLE:View.GONE);
    }


    public void onClicked() {
        if (btnStart.isFocused()) {
            btnStart.performClick();
            doClickAnim(btnStart);
        } else if (btnSwitchCamera.isFocused()) {
            btnSwitchCamera.performClick();
            doClickAnim(btnSwitchCamera);
        } else if (btnSwitchCamera2.isFocused()) {
            btnSwitchCamera2.performClick();
            doClickAnim(btnSwitchCamera2);
        } else if (btnExitApp.isFocused()) {
            btnExitApp.performClick();
            doClickAnim(btnExitApp);
        } else if (btnPause.isFocused()) {
            btnPause.performClick();
            doClickAnim(btnPause);
        } else if (btnContinue.isFocused()) {
            btnContinue.performClick();
            doClickAnim(btnContinue);
        } else if (btnStop.isFocused()) {
            btnStop.performClick();
            doClickAnim(btnStop);
        }
    }

    public void facousPauseOrContinue() {
        if (llbtnContinue.getVisibility() == View.VISIBLE) {
            btnContinue.requestFocus();
        } else if (llbtnPause.getVisibility() == View.VISIBLE) {
            btnPause.requestFocus();
        }
    }

    public void showImageMarking(){
        llImageMarking.setVisibility(View.VISIBLE);
        Animation setAnim = AnimationUtils.loadAnimation(getContext(), R.anim.view_translate_top_in);
        llImageMarking.startAnimation(setAnim);
    }
    public void hideImageMarkding(){
        llImageMarking.setVisibility(View.GONE);
    }

    public void showMarkSuccess(){

        llMarkSuccess.setVisibility(View.VISIBLE);

        Animation setAnim = AnimationUtils.loadAnimation(getContext(), R.anim.view_translate_top_in);
        llMarkSuccess.startAnimation(setAnim);


    }

    public void hideMarkSuccess(){
        llMarkSuccess.setVisibility(View.GONE);
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
            } else if(DIR == MyGestureListener.DIR_RIGHT){

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

    public void hideControler(){
        llRecording.setVisibility(View.GONE);
    }

    public void doClickAnim(View view) {
        if(!Constants.isAniClick)return;
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation setAnim = AnimationUtils.loadAnimation(getContext(), R.anim.view_click);
                view.startAnimation(setAnim);
            }
        });
    }
}
