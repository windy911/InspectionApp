package com.pm.cameraui.widget;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
            recordMarks.setText(mRecord.getLabels()+"个");
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
}
