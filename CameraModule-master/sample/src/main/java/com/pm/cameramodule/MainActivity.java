package com.pm.cameramodule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.pm.cameraui.CameraActivity;
import com.pm.cameraui.Constants;
import com.pm.cameraui.base.BaseActivity;
import com.pm.cameraui.base.MyGestureListener;
import com.pm.cameraui.base.MyRightLeftListener;
import com.pm.cameraui.bean.UserInfo;
import com.pm.cameraui.mvp.MainPresenter;
import com.pm.cameraui.mvp.MainView;
import com.pm.cameraui.utils.SPHelp;
import com.pm.cameraui.utils.ToastUtils;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {


    EditText edtLoginName;
    EditText edtLoginPswd;
    Button btnLogin;
    CheckBox cbRemmenber;

    GestureDetector detector;


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

    public void doClickAnim(View view) {
        if(!Constants.isAniClick)return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation setAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view_click);
                view.startAnimation(setAnim);
            }
        });
    }

    public void initView() {
        edtLoginName = findViewById(R.id.edtLoginName);
        edtLoginPswd = findViewById(R.id.edtLoginPswd);
        cbRemmenber = findViewById(R.id.cbRemmenber);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setFocusable(true);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doClickAnim(view);
                login();
            }
        });

        btnLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                btnLogin.setAlpha(b ? 1.0f : 0.3f);
            }
        });


        btnLogin.requestFocus();

        boolean isRemmember = SPHelp.getInstance(getActivity()).getBooleanValue(SPHelp.SP_IS_REMMEMBER, true);
        cbRemmenber.setChecked(isRemmember);

        //默认会自动记住用户名，密码必须勾选才记住
        String userName = SPHelp.getInstance(getActivity()).getStringValue(SPHelp.SP_LOGIN_NAME);
        edtLoginName.setText(userName);

        if (cbRemmenber.isChecked()) {
            String password = SPHelp.getInstance(getActivity()).getStringValue(SPHelp.SP_LOGIN_PASSWORD);
            if (!TextUtils.isEmpty(password)) {
                edtLoginPswd.setText(password);
            }
        }

//        edtLoginName.setText("17700010002");
//        edtLoginPswd.setText("Hqwe123");


        detector = new GestureDetector(this, new MyGestureListener(
                new MyRightLeftListener() {
                    @Override
                    public void onRight() {
                        slideFocusChange(MyGestureListener.DIR_RIGHT);
                    }

                    @Override
                    public void onLeft() {
                        slideFocusChange(MyGestureListener.DIR_LEFT);
                    }

                    @Override
                    public void onUp() {
                        // TODO Auto-generated method stub
                        slideFocusChange(MyGestureListener.DIR_UP);
                    }

                    @Override
                    public void onDown() {
                        slideFocusChange(MyGestureListener.DIR_DOWN);
                    }

                    @Override
                    public void onSlide() {

                    }

                    @Override
                    public void onSingleTapUp() {
                        clicked();
                    }
                }));
    }

    public void login() {
        if (TextUtils.isEmpty(edtLoginPswd.getText()) || TextUtils.isEmpty(edtLoginName.getText())) {
            ToastUtils.show(getActivity(), "请先输入账号和密码");
            return;
        }

        if (!presenter.isLoading) {
            presenter.login(edtLoginName.getText().toString().trim(), edtLoginPswd.getText().toString().trim());
        }
    }

    @Override
    public void onLogin(UserInfo o) {
        //登录成功保存用户名，密码根据checkbox
        Constants.userInfo = o;
        startActivity(CameraActivity.newIntent(MainActivity.this, CameraActivity.TYPE_VIDEO));


        SPHelp.getInstance(getActivity()).setStringValue(SPHelp.SP_LOGIN_NAME, edtLoginName.getText().toString());
        if (!cbRemmenber.isChecked()) {
//            edtLoginName.setText("");
            edtLoginPswd.setText("");
            SPHelp.getInstance(getActivity()).setStringValue(SPHelp.SP_LOGIN_PASSWORD, "");
        }else {
            SPHelp.getInstance(getActivity()).setStringValue(SPHelp.SP_LOGIN_PASSWORD, edtLoginPswd.getText().toString());
        }
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


    public void slideFocusChange(int DIR) {
        if (edtLoginName.isFocused() && DIR == MyGestureListener.DIR_DOWN) {
            edtLoginPswd.requestFocus();
            edtLoginPswd.setSelection(edtLoginPswd.getText().length());
        } else if (edtLoginPswd.isFocused()) {
            if (DIR == MyGestureListener.DIR_UP) {
                edtLoginName.requestFocus();
                edtLoginName.setSelection(edtLoginName.getText().length());
            } else if (DIR == MyGestureListener.DIR_DOWN) {
                cbRemmenber.requestFocus();
            }
        } else if (cbRemmenber.isFocused()) {
            if (DIR == MyGestureListener.DIR_UP) {
                edtLoginPswd.requestFocus();
                edtLoginPswd.setSelection(edtLoginPswd.getText().length());
            } else if (DIR == MyGestureListener.DIR_DOWN) {
                btnLogin.requestFocus();
            }
        } else if (btnLogin.isFocused() && DIR == MyGestureListener.DIR_UP) {
            cbRemmenber.requestFocus();
        }
    }

    public void clicked() {
        if (edtLoginName.isFocused()) {
            showSoftInputFromWindow(getActivity(), edtLoginName);
        } else if (edtLoginPswd.isFocused()) {
            showSoftInputFromWindow(getActivity(), edtLoginPswd);
        } else if (btnLogin.isFocused()) {
            btnLogin.performClick();
        } else if (cbRemmenber.isFocused()) {
            clickedRemember();
        }
    }

    public void clickedRemember() {
        boolean isChecked = cbRemmenber.isChecked();
        cbRemmenber.setChecked(!isChecked);
        SPHelp.getInstance(getActivity()).setBooleanValue(SPHelp.SP_IS_REMMEMBER, cbRemmenber.isChecked());
    }


    /**
     * 触屏事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //显示软键盘
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(editText, 0);
//        editText.requestFocus();
//        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);

    }
}
