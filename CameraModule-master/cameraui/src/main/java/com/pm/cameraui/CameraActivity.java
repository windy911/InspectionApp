package com.pm.cameraui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.pm.cameraui.bean.InsLocation;
import com.pm.cameraui.utils.LocationUtil;
import com.pm.cameraui.widget.TouchHandlerListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class CameraActivity extends AppCompatActivity implements TouchHandlerListener {
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

    public void hideNavigation() {
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
//        showExitDialog();
    }

//    public void showExitDialog(){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
//        builder.setTitle("提示：");
//        builder.setMessage("确认要退出吗？");
//        //设置正面按钮
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                CameraActivity.this.finish();
//            }
//        });
//        //设置反面按钮
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                hideNavigation();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        hideNavigation();
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_ALL_APPS == keyCode) {
            ((VideoFragment) fragment).onVoiceKeyDown();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_ALL_APPS == keyCode) {
            ((VideoFragment) fragment).onVoiceKeyUp();
        }
        return super.onKeyUp(keyCode, event);
    }


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;


    public void location() {
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mAMapLocationListener);
            //启动定位
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setInterval(5000);
            mLocationClient.setLocationOption(option);
            mLocationClient.startLocation();
            //异步获取定位结果
        }
    }


    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            Log.d("RAMBO", "onLocationChanged " + amapLocation.toStr());
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //解析定位结果
                    if (LocationUtil.isEnableRecording()) {
                        LocationUtil.locationList.add(new InsLocation(amapLocation.getLatitude() + "", amapLocation.getLongitude() + "", "", System.currentTimeMillis()));
                        //可在其中解析amapLocation获取相应内容。
                        Log.e("RAMBO", "size = " + LocationUtil.locationList.size() + " 经度:" + amapLocation.getLatitude() + ", 维度:" + amapLocation.getLongitude());
//                        Toast.makeText(CameraActivity.this, " 经度:" + amapLocation.getLatitude() + ", 维度:" + amapLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }


    public void slideFocusChange(int DIR) {
        Log.d("RAMBO", "DIR = " + DIR);
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

    }

    @Override
    public void doTouchUp() {

    }

    @Override
    public void slideDown() {
        ((VideoFragment) fragment).slideDown();
    }

    @Override
    public void slideUp() {
        ((VideoFragment) fragment).slideUp();
    }

    @Override
    public void slideLeft() {
        ((VideoFragment) fragment).slideLeft();
    }

    @Override
    public void slideRight() {
        ((VideoFragment) fragment).slideRight();
    }


    public long lastClickedTime = System.currentTimeMillis();

    public void clicked() {
        if (System.currentTimeMillis() - lastClickedTime < 1000) {
                //防止过快产生点击事件
                return;
        }
        lastClickedTime = System.currentTimeMillis();
        ((VideoFragment) fragment).clicked();
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

    int touchStatus = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        String action = "";
        //在触发时回去到起始坐标
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (touchStatus == 0) {
                    touchStatus = 1;
                } else {
                    touchStatus = 0;
                    return true;
                }

                //将按下时的坐标存储
                downX = x;
                downY = y;
                Log.e("Tag", "=======按下时X：" + x);
                Log.e("Tag", "=======按下时Y：" + y);
                break;
            case MotionEvent.ACTION_UP:
                if (touchStatus == 1) {
                    touchStatus = 0;
                } else {
                    touchStatus = 0;
                    return true;
                }

                Log.e("Tag", "=======抬起时X：" + x);
                Log.e("Tag", "=======抬起时Y：" + y);


                if (downX == 0 || downY == 0) {
                    return true;
                }
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
                } else {
                    clicked();
                }

                downX = 0;
                downY = 0;
                break;
        }

        return false;
//      return super.onTouchEvent(event);
    }

}
