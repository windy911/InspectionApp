package com.pm.cameracore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import static android.os.Looper.getMainLooper;
import static com.pm.cameracore.BuildConfig.DEBUG;

/**
 * @author pm
 * @date 2019/9/19
 * @email puming@zdsoft.cn
 */
public class RecordDelegate implements CameraDelegate {
    private static final String TAG = "RecordDelegate";
    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();
    public static final String CAMERA_ID_0 = "0";
    public static final String CAMERA_ID_1 = "1";

    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

    private Context mContext;
    private DelegateCallback mCallback;
    private CameraManager mCameraManager;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private Integer mSensorOrientation;
    private Size mVideoSize;
    private Size mPreviewSize;
    private WindowManager mWindowManager;
    public String mCameraId = CAMERA_ID_0;
    private String mNextVideoAbsolutePath;

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its status.
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            //当相机打开时回调。我们在这里开始相机预览
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            //step4 开始预览
            createCameraPreviewSession(cameraDevice);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            // TODO: 2019/9/19
        }
    };
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private CameraCaptureSession mPreviewSession;
    private MediaRecorder mMediaRecorder;
    public boolean mIsRecordingVideo;

    public boolean isRecording(){
        return mIsRecordingVideo;
    }

    public RecordDelegate(Context mContext, DelegateCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void openCamera(int width, int height) {
        //step1 选择相机预览的最佳尺寸
        setupCameraOutputs(width, height);
        //step2 配置相机预览的方向
        configureTransform(width, height);
//        try {
//            //信号量中尝试获取一个许可
//            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
//                throw new RuntimeException("Time out waiting to lock camera opening.");
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            Log.e(TAG, "openCamera: Interrupted while trying to lock camera opening.");
//        }
        //step3 打开相机
        try {
            Log.d("RAMBO","openCamera id = "+ mCameraId);
            mCameraManager.openCamera(mCameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "无法访问相机", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCameraOutputs(int width, int height) {
        if (null == mContext) {
            return;
        }
        //setup camera
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : Objects.requireNonNull(mCameraManager).getCameraIdList()) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                //录制视频只使用后置摄像头
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    Log.d("RAMBO", "cameraID front continue");
                    continue;
                }
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (null == map) {
                    throw new RuntimeException("Cannot get available preview/video sizes");
//                    continue;
                }
                //选择相机预览和录像的尺寸
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                Size[] outputSizes = map.getOutputSizes(MediaRecorder.class);
                mVideoSize = SizeUtils.chooseOutputSize(outputSizes, new Size(1280, 720), new Size(16, 9), false);
                 mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, mVideoSize);
                if (DEBUG) {
                    for (Size size : outputSizes) {
                        Log.d(TAG, "openCamera: size=" + size);
                    }
                    Log.d(TAG, "openCamera: mVideoSize=" + mVideoSize);
                }
                mWindowManager = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE));
                int rotation = mWindowManager.getDefaultDisplay().getRotation();
                int orientation = mContext.getResources().getConfiguration().orientation;
                if (DEBUG) {
                    //摄像头默认方向为90，手机屏幕方向为0
                    Log.d(TAG, "openCamera: rotation=" + rotation);
                    Log.d(TAG, "openCamera: orientation=" + orientation);
                }
                //根据屏幕方向设置TextureView的宽高
                changeTextureViewSize();
