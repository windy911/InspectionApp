package com.pm.cameraui;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.pm.cameracore.CaptureDelegate;
import com.pm.cameracore.DelegateCallback;
import com.pm.cameracore.RecordDelegate;
import com.pm.cameraui.widget.AutoFitTextureView;
import com.pm.cameraui.widget.CameraController;
import com.pm.cameraui.widget.CaptureButton;
import com.pm.cameraui.widget.MyVidoeController;
import com.pm.cameraui.widget.VideoViewController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VideoFragment extends BaseCameraFragment implements DelegateCallback {

    private AutoFitTextureView mTextureView;
    private VideoViewController mVideoViewController;
    private CameraController mController;
    private RecordDelegate mRecordDelegate;
    private MyVidoeController myVideoController;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VideoFragment.
     */
    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        // TODO: 2019/7/26
        fragment.setArguments(args);
        return fragment;
    }

    public VideoFragment() {
        super(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecordDelegate = new RecordDelegate(getActivity(), this);


        mTextureView = (AutoFitTextureView) view.findViewById(R.id.texture);
        mController = view.findViewById(R.id.controller);
        mVideoViewController = view.findViewById(R.id.vv_controller);
        myVideoController = view.findViewById(R.id.myVideoController);
        myVideoController.setControllerCallback(new CameraController.ControllerCallback() {
            @Override
            public void takePicture() {
                mRecordDelegate.takePicture();
            }

            @Override
            public void recordStart() {
                mRecordDelegate.startRecordingVideo();
                myVideoController.refreshUIRecord(true);
            }

            @Override
            public void recordStop() {
                //真正退出，不及时预览
                mRecordDelegate.stopRecordingVideo(true);
                myVideoController.refreshUIRecord(false);
                onPrepareCamera(mTextureView.getWidth(), mTextureView.getHeight());
            }

            @Override
            public void onCancel() {
                mVideoViewController.hide();
                Log.d("RAMBO","onCancel");
            }

            @Override
            public void onConfirm() {
                mVideoViewController.hide();
            }

            @Override
            public void onClose() {
                if (getActivity() != null) {
                    getActivity().finishAfterTransition();
                }
            }

            @Override
            public void onSwitchCamera() {
                Log.d("RAMBO","onCustomRight");
                switchCameraID();
            }
        });


        mController.setVisibility(View.GONE);

//        mController.setDuration(10);
//        mController.setAction(CaptureButton.Action.RECORD_VIDEO);
//        mController.setRecordPressMode(CaptureButton.PressMode.CLICK);
//        mController.showRightCustomView();
//        mController.setIconSrc(0,R.drawable.ic_switch_camera);
//        mController.setControllerCallback(new CameraController.ControllerCallback() {
//            @Override
//            public void takePicture() {
//            }
//
//            @Override
//            public void recordStart() {
//                mRecordDelegate.startRecordingVideo();
//                myVideoController.refreshUIRecord(true);
//            }
//
//            @Override
//            public void recordStop() {
//                mRecordDelegate.stopRecordingVideo(false);
//                myVideoController.refreshUIRecord(false);
//            }
//
//            @Override
//            public void onCancel() {
//                mVideoViewController.hide();
//                Log.d("RAMBO","onCancel");
//            }
//
//            @Override
//            public void onConfirm() {
//                mVideoViewController.hide();
//            }
//
//            @Override
//            public void onClose() {
//                if (getActivity() != null) {
//                    getActivity().finishAfterTransition();
//                }
//            }
//
//            @Override
//            public void onSwitchCamera() {
//                Log.d("RAMBO","onCustomRight");
//                switchCameraID();
//            }
//        });
    }

    public void switchCameraID(){
        mRecordDelegate.switchCameraID();
        mRecordDelegate.closeCamera();
        onPrepareCamera(mTextureView.getWidth(), mTextureView.getHeight());
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecordDelegate.startBackgroundThread();
        if (mTextureView.isAvailable()) {
            onPrepareCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecordDelegate.stopRecordingVideo(true);
        closeCamera();
        mRecordDelegate.stopBackgroundThread();
        mVideoViewController.hide();
    }

    @Override
    protected void openCamera(int width, int height) {
        mRecordDelegate.openCamera(width, height);
    }

    @Override
    protected void closeCamera() {
        mRecordDelegate.closeCamera();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        onPrepareCamera(width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}

    @Override
    public void onChangeViewSize(Size size) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mTextureView.setAspectRatio(size.getWidth(), size.getHeight());
        } else {
            mTextureView.setAspectRatio(size.getHeight(), size.getWidth());
        }
    }

    @Override
    public void onTransformView(Matrix matrix) {
        mTextureView.setTransform(matrix);
    }

    @Override
    public SurfaceTexture getSurfaceTexture() {
        return mTextureView.getSurfaceTexture();
    }

    @Override
    public void onCaptureResult(Bitmap bitmap) {
        if(bitmap!=null){
            Log.d("RAMBO","onCaptureResult: bitmap count = "+bitmap.getByteCount());
        }
        myVideoController.setPreViewImage(bitmap);
    }

    @Override
    public void onRecordResult(Bitmap coverBitmap, String videoAbsolutePath) {
        Log.d("RAMBO","VideoAbsolutePath:"+videoAbsolutePath);
//        mVideoViewController.show(coverBitmap,videoAbsolutePath);

    }

}
