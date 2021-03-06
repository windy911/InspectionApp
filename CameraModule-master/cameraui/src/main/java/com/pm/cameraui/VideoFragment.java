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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.obs.services.model.ProgressListener;
import com.obs.services.model.ProgressStatus;
import com.pm.cameracore.DelegateCallback;
import com.pm.cameracore.RecordDelegate;
import com.pm.cameraui.base.BaseFragment;
import com.pm.cameraui.base.MyGestureListener;
import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.Mark;
import com.pm.cameraui.bean.RecordSave;
import com.pm.cameraui.bean.RecordSaveList;
import com.pm.cameraui.bean.Topic;
import com.pm.cameraui.bean.UploadStatus;
import com.pm.cameraui.mvp.VideoPresenter;
import com.pm.cameraui.mvp.VideoView;
import com.pm.cameraui.utils.FileUtils;
import com.pm.cameraui.utils.LocationUtil;
import com.pm.cameraui.utils.MarkUtil;
import com.pm.cameraui.utils.RecordSaveUtils;
import com.pm.cameraui.utils.RecordUtil;
import com.pm.cameraui.utils.TimeUtil;
import com.pm.cameraui.utils.UploadUtil;
import com.pm.cameraui.widget.AutoFitTextureView;
import com.pm.cameraui.widget.CameraController;
import com.pm.cameraui.widget.MyVidoeController;
import com.pm.cameraui.widget.ShareDialog;
import com.pm.cameraui.widget.TopicSelectDialog;

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

public class VideoFragment extends BaseFragment<VideoPresenter> implements DelegateCallback, VideoView {

    private AutoFitTextureView mTextureView;
    private CameraController mController;
    private RecordDelegate mRecordDelegate;
    private MyVidoeController myVideoController;
    private boolean isLastVideoPath = false;
    private long startTimer = System.currentTimeMillis();
    private long periodTime = 0;
    private long savePeriodTime = 0;
    private ArrayList<Mark> markList = new ArrayList<>();

