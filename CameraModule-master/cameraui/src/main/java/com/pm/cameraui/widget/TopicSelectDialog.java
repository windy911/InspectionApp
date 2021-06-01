package com.pm.cameraui.widget;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pm.cameraui.CameraActivity;
import com.pm.cameraui.Constants;
import com.pm.cameraui.R;
import com.pm.cameraui.bean.Topic;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TopicSelectDialog extends DialogFragment implements View.OnClickListener {

    private TextView confirmButton;
    private Topic selectTopic;
    private List<Topic> topicList;
    private LinearLayout llContent;
    private ArrayList<TopicView> topicViews;
    private View arrowLeft, arrowRight;
    HorizontalScrollView hscrollView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public TopicSelectDialog(List<Topic> topics) {
        setStyle(STYLE_NO_TITLE, R.style.topic_dialog_style);
        this.topicList = topics;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.topic_select_dialog, container, false);
        confirmButton = mView.findViewById(R.id.topic_tag_confirm);
        confirmButton.setOnClickListener(this);
        llContent = mView.findViewById(R.id.llContent);
        topicViews = new ArrayList<>();
        arrowLeft = mView.findViewById(R.id.arrowLeft);
        arrowRight = mView.findViewById(R.id.arrowRight);
        hscrollView = mView.findViewById(R.id.hscrollView);
        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveContent(true);
            }
        });

        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveContent(false);
            }
        });

        for (int i = 0; i < topicList.size(); i++) {
            TopicView topicView = new TopicView(getActivity());
            topicView.setContent(topicList.get(i));
            //设置index为Tag
            topicView.setTag(i);
            topicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    selectTopic = topicList.get(index);
                    Log.d("RAMBO", "选中了 " + selectTopic.getName());
                    selectedIndex(index);
                }
            });
            llContent.addView(topicView);
            topicViews.add(topicView);
        }

        if (topicList != null && topicList.size() > 0) {
            selectedIndex(0);
        }
        return mView;
    }

    public void moveContent(boolean isLeft) {
        hscrollView.smoothScrollBy(isLeft ? -480 : 480, 0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.topic_tag_confirm) {
            if (selectTopic == null) {
                Toast.makeText(getActivity(), getString(R.string.select_topic), Toast.LENGTH_SHORT).show();
                return;
            }
            Constants.CURRENT_TOPIC = selectTopic;
            dismiss();
        }
    }

    public void selectedIndex(int index) {
        selectTopic = topicList.get(index);
        for (int i = 0; i < topicViews.size(); i++) {
            topicViews.get(i).setSelected(index == i);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (CameraActivity.instance != null) {
            CameraActivity.instance.hideNavigation();
        }
    }
}
