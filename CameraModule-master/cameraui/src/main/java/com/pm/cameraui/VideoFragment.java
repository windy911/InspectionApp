package com.pm.cameraui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pm.cameracore.DelegateCallback;
import com.pm.cameracore.RecordDelegate;
import com.pm.cameraui.utils.UploadUtil;
import com.pm.cameraui.widget.AutoFitTextureView;
import com.pm.cameraui.widget.CameraController;
import com.pm.cameraui.widget.MyVidoeController;
import com.pm.cameraui.widget.VideoViewController;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VideoFragment extends BaseCameraFragment implements DelegateCallback {

    private AutoFitTextureView mTextureView;
    private CameraController mController;
    private RecordDelegate mRecordDelegate;
    private MyVidoeController myVideoController;
    private boolean isLastVideoPath = false;
    private long startTimer = System.currentTimeMillis();
    private long periodTime = 0;

    private List<String> recordFileList = new ArrayList<>();


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
//        mVideoViewController = view.findViewById(R.id.vv_controller);
        myVideoController = view.findViewById(R.id.myVideoController);
        myVideoController.setControllerCallback(new CameraController.ControllerCallback() {
            @Override
            public void takePicture() {
                mRecordDelegate.takePicture();
            }

            @Override
            public void recordStart() {
                isLastVideoPath = false;
                clearSaveList();
                startRecording();
            }

            @Override
            public void recordStop() {
                isLastVideoPath = true;
                //真正退出，不及时预览
                stopRecording();
            }

            @Override
            public void recordPause() {
                //真正退出，不及时预览
                pauseRecording();
            }

            @Override
            public void recordContinue() {
                continueRecording();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm() {
            }

            @Override
            public void onClose() {
                if (getActivity() != null) {
                    getActivity().finishAfterTransition();
                }
            }

            @Override
            public void onSwitchCamera() {
                Log.d("RAMBO", "onCustomRight");
                switchCameraID();
            }
        });

        mController.setVisibility(View.GONE);

    }


    public void switchCameraID() {
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
        myVideoController.setRecordDotShow(false);
        if (mRecordDelegate.isRecording()) {
            pauseRecording();
        }
    }

    public void startRecording() {
        startTimer = System.currentTimeMillis();
        mRecordDelegate.startRecordingVideo();
        myVideoController.refreshUIRecord(true);
        mRecordDelegate.startBackgroundThread();
        refreshTimer();
    }

    public void stopRecording() {
        if (mRecordDelegate.isRecording()) {
            mRecordDelegate.stopRecordingVideo(true);
            closeCamera();
            mRecordDelegate.stopBackgroundThread();
            onPrepareCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            if (isLastVideoPath) {
                mergeSaveList();
            }
        }
        myVideoController.continueRecording();
        myVideoController.refreshUIRecord(false);
    }

    public void pauseRecording() {
        mRecordDelegate.stopRecordingVideo(true);
        closeCamera();
        myVideoController.refreshUIRecord(true);
        mRecordDelegate.stopBackgroundThread();
        onPrepareCamera(mTextureView.getWidth(), mTextureView.getHeight());
        myVideoController.pauseRecording();
    }

    public void continueRecording() {
        myVideoController.continueRecording();
        startRecording();
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
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

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
        if (bitmap != null) {
            Log.d("RAMBO", "onCaptureResult: bitmap count = " + bitmap.getByteCount());
        }
        myVideoController.setPreViewImage(bitmap);
    }

    @Override
    public void onRecordResult(Bitmap coverBitmap, String videoAbsolutePath) {
        Log.d("RAMBO", "Video 生成文件:" + videoAbsolutePath);

        addToSaveList(videoAbsolutePath);
        if (isLastVideoPath) {
            mergeSaveList();
        }

    }

    public void addToSaveList(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            recordFileList.add(filePath);
        }
    }

    //记录视频的列表
    public void clearSaveList() {
        if (recordFileList != null) {
            recordFileList.clear();
        }
    }

    //合成列表的视频成为一个整视频
    public void mergeSaveList() {
        Log.d("RAMBO", "视频任务拍摄结束：开始MergeSaveList");
        resetUI();
        String mergeVideoPath = createVideoFilePath(getActivity());
        UploadUtil.mergeVideos(getActivity(), mergeVideoPath, recordFileList, new UploadUtil.OnMergeSuccessListener() {
            @Override
            public void onMergeSuccess(String outFile) {
                Log.d("RAMBO", "合成成功了文件：" + outFile);
                clearSaveList();
            }
        });
    }

    private String createVideoFilePath(Context context) {
        final File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSSS", Locale.getDefault());
        String dateStr = dateFormat.format(new Date());
        return (dir == null ? "" : (dir.getAbsolutePath() + "/")) + dateStr + ".mp4";
    }

    Timer timer;
    TimerTask task;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    refreshTimer();
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                try {
                    updatePeriod();
                    Message message = new Message();
                    message.what = 200;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void refreshTimer() {
        myVideoController.refreshTimer(periodTime);
    }

    private void resetUI() {
        periodTime = 0;
        refreshTimer();
    }

    private void updatePeriod() {
        if (mRecordDelegate.isRecording()) {
            periodTime += System.currentTimeMillis() - startTimer;
            startTimer = System.currentTimeMillis();
        }
    }
}
