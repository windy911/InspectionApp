package com.pm.cameraui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.pm.cameraui.Constants;
import com.pm.cameraui.R;
import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.utils.TimeUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

@SuppressLint("ValidFragment")
public class ShareDialog extends DialogFragment {

    private int layout = -1;
    private TextView confirmTv, cancelTv, titleTV,recordTime,recordDuration,recordMarks;
    private String confirm, cancel, content,mTitle;
    private View dividerLine;
    private boolean showCancelBButton = true;
    private InspectRecord mRecord;

    public ShareDialog(int layout) {
        setStyle(STYLE_NO_FRAME, R.style.common_dialog_style);
        this.layout = layout;
    }

    public ShareDialog(InspectRecord record) {
        mRecord = record;
        setStyle(STYLE_NO_FRAME, R.style.common_dialog_style);
    }
    public ShareDialog(int layout, int style) {
        setStyle(STYLE_NO_FRAME, style);
        this.layout = layout;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(layout == -1 ? R.layout.view_dialog_share : layout, container, false);
        confirmTv = mView.findViewById(R.id.item_inspect_dialog_confirm);
//        cancelTv = mView.findViewById(R.id.item_inspect_dialog_cancel);
        recordTime = mView.findViewById(R.id.item_inspect_share_time);
        recordDuration = mView.findViewById(R.id.item_inspect_share_duration);
        recordMarks = mView.findViewById(R.id.item_inspect_share_mark);
        titleTV = mView.findViewById(R.id.item_inspect_dialog_title);
//        dividerLine = mView.findViewById(R.id.comm_dialog_divider_line);
//        if (this.cancel != null){
//            cancelTv.setText(cancel);
//        }
        if (this.confirm != null){
            confirmTv.setText(confirm);
        }
        if (this.titleTV != null){
            titleTV.setText(mTitle);
        }

        if (mRecord != null){
            recordTime.setText(TimeUtil.getFormatDate(mRecord.getStartTimeLong()));
            recordDuration.setText(TimeUtil.formatTime(mRecord.getEndTimeLong() - mRecord.getStartTimeLong()));
            recordMarks.setText(mRecord.getLabels()+"???");
        }

        if(confirmTv != null) {
            confirmTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(mOnCertainButtonClickListener != null){
                        mOnCertainButtonClickListener.onCertainButtonClick();
                    }
                }
            });
        }
        if(cancelTv != null) {
            if (!showCancelBButton){
                this.cancelTv.setVisibility(View.GONE);

                if (this.dividerLine != null){
                    this.dividerLine.setVisibility(View.GONE);
                }
            }
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mOnCancelClickListener != null){
                        mOnCancelClickListener.onCancelClick();
                    }
                }
            });
        }
        getDialog().getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onTouchEvent(motionEvent);
                return true;
            }
        });
        return mView;
    }

    public interface OnCertainButtonClickListener {
        void onCertainButtonClick();
    }
    public interface onCancelClickListener{
        void onCancelClick();
    }
    private OnCertainButtonClickListener mOnCertainButtonClickListener;
    private onCancelClickListener mOnCancelClickListener;

    public ShareDialog setOnCancelClickListener(onCancelClickListener listener){
        mOnCancelClickListener = listener;
        return this;
    }
    public  ShareDialog setOnCertainButtonClickListener(OnCertainButtonClickListener listener) {
        mOnCertainButtonClickListener = listener;
        return  this;
    }

    public  ShareDialog setConfirm(String confirm) {
        this.confirm = confirm;
        return this;
    }

    public  ShareDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public  ShareDialog setCancelBtnShow(Boolean cancelBtnShow) {
        showCancelBButton = cancelBtnShow;
        return this;
    }

    public  ShareDialog setCancel(String cancel) {
        this.cancel = cancel;
        return this;
    }

    public ShareDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public  ShareDialog setDialogCancelable(boolean isCancel){
        this.setCancelable(isCancel);
        return this;
    }

    /**
     * ????????????
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        String action = "";
        //?????????????????????????????????
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //???????????????????????????
                downX = x;
                downY = y;
                Log.e("Tag", "=======?????????X???" + x);
                Log.e("Tag", "=======?????????Y???" + y);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("Tag", "=======?????????X???" + x);
                Log.e("Tag", "=======?????????Y???" + y);

                //??????????????????
                float dx = x - downX;
                float dy = y - downY;
                //????????????????????????
                if (Math.abs(dx) > 2 && Math.abs(dy) > 2) {
                    //???????????????????????????
                    int orientation = getOrientation(dx, dy);
                    switch (orientation) {
                        case 'r':
                            action = "???";
                            slideRight();
                            break;
                        case 'l':
                            action = "???";
                            slideLeft();
                            break;
                        case 't':
                            action = "???";
                            slideUp();
                            break;
                        case 'b':
                            action = "???";
                            slideDown();
                            break;
                    }
                } else {
                    clicked();
                }
                break;
        }

        return false;
    }

    public void slideRight() {
//        slideFocusChange(TouchHandlerListener.DIR_RIGHT);
    }

    public void slideDown() {
//        slideFocusChange(TouchHandlerListener.DIR_DOWN);
    }

    public void slideUp() {
//        slideFocusChange(TouchHandlerListener.DIR_UP);
    }

    public void slideLeft() {
//        slideFocusChange(TouchHandlerListener.DIR_LEFT);
    }

    private float downX;    //????????? ???X??????
    private float downY;    //????????? ???Y??????

    /**
     * ????????????????????? ????????????
     *
     * @param dx X???????????????
     * @param dy Y???????????????
     * @return ???????????????
     */
    private int getOrientation(float dx, float dy) {
        Log.e("Tag", "========X???????????????" + dx);
        Log.e("Tag", "========Y???????????????" + dy);
        if (Math.abs(dx) > Math.abs(dy)) {
            //X?????????
            return dx > 0 ? 'r' : 'l';
        } else {
            //Y?????????
            return dy > 0 ? 'b' : 't';
        }
    }

    public void clicked(){
        if(confirmTv!=null){
            doClickAnim(confirmTv);
            confirmTv.performClick();
        }
    }

    public void doClickAnim(View view) {
        if(!Constants.isAniClick)return;
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation setAnim = AnimationUtils.loadAnimation(getContext(), R.anim.view_click);
                view.startAnimation(setAnim);
            }
        });
    }
}
