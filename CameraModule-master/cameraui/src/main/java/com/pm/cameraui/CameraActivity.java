package com.pm.cameraui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.pm.cameraui.base.MyGestureListener;
import com.pm.cameraui.base.MyRightLeftListener;
import com.pm.cameraui.bean.InsLocation;
import com.pm.cameraui.utils.LocationUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class CameraActivity extends AppCompatActivity {
    private static final String CAMERA_TYPE_INTENT_NAME = "camera_type";
    public static final String TYPE_PICTURE = "picture";
    public static final String TYPE_VIDEO = "video";
    public Fragment fragment;
    public static CameraActivity instance = null;
    GestureDetector detector;

    public static String longitude, latitude;

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


        detector = new GestureDetector(this, new MyGestureListener(
                new MyRightLeftListener() {
                    @Override
                    public void onRight() {
                        ((VideoFragment) fragment).slideRight();
                    }

                    @Override
                    public void onLeft() {
                        ((VideoFragment) fragment).slideLeft();
                    }

                    @Override
                    public void onUp() {
                        ((VideoFragment) fragment).slideUp();
                    }

                    @Override
                    public void onDown() {
                        ((VideoFragment) fragment).slideDown();
                    }

                    @Override
                    public void onSlide() {

                    }

                    @Override
                    public void onSingleTapUp() {
                        clicked();
                    }
                }));

        longitude = "";
        latitude = "";
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("RAMBO","CameraActivity onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigation();
    }

    public void hideNavigation() {
        //????????????
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
        //????????????????????????
        this.overridePendingTransition(0, R.anim.activity_down_slid);
    }

    @Override
    public void onBackPressed() {
//        showExitDialog();
    }

//    public void showExitDialog(){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
//        builder.setTitle("?????????");
//        builder.setMessage("?????????????????????");
//        //??????????????????
//        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                CameraActivity.this.finish();
//            }
//        });
//        //??????????????????
//        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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

    private long ClickedLastTimer = System.currentTimeMillis();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // || KeyEvent.KEYCODE_VOLUME_DOWN == keyCode
        if (KeyEvent.KEYCODE_ALL_APPS == keyCode) {
            if ((System.currentTimeMillis() - ClickedLastTimer) > 1000) {
                ((VideoFragment) fragment).onVoiceKeyDown();
                ClickedLastTimer = System.currentTimeMillis();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // || KeyEvent.KEYCODE_VOLUME_DOWN == keyCode
        if (KeyEvent.KEYCODE_ALL_APPS == keyCode) {
            ((VideoFragment) fragment).onVoiceKeyUp();
        }
        return super.onKeyUp(keyCode, event);
    }

    //??????AMapLocationClient?????????
    public AMapLocationClient mLocationClient = null;


    public void location() {
        //???????????????
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //????????????????????????
        restartListener();
        mLocationClient.setLocationListener(mAMapLocationListener);
        //????????????
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setInterval(5000);
        mLocationClient.setLocationOption(option);
        mLocationClient.startLocation();
        //????????????????????????
//        }
    }

    AMapLocationListener mAMapLocationListener;

    public void restartListener() {
        mAMapLocationListener = null;
        mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //??????????????????
                        if (LocationUtil.isEnableRecording()) {
                            LocationUtil.locationList.add(new InsLocation(amapLocation.getLatitude() + "", amapLocation.getLongitude() + "", "", System.currentTimeMillis()));
                            //??????????????????amapLocation?????????????????????
                            Log.e("RAMBO", "size = " + LocationUtil.locationList.size() + " ??????:" + amapLocation.getLatitude() + ", ??????:" + amapLocation.getLongitude());
                            longitude = String.valueOf(amapLocation.getLongitude());
                            latitude = String.valueOf(amapLocation.getLatitude());
                        }
                    }
                }
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }


    public long lastClickedTime = System.currentTimeMillis();

    public void clicked() {
        if (System.currentTimeMillis() - lastClickedTime < 1000) {
            //??????????????????????????????
            return;
        }
        lastClickedTime = System.currentTimeMillis();
        ((VideoFragment) fragment).clicked();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

}