//                mCameraId = cameraId;
            }
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
//            ErrorDialog.newInstance(getString(R.string.camera_error))
//                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
            // TODO: 2019/9/19
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "无法访问相机", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the respective requested values, and whose aspect
     * ratio matches with the specified value.
     *
     * @param choices     The list of sizes that the camera supports for the intended output class
     * @param width       The minimum desired width
     * @param height      The minimum desired height
     * @param aspectRatio The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    private void changeTextureViewSize() {
        mCallback.onChangeViewSize(mPreviewSize);
    }

    /**
     * Configures the necessary {@link Matrix} transformation to `mTextureView`.
     * This method should not to be called until the camera preview size is determined in
     * openCamera, or until the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mPreviewSize || null == mContext) {
            return;
        }

        int rotation = mWindowManager.getDefaultDisplay().getRotation();

        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        mCallback.onTransformView(matrix);
    }


    /**
     * Start the camera preview.
     */
    private void createCameraPreviewSession(CameraDevice cameraDevice) {
        if (null == cameraDevice || null == mPreviewSize) {
            return;
        }
        closePreviewSession();
        SurfaceTexture texture = mCallback.getSurfaceTexture();
        assert texture != null;
        texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        try {
            //创建CaptureRequestBuilder，TEMPLATE_PREVIEW比表示预览请求
            mPreviewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface previewSurface = new Surface(texture);
            //设置Surface作为预览数据的显示界面
            mPreviewBuilder.addTarget(previewSurface);

            List<Surface> surfaces = Collections.singletonList(previewSurface);

            // 创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，
            // 第二个参数是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，
            // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行

            cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mPreviewSession = session;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    // TODO: 2019/9/19
                }
            }, mBackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the camera preview. {@link #createCameraPreviewSession(CameraDevice cameraDevice)} needs to be called in advance.
     */
    private void updatePreview() {
        if (null == mCameraDevice) {
            return;
        }
        setUpCaptureRequestBuilder(mPreviewBuilder);
        HandlerThread thread = new HandlerThread("CameraPreview");
        thread.start();
        try {
            //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    }

    public void startRecordingVideo() {
        if (null == mCameraDevice || null == mPreviewSize) {
            return;
        }

        closePreviewSession();
        setUpMediaRecorder();

        initImageReader();

        SurfaceTexture texture = mCallback.getSurfaceTexture();
        assert texture != null;
        texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        try {
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            ArrayList<Surface> surfaces = new ArrayList<>(2);

            // Set up Surface for the camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);
            // Set up Surface for the MediaRecorder
            Surface recordSurface = mMediaRecorder.getSurface();
            surfaces.add(recordSurface);

            surfaces.add(mImageReader.getSurface());

            mPreviewBuilder.addTarget(recordSurface);
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mPreviewSession = session;
                    updatePreview();
                    mIsRecordingVideo = true;
                    // Start recording
                    mMediaRecorder.start();
                    // TODO: 2019/9/19
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                    // TODO: 2019/9/19
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    /**
     * 停止录制
     *
     * @param isQuit true 完全退出 false 没有完全退出，回到预览状态
     */
    public void stopRecordingVideo(boolean isQuit) {
        if (!mIsRecordingVideo) {
            return;
        }
        mIsRecordingVideo = false;
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mCallback.onRecordResult(null, mNextVideoAbsolutePath);
        Log.d(TAG, "Video saved: " + mNextVideoAbsolutePath);
        if (isQuit) {
            return;
        }
        createCameraPreviewSession(mCameraDevice);
    }

    public void pauseRecordingVideo(){
        stopRecordingVideo(true);
    }


    public static String TopicName = "";
    public static String UserName = "";

    private String createVideoFilePath(Context context) {
//        final File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSSS", Locale.getDefault());
//        String dateStr = dateFormat.format(new Date());
//        return (dir == null ? "" : (dir.getAbsolutePath() + "/")) + dateStr + ".mp4";



        final File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSSS", Locale.getDefault());
        String dateStr = "";

        if(!TextUtils.isEmpty(TopicName)){
            dateStr+=TopicName;
        }
        if(!TextUtils.isEmpty(UserName)){
            dateStr+="_"+UserName;
        }
        dateStr +="_"+dateFormat.format(new Date());

        return (dir == null ? "" : (dir.getAbsolutePath() + "/")) + dateStr + ".mp4";
    }

    private void setUpMediaRecorder() {
        if (null == mContext) {
            return;
        }
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

//        if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty()) {
//        }

        mNextVideoAbsolutePath = createVideoFilePath(mContext);
        mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
        mMediaRecorder.setVideoEncodingBitRate(8000000);

        //每秒30帧
        mMediaRecorder.setVideoFrameRate(15);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        int rotation = 1;//mWindowManager.getDefaultDisplay().getRotation();
        switch (mSensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
            default:
                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
        }
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            /*if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }*/
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    @Override
    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    public void stopBackgroundThread() {
        if(mBackgroundThread!=null){
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void switchCameraID() {
        if (mCameraId.equals("0")) {
            mCameraId = "1";
        } else if (mCameraId.equals("1")) {
            mCameraId = "0";
        }
    }

    private ImageReader mImageReader;
    private Handler childHandler;
    private Handler mainHandler;
    private void initImageReader(){
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        mImageReader = ImageReader.newInstance(1280, 720, ImageFormat.JPEG,1);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { //可以在这里处理拍照得到的临时照片 例如，写入本地
            @Override
            public void onImageAvailable(ImageReader reader) {
//                mCameraDevice.close();
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);//由缓冲区存入字节数组
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    if(mCallback!=null){
                        mCallback.onCaptureResult(bitmap);
                    }
                }
                //這裏不快速image.close()會導致相機surface刷新卡死
                image.close();
            }
        }, mainHandler);
    }

    /**
     * 拍照
     */
    public void takePicture() {
        if (mCameraDevice == null) return;
        // 创建拍照需要的CaptureRequest.Builder
        final CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // 获取手机方向
//            int rotation = mWindowManager.getDefaultDisplay().getRotation();
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, 1);
            //拍照
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mPreviewSession.capture(mCaptureRequest, null, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}

