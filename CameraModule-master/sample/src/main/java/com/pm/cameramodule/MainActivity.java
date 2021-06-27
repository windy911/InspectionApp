package com.pm.cameramodule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.pm.cameraui.CameraActivity;
import com.pm.cameraui.Constants;
import com.pm.cameraui.base.BaseActivity;
import com.pm.cameraui.bean.UserInfo;
import com.pm.cameraui.mvp.MainPresenter;
import com.pm.cameraui.mvp.MainView;
import com.pm.cameraui.utils.SPHelp;
import com.pm.cameraui.utils.ToastUtils;
import com.pm.cameraui.widget.TouchHandlerListener;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView, TouchHandlerListener {


    EditText edtLoginName;
    EditText edtLoginPswd;
    Button btnLogin;
    CheckBox cbRemmenber;

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
        cbRemmenber = findViewById(R.id.cbRemmenber);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setFocusable(true);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });



        btnLogin.requestFocus();

        boolean isRemmember = SPHelp.getInstance(getActivity()).getBooleanValue(SPHelp.SP_IS_REMMEMBER,true);
        cbRemmenber.setChecked(isRemmember);


        if(cbRemmenber.isChecked()){
            String userName = SPHelp.getInstance(getActivity()).getStringValue(SPHelp.SP_LOGIN_NAME);
            String password = SPHelp.getInstance(getActivity()).getStringValue(SPHelp.SP_LOGIN_PASSWORD);
            if ((!TextUtils.isEmpty(userName)) && (!TextUtils.isEmpty(password))) {
                edtLoginName.setText(userName);
                edtLoginPswd.setText(password);
            }
        }
    }

    public void login() {
        if(TextUtils.isEmpty(edtLoginPswd.getText())||TextUtils.isEmpty(edtLoginName.getText())){
            ToastUtils.show(getActivity(),"请先输入账号和密码");
            return;
        }

        if(!presenter.isLoading){
            presenter.login(edtLoginName.getText().toString().trim(), edtLoginPswd.getText().toString().trim());
        }
    }

    @Override
    public void onLogin(UserInfo o) {
//        Toast.makeText(getApplication(), "登录成功！" + o.getUserId(), Toast.LENGTH_SHORT).show();
        Constants.userInfo = o;
        startActivity(CameraActivity.newIntent(MainActivity.this, CameraActivity.TYPE_VIDEO));

        if(!cbRemmenber.isChecked()){
            edtLoginName.setText("");
            edtLoginPswd.setText("");
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
        if (edtLoginName.isFocused() && DIR == TouchHandlerListener.DIR_DOWN) {
            edtLoginPswd.requestFocus();
            edtLoginPswd.setSelection(edtLoginPswd.getText().length());
        } else if (edtLoginPswd.isFocused()) {
            if (DIR == TouchHandlerListener.DIR_UP) {
                edtLoginName.requestFocus();
                edtLoginName.setSelection(edtLoginName.getText().length());
            } else if (DIR == TouchHandlerListener.DIR_DOWN) {
                cbRemmenber.requestFocus();
            }
        } else if(cbRemmenber.isFocused()){
            if (DIR == TouchHandlerListener.DIR_UP) {
                edtLoginPswd.requestFocus();
                edtLoginPswd.setSelection(edtLoginPswd.getText().length());
            } else if (DIR == TouchHandlerListener.DIR_DOWN) {
                btnLogin.requestFocus();
            }
        } else if (btnLogin.isFocused() && DIR == TouchHandlerListener.DIR_UP) {
            cbRemmenber.requestFocus();
        }
    }

    public void clicked() {
        if (edtLoginName.isFocused()) {
            showSoftInputFromWindow(getActivity(),edtLoginName);
        } else if (edtLoginPswd.isFocused()) {
            showSoftInputFromWindow(getActivity(),edtLoginPswd);
        } else if (btnLogin.isFocused()) {
            btnLogin.performClick();
        } else if(cbRemmenber.isFocused()){
            clickedRemember();
        }
    }

    public void clickedRemember(){
        boolean isChecked = cbRemmenber.isChecked();
        cbRemmenber.setChecked(!isChecked);
        SPHelp.getInstance(getActivity()).setBooleanValue(SPHelp.SP_IS_REMMEMBER,cbRemmenber.isChecked());
    }


    @Override
    public void updateCursorPos(int cursorPos) {

    }

    @Override
    public void doScrollX(int dx) {

    }

    @Override
    public void doFling(int speedX) {

    }

    @Override
    public void doTouchupNoMove() {
        clicked();
    }

    @Override
    public void doTouchUp() {

    }

    @Override
    public void slideDown() {
        slideFocusChange(TouchHandlerListener.DIR_DOWN);
    }

    @Override
    public void slideUp() {
        slideFocusChange(TouchHandlerListener.DIR_UP);
    }

    @Override
    public void slideLeft() {
        slideFocusChange(TouchHandlerListener.DIR_LEFT);
    }

    @Override
    public void slideRight() {
        slideFocusChange(TouchHandlerListener.DIR_RIGHT);
    }


    private float downX;    //按下时 的X坐标
    private float downY;    //按下时 的Y坐标

    /**
     * 根据距离差判断 滑动方向
     *
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        Log.e("Tag", "========X轴距离差：" + dx);
        Log.e("Tag", "========Y轴距离差：" + dy);
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? 'r' : 'l';
        } else {
            //Y轴移动
            return dy > 0 ? 'b' : 't';
        }
    }

    /**
     * 触屏事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String action = "";
        //在触发时回去到起始坐标
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //将按下时的坐标存储
                downX = x;
                downY = y;
                Log.e("Tag", "=======按下时X：" + x);
                Log.e("Tag", "=======按下时Y：" + y);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("Tag", "=======抬起时X：" + x);
                Log.e("Tag", "=======抬起时Y：" + y);

                //获取到距离差
                float dx = x - downX;
                float dy = y - downY;
                //防止是按下也判断
                if (Math.abs(dx) > 1 && Math.abs(dy) > 1) {
                    //通过距离差判断方向
                    int orientation = getOrientation(dx, dy);
                    switch (orientation) {
                        case 'r':
                            action = "右";
                            slideRight();
                            break;
                        case 'l':
                            action = "左";
                            slideLeft();
                            break;
                        case 't':
                            action = "上";
                            slideUp();
                            break;
                        case 'b':
                            action = "下";
                            slideDown();
                            break;
                    }
                }else {
                    clicked();
                }
                break;
        }
        return false;
//         return super.onTouchEvent(event);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public  void showSoftInputFromWindow(Activity activity, EditText editText) {
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
        imm.showSoftInput(editText,0);

    }
}
