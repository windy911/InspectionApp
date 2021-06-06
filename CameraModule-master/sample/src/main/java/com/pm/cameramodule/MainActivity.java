package com.pm.cameramodule;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.pm.cameraui.CameraActivity;
import com.pm.cameraui.Constants;
import com.pm.cameraui.base.BaseActivity;
import com.pm.cameraui.bean.UserInfo;
import com.pm.cameraui.mvp.MainPresenter;
import com.pm.cameraui.mvp.MainView;
import com.pm.cameraui.utils.SPHelp;
import com.pm.cameraui.widget.TouchHandlerListener;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {



    EditText edtLoginName;
    EditText edtLoginPswd;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void addListener() {

    }

    public void initView() {
        edtLoginName = findViewById(R.id.edtLoginName);
        edtLoginPswd = findViewById(R.id.edtLoginPswd);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        String userName = SPHelp.getInstance(getActivity()).getStringValue(SPHelp.SP_LOGIN_NAME);
        String password = SPHelp.getInstance(getActivity()).getStringValue(SPHelp.SP_LOGIN_PASSWORD);
        if ((!TextUtils.isEmpty(userName)) && (!TextUtils.isEmpty(password))) {
            edtLoginName.setText(userName);
            edtLoginPswd.setText(password);
        }

        edtLoginName.requestFocus();
    }

    public void login() {
        presenter.login(edtLoginName.getText().toString().trim(), edtLoginPswd.getText().toString().trim());

    }

    @Override
    public void onLogin(UserInfo o) {
//        Toast.makeText(getApplication(), "登录成功！" + o.getUserId(), Toast.LENGTH_SHORT).show();
        Constants.userInfo = o;
        startActivity(CameraActivity.newIntent(MainActivity.this, CameraActivity.TYPE_VIDEO));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KillApp();
    }

    public void KillApp() {
        Log.d("RAMBO", "KILL APP");
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public Activity getActivity() {
        return this;
    }



//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
////        mHandler.removeCallbacks(mLongPressRunnable);
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
////            mMyTouchHandler.setTouchDown(ev);
////            mHandler.postDelayed(mLongPressRunnable, 1200);
////            mLastTouchDir = 0;
//            return true;
//        }
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
////            mMyTouchHandler.handleTouchMove(ev);
//            return true;
//        }
//        if (ev.getAction() == MotionEvent.ACTION_UP) {
////            mMyTouchHandler.setTouchUp(ev);
//            return true;
//        }
//
//
//        return true;
//
//    }

}