    private List<String> recordFileList = new ArrayList<>();
    private RelativeLayout rlLoading;
    private TextView tvProgress;
    private String strProgress;
    private boolean isFinishedTask = false;
    public static boolean isUploading = false;


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
    protected VideoPresenter createPresenter() {
        return new VideoPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void addListener() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.getAppConfiguration();

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
        myVideoController = view.findViewById(R.id.myVideoController);
        myVideoController.setControllerCallback(new CameraController.ControllerCallback() {
            @Override
            public void takePicture() {
//                markRecord(true);
            }

            @Override
            public void recordStart() {
                if (Constants.CURRENT_TOPIC != null) {
                    startNewInspectRecord();
                } else {
                    presenter.getInspectionTopic();
                }
            }

            @Override
            public void recordStop() {
                isLastVideoPath = true;
                //??????????????????????????????
                stopRecording();
            }

            @Override
            public void recordPause() {
                //??????????????????????????????
                try{
                    pauseRecording();
                }catch (Exception e){

                }
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
                if (mRecordDelegate.mIsRecordingVideo) {//????????????????????????????????? ??????+??????+???????????????
                    //Toast.makeText(getActivity(),"?????????????????????????????????",Toast.LENGTH_SHORT).show();
                    pauseRecording();
                    startTaskForSwitchCamera();
                } else {
                    //?????????????????????????????????????????????
                    switchCameraID();
                }
            }

            @Override
            public void onExitApp() {
                CameraActivity.instance.finish();
            }
        });
        mController.setVisibility(View.GONE);
        rlLoading = view.findViewById(R.id.rlLoading);
        tvProgress = view.findViewById(R.id.tvProgress);
        rlLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????????????????????
            }
        });
        showLoading(false);

        startBackgroundUpload();



        initGestureDetetor();

    }

    public void initGestureDetetor(){
//        detector = new GestureDetector(this, new MyGestureListener(
//                new MyRightLeftListener() {
//                    @Override
//                    public void onRight() {
//                        slideFocusChange(MyGestureListener.DIR_RIGHT);
//                    }
//
//                    @Override
//                    public void onLeft() {
//                        slideFocusChange(MyGestureListener.DIR_LEFT);
//                    }
//
//                    @Override
//                    public void onUp() {
//                        // TODO Auto-generated method stub
//                        slideFocusChange(MyGestureListener.DIR_UP);
//                    }
//                    @Override
//                    public void onDown() {
//                        // TODO Auto-generated method stub
//                        slideFocusChange(MyGestureListener.DIR_DOWN);
//                    }
//
//                    @Override
//                    public void onSlide() {
//
//                    }
//
//                    @Override
//                    public void onSingleTapUp() {
//                      clicked();
//                    }
//                }));
    }

    //????????????????????????
    public void startBackgroundUpload() {
        uiHandler.sendEmptyMessageDelayed(HANDLER_UPLOAD_BACKGROUD, 5000);
    }

    public void startTaskForSwitchCamera() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //???????????????

                    //??????????????????5???????????????onCLick
                    CameraActivity.instance.lastClickedTime = System.currentTimeMillis()+5000;

                    Message msg = new Message();
                    msg.what = HANDLER_SWITCH_CAMERA;
                    uiHandler.sendMessageDelayed(msg, 1000);
                    Thread.sleep(2000);
                    Message msg2 = new Message();
                    msg2.what = HANDLER_CONTINUE_RECORD;
                    uiHandler.sendMessage(msg2);
                } catch (Exception e) {

                }
            }
        }).start();
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
        LocationUtil.setEnableLocation(false);
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
        isFinishedTask = false;

    }

    public void stopRecording() {

        myVideoController.continueRecording();
        myVideoController.refreshUIRecord(false);

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
            Log.d("RAMBO", "????????????????????? onCaptureResult: bitmap count = " + bitmap.getByteCount());
            String filePath = createImageFilePath(getActivity());
            String saveFile = FileUtils.saveBitmapToFile(filePath, bitmap);
            if (!TextUtils.isEmpty(saveFile)) {
                UploadUtil.uploadImage(filePath, new UploadUtil.OnUploadsListener() {
                    @Override
                    public void onUploadSuccess(String localPath, String remoteUrl) {
                        if (isForVoiceMark) {
                            audioMarkImagePath = localPath;
                            audioMarkImageURL = remoteUrl;
                            Log.d("RAMBO", "???????????????MARK?????????localPath=" + localPath + " remoteUrl=" + remoteUrl);
                        } else {
                            updateRecord(localPath, remoteUrl);
                        }
                    }
                });
            }
        }
