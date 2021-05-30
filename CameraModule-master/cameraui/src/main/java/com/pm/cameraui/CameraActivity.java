package com.pm.cameraui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class CameraActivity extends AppCompatActivity {
    private static final String CAMERA_TYPE_INTENT_NAME = "camera_type";
    public static final String TYPE_PICTURE = "picture";
    public static final String TYPE_VIDEO = "video";
    public Fragment fragment;
    public static CameraActivity instance = null;

    public static Intent newIntent(Context context, String cameraType) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra(CAMERA_TYPE_INTENT_NAME, cameraType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        String cameraType = getIntent().getStringExtra(CAMERA_TYPE_INTENT_NAME);
        if (null == savedInstanceState) {
            fragment = VideoFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, cameraType.equals(TYPE_PICTURE) ? PictureFragment.newInstance() : fragment)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigation();
    }

    public void hideNavigation(){
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0, R.anim.activity_down_slid);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    public void showExitDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
        builder.setTitle("提示：");
        builder.setMessage("确认要退出吗？");
        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CameraActivity.this.finish();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                hideNavigation();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        hideNavigation();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_ALL_APPS == keyCode){
            ((VideoFragment)fragment).onVoiceKeyDown();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_ALL_APPS == keyCode){
            ((VideoFragment)fragment).onVoiceKeyUp();
        }
        return super.onKeyUp(keyCode, event);
    }

}
