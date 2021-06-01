package com.pm.cameraui.widget;

import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pm.cameraui.R;
import com.pm.cameraui.bean.Topic;

import androidx.annotation.RequiresApi;

/*
用户头像+用户名
 */
public class TopicView extends RelativeLayout {

    private boolean isSelected = false;
    public TextView tvTopic;

    public TopicView(Context context) {
        super(context);
        initView(context,null);
    }

    public TopicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TopicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    public TopicView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.topic_view, this, true);
        tvTopic = rootView.findViewById(R.id.tvTopic);
        tvTopic.setSelected(false);
    }

    public void setContent(Topic topic){
        if(topic==null)return;
        tvTopic.setText(topic.getName());
    }

    public void setSelected(boolean selected){
        tvTopic.setSelected(selected);
        this.isSelected = selected;
        tvTopic.setBackground(getContext().getDrawable(selected?R.drawable.shape_round_rect_blue:R.drawable.shape_round_rect_white));
    }

    public boolean isSelected(){
        return isSelected;

    }
}