//        myVideoController.setPreViewImage(bitmap);
    }

    @Override
    public void onRecordResult(Bitmap coverBitmap, String videoAbsolutePath) {
        Log.d("RAMBO", "Video ????????????:" + videoAbsolutePath);

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

    //?????????????????????
    public void clearSaveList() {
        if (recordFileList != null) {
            recordFileList.clear();
        }
    }


    //??????????????????????????????????????????
    public void mergeSaveList() {
        Log.d("RAMBO", "?????????????????????????????????MergeSaveList");
        resetUI();
        String mergeVideoPath = createVideoFilePath(getActivity());
        showLoading(true);

        UploadUtil.mergeVideos(getActivity(), mergeVideoPath, recordFileList, new UploadUtil.OnMergeSuccessListener() {
            @Override
            public void onMergeSuccess(String outFile) {
                if(!TextUtils.isEmpty(outFile)){
                    Log.d("RAMBO", "????????????????????????" + outFile);
                    clearSaveList();
                    stopInspectionTopic(outFile);
                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLoading(false);
                            CameraActivity.instance.hideNavigation();
                            presenter.getInspectionTopic();
                            Toast.makeText(getContext(),"??????????????????",Toast.LENGTH_SHORT).show();
                        }
                    });
//
//                    UploadUtil.mergeVideosSlow(getActivity(), mergeVideoPath, recordFileList, new UploadUtil.OnMergeSuccessListener(){
//                        @Override
//                        public void onMergeSuccess(String outFile) {
//                            if(!TextUtils.isEmpty(outFile)){
//                                Log.d("RAMBO", "????????????????????????" + outFile);
//                                clearSaveList();
//                                stopInspectionTopic(outFile);
//                            }else {
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        showLoading(false);
//                                        CameraActivity.instance.hideNavigation();
//                                        presenter.getInspectionTopic();
//                                        Toast.makeText(getContext(),"??????????????????",Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }
//                    });
                }
            }
        });


    }

    private String createVideoFilePath(Context context) {
        final File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSSS", Locale.getDefault());
        String dateStr = "";

        if(Constants.CURRENT_TOPIC != null){
            dateStr+=Constants.CURRENT_TOPIC.getName();
        }
        if(Constants.userInfo!=null){
            dateStr+="_"+Constants.userInfo.getUserName();
        }
        dateStr +="_"+dateFormat.format(new Date());

        return (dir == null ? "" : (dir.getAbsolutePath() + "/")) + dateStr + ".mp4";
    }

    private String createImageFilePath(Context context) {
        final File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSSS", Locale.getDefault());
        String dateStr = dateFormat.format(new Date());
        return (dir == null ? "" : (dir.getAbsolutePath() + "/")) + dateStr + ".jpg";
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
        try {
            if (timer != null) {
                timer.cancel();
            }
            mRecordDelegate.stopBackgroundThread();
            mRecordDelegate.closeCamera();
        } catch (Exception e) {

        }
    }

    public void refreshTimer() {
        myVideoController.refreshTimer(periodTime);
    }

    private void resetUI() {
        savePeriodTime = periodTime;
        periodTime = 0;
        refreshTimer();

    }

    private void updatePeriod() {
        LocationUtil.setEnableLocation(mRecordDelegate.isRecording());
        if (mRecordDelegate.isRecording()) {
            periodTime += System.currentTimeMillis() - startTimer;
            startTimer = System.currentTimeMillis();
        }
    }

    @Override
    public void showJsonText(String text) {
        Log.d("RAMBO", "ShowJsonText:" + text);
    }

    InspectRecord inspectRecord;

    //????????????????????????
    public void startNewInspectRecord() {
        inspectRecord = new InspectRecord();
        inspectRecord.setStartTimeLong(System.currentTimeMillis());
        inspectRecord.setStartTime(TimeUtil.getFormatDateTime(inspectRecord.getStartTimeLong()));
        if (Constants.CURRENT_TOPIC != null) {
            inspectRecord.setTopicId(Constants.CURRENT_TOPIC.getId());
        }
        presenter.newInspectRecord(inspectRecord);
    }


    //??????????????????????????????
    @Override
    public void newInspectionTopic(InspectRecord record) {
        RecordDelegate.TopicName = Constants.CURRENT_TOPIC.getName();
        RecordDelegate.UserName = Constants.userInfo.getUserName();


        isLastVideoPath = false;
        clearSaveList();
        startRecording();
        inspectRecord = record;
        markList = new ArrayList<>();
        CameraActivity.instance.location();
        myVideoController.hideControler();

    }

    @Override
    public void updateInspection(InspectRecord record) {
        MarkUtil.generateAudioForMarks(record, markList, presenter, false);
    }

    @Override
    public void updateInspection2(RecordSave recordSave) {
        MarkUtil.generateAudioForMarks(recordSave.inspectRecord, recordSave.markList, presenter, true);

        RecordSaveUtils.removeRecordSave(getActivity(), recordSave);
        if (RecordSaveUtils.haveLocalSaves(getActivity())&& (!isUploading())) {
            startBackgroundUpload();
        }else{


        }
    }

    //???????????????????????????????????????????????????????????????????????????
    public boolean isUploading(){
        return isUploading;
    }

    //????????????InspcetionTopic???????????????????????????????????????????????????????????????????????????????????????????????????????????????
    public void stopInspectionTopic(String videoFilePath) {
        if(TextUtils.isEmpty(videoFilePath)){return;}
        //???savePeriodTime??????????????????????????????????????????
        inspectRecord = RecordUtil.endRecord(inspectRecord, savePeriodTime);
        new ShareDialog(inspectRecord)
                .setTitle(inspectRecord.getName())
                .setConfirm("?????????????????????")
                .setCancel("????????????")
                .setDialogCancelable(false)
                .setOnCertainButtonClickListener(new ShareDialog.OnCertainButtonClickListener() {
                    @Override
                    public void onCertainButtonClick() {
//                        showLoading(true);
//                        uploadVideoFileToHuaWei(videoFilePath);

                        //1.??????????????????
                        RecordSave recordSave = new RecordSave(videoFilePath, inspectRecord, LocationUtil.getLocationString(), markList);
                        RecordSaveUtils.saveRecordSaves(getActivity(), recordSave);
                        CameraActivity.instance.hideNavigation();
                        //2.???????????????
                        loadUploadSave();

                        Message message = new Message();
                        message.what = HANDLER_SHOW_BG_ACTION;
                        uiHandler.sendMessage(message);

                        presenter.getInspectionTopic();
                    }
                }).show(getFragmentManager(), "CommonDialog");

        Message message = new Message();
        message.what = HANDLER_HIDE_BG_ACTION;
        uiHandler.sendMessage(message);
    }

//???????????????????????????

//    public void uploadVideoFileToHuaWei(String videoFilePath) {
//        UploadUtil.upload(videoFilePath, new ProgressListener() {
//            @Override
//            public void progressChanged(ProgressStatus progressStatus) {
//                Log.d("RAMBO progressChanged ", "percent = " + progressStatus.getTransferPercentage());
//                strProgress = "" + progressStatus.getTransferPercentage() + "%";
//                Message message = new Message();
//                message.what = HANDLER_UPDATE_PROGRESS;
//                uiHandler.sendMessage(message);
//            }
//        }, new UploadUtil.OnUploadsListener() {
//            @Override
//            public void onUploadSuccess(String localPath, String remoteUrl) {
//                inspectRecord.setLocalVideoFilePath(localPath);
//                inspectRecord.setVideoUrl(remoteUrl);
//                inspectRecord.setUploadStatus(UploadStatus.UPLOAD_SUCCESS);
//                inspectRecord.setTraceLocus(LocationUtil.getLocationString());
//                LocationUtil.clearAll();
//                presenter.updateInspectRecord(inspectRecord);
//            }
//        });
//    }

    //????????????????????????????????????
    public void markRecord(boolean isShowMark) {
        if(isShowMark){
            myVideoController.showImageMarking();
            Message msg = new Message();
            msg.what = HANDLER_HIDE_IMAGE_MARKDING;
            uiHandler.sendMessageDelayed(msg, 1000);
        }
        mRecordDelegate.takePicture();
    }

    public void updateRecord(String localPath, String imgUrl) {
        Mark mark = MarkUtil.singleImageMark(inspectRecord, localPath, imgUrl, periodTime,CameraActivity.longitude,CameraActivity.latitude);
        if (mark != null) {
            presenter.addMarkRecord(mark);
        }
    }

    @Override
    public void addMarkRecord(Mark mark) {
        myVideoController.showMarkSuccess();
        Message msg = new Message();
        msg.what = HANDLER_HIDE_MARK_SUCCESS;
        uiHandler.sendMessageDelayed(msg, 1000);
        //??????????????????????????????Mark??? +1
        inspectRecord.setLabels(inspectRecord.getLabels() + 1);
        markList.add(mark);
    }


    //???????????? ???????????? 500ms ???,?????? Mark
    public void onMarkShortClicked() {
        if(!isStartVoice){
            Log.d("RAMBO", "onMarkShortClicked : Voice?????????????????????????????????");
            markRecord(true);
            isForVoiceMark = false;
            isStartVoice = false;
            isStartKeyDown = false;
        }
        myVideoController.hideVoiceMarker();
    }

    //????????????
    public void onMarkLongClickStart() {
        isStartVoice = true;
//        ToastUtils.show("????????????");
        Log.d("RAMBO", "onMarkLongClickStart :Voice????????????");
        audioStartTime = periodTime / 1000;
        audioStartTimeLong = System.currentTimeMillis();
        markRecord(false);
        isForVoiceMark = true;
        audioDownTime = System.currentTimeMillis();
    }

    //????????????
    public void onMarkLongClickEnd(long voiceDuration) {
        isStartVoice = false;
        myVideoController.hideVoiceMarker();
        endVoiceMark();
    }


    boolean isForVoiceMark = false;
    long audioDownTime, audioUpTime;
    boolean isStartVoice = false;
    boolean isStartKeyDown = false;

    public void onVoiceKeyDown() {
        if (!isEnableVoiceKey) return;

        if (!mRecordDelegate.isRecording()) {
            return;
        }
        if (!isStartKeyDown) {
            isStartKeyDown = true;
            audioDownTime = System.currentTimeMillis();
        } else if (isStartKeyDown) {
            //????????????????????????
            if ((!isStartVoice) && (System.currentTimeMillis() - audioDownTime) > 500) {
                //??????1000ms???????????????????????????
                onMarkLongClickStart();
                //??????????????????????????????ReleaseKey???????????????????????????isReleaseVoiceKey=true,?????????????????????
            } else if (isStartVoice) {
                updateVoiceProgress(System.currentTimeMillis() - audioDownTime);
            }
        }
    }

    public void onVoiceKeyUp() {
        //???????????????????????????????????????

        if (!mRecordDelegate.isRecording()) {
            return;
        }
        audioUpTime = System.currentTimeMillis();
        if (isStartKeyDown) {
            if ((audioUpTime - audioDownTime) < 500) {
                //500ms????????????????????????????????????
                onMarkShortClicked();
            } else if (audioUpTime - audioDownTime > 1000) {
                onMarkLongClickEnd(audioUpTime - audioDownTime);
            } else{
                isStartVoice = false;
                myVideoController.hideVoiceMarker();
            }
        }
        isEnableVoiceKey = true;
    }

    public static final long MAX_VOICE = 20 * 1000;//20?????????

    //??????voice?????????????????????
    public void updateVoiceProgress(long timer) {
        int progress = 0;
        if (timer > MAX_VOICE) {
            progress = 100;
        } else {
            progress = (int) ((float) (timer * 100.0) / (float) MAX_VOICE);
        }
        myVideoController.showVoiceMarker(progress);
        if (progress == 100) {
            onMarkLongClickEnd(MAX_VOICE);
        }
    }

    //* 1?????????????????? ????????????
    //* 2???????????????20??????????????????
    long audioStartTime;
    long audioStartTimeLong;
    long audioEndTime;
    long audioEndTimeLong;
    String audioMarkImagePath;
    String audioMarkImageURL;
    boolean isEnableVoiceKey = true;

    //?????????ArrayList??????Mark??????????????????????????????????????????????????????Mark???
    //?????????????????????????????????20???Progress???????????????????????????????????????????????????????????????????????????mark
    public void endVoiceMark() {
        audioEndTime = periodTime / 1000;
        audioEndTimeLong = System.currentTimeMillis();
        Mark mark = MarkUtil.withAudioMark(inspectRecord, audioMarkImagePath, audioMarkImageURL,
                audioStartTime, audioEndTime, audioStartTimeLong, audioEndTimeLong,CameraActivity.longitude,CameraActivity.latitude);
        if (mark != null) {
            presenter.addMarkRecord(mark);
        }
        isStartKeyDown = false;
        isStartVoice = false;
        isEnableVoiceKey = false;
    }

    @Override
    public void showTopicList(List<Topic> topics) {
        TopicSelectDialog dialog = new TopicSelectDialog(topics, new TopicSelectDialog.OnDialogAction() {
            @Override
            public void onDismiss() {
                myVideoController.startInit();
                showLoading(false);
                LocationUtil.clearAll();
            }
        });
        dialog.show(getFragmentManager(), "TopicDialog");

    }

    @Override
    public void onApplicationResult() {
        presenter.getInspectionTopic();
    }

    public void showLoading(boolean isShow) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rlLoading.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onTaskFinish() {
        //??????????????????????????????
        if (!isFinishedTask) {
            isFinishedTask = true;
            Message message = new Message();
            message.what = HANDLER_FINISHED_TASK;
            uiHandler.sendMessage(message);
        }
    }


    public static final int HANDLER_FINISHED_TASK = 0;
    public static final int HANDLER_UPDATE_PROGRESS = 1;
    public static final int HANDLER_SWITCH_CAMERA = 2;
    public static final int HANDLER_CONTINUE_RECORD = 3;
    public static final int HANDLER_UPLOAD_BACKGROUD = 4;
    public static final int HANDLER_HIDE_BG_ACTION = 5;
    public static final int HANDLER_SHOW_BG_ACTION = 6;
    public static final int HANDLER_HIDE_MARK_SUCCESS = 7;
    public static final int HANDLER_HIDE_IMAGE_MARKDING = 8;


    private Handler uiHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_FINISHED_TASK) {
                showLoading(false);
                CameraActivity.instance.hideNavigation();
                presenter.getInspectionTopic();
            } else if (msg.what == HANDLER_UPDATE_PROGRESS) {
                tvProgress.setText(strProgress);
            } else if (msg.what == HANDLER_SWITCH_CAMERA) {
                switchCameraID();
            } else if (msg.what == HANDLER_CONTINUE_RECORD) {
                continueRecording();
            } else if (msg.what == HANDLER_UPLOAD_BACKGROUD) {
                loadUploadSave();
            }else if (msg.what == HANDLER_HIDE_BG_ACTION) {
                myVideoController.showBackgroundAction(false);
                showLoading(false);
            }else if (msg.what == HANDLER_SHOW_BG_ACTION) {
                myVideoController.showBackgroundAction(true);
            }else if(msg.what == HANDLER_HIDE_MARK_SUCCESS){
                myVideoController.hideMarkSuccess();
            }else if(msg.what == HANDLER_HIDE_IMAGE_MARKDING){
                myVideoController.hideImageMarkding();
            }
        }
    };


    //?????????????????????????????????????????????
    public void loadUploadSave() {
        RecordSaveList recordSaveList = RecordSaveUtils.loadAllRecordSave(getActivity());
        int size = recordSaveList.recordSaves.size();
        Log.d("RAMBO", "???????????????????????????" + size + "??????");
        if (size > 0) {
            RecordSave recordSave = recordSaveList.recordSaves.get(recordSaveList.recordSaves.size() - 1);
            uploadSave(recordSave);
        }else{
            //????????????????????????????????????????????????????????????????????????
//            DeviceUtil.clearMediaFiles(getContext());
        }
    }

    public void uploadSave(RecordSave recordSave) {
//        if(TextUtils.isEmpty(recordSave.videoFilePath)){
//            RecordSaveUtils.removeRecordSave(getActivity(), recordSave);
//            return;
//        }
        uploadVideoFileToHuaWei_2(recordSave);
    }


    public void uploadVideoFileToHuaWei_2(RecordSave recordSave) {
        UploadUtil.upload(recordSave.videoFilePath, new ProgressListener() {
            @Override
            public void progressChanged(ProgressStatus progressStatus) {
                isUploading = true;
                Log.d("RAMBO", "uploadVideoFileToHuaWei_2 percent = " + progressStatus.getTransferPercentage());
            }
        }, new UploadUtil.OnUploadsListener() {
            @Override
            public void onUploadSuccess(String localPath, String remoteUrl) {
                recordSave.inspectRecord.setLocalVideoFilePath(localPath);
                recordSave.inspectRecord.setVideoUrl(remoteUrl);
                recordSave.inspectRecord.setUploadStatus(UploadStatus.UPLOAD_SUCCESS);
                recordSave.inspectRecord.setTraceLocus(LocationUtil.getLocationString());
                presenter.updateInspectRecord2(recordSave);
                isUploading = false;
            }
        });
    }

    public void slideFocusChange(int DIR) {
        myVideoController.slideFocusChange(DIR);
    }

    public void clicked(){
        myVideoController.onClicked();
    }

    public void showOrHideControl(boolean isShow){
        Log.d("RAMBO",isShow?"showControler":"hideControler");
    }


    public void slideDown(){myVideoController.slideFocusChange(MyGestureListener.DIR_DOWN);}
    public void slideUp(){myVideoController.slideFocusChange(MyGestureListener.DIR_UP);}
    public void slideLeft(){myVideoController.slideFocusChange(MyGestureListener.DIR_LEFT);}
    public void slideRight(){myVideoController.slideFocusChange(MyGestureListener.DIR_RIGHT);}


}
