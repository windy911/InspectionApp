package com.pm.cameraui.widget;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.pm.cameraui.CameraActivity;
import com.pm.cameraui.Constants;
import com.pm.cameraui.R;
import com.pm.cameraui.adapter.TopicAdapter;
import com.pm.cameraui.bean.Topic;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TopicSelectDialog extends DialogFragment implements View.OnClickListener, TopicAdapter.onTopicItemClickListener {
    private TagFlowLayout tagFlowLayout;
    private RecyclerView tagRecycleView;
    private TextView confirmButton;
    private Topic selectTopic;
    private List<Topic> topicList;
    private TopicAdapter mTopicAdapter;

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

        tagRecycleView = mView.findViewById(R.id.topic_tag_recycle_view);
        confirmButton = mView.findViewById(R.id.topic_tag_confirm);

        tagRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTopicAdapter = new TopicAdapter(getActivity(),topicList);
        tagRecycleView.setAdapter(mTopicAdapter);
        confirmButton.setOnClickListener(this);
        mTopicAdapter.setOnTopicItemClickListener(this);
        if (topicList != null && topicList.size() > 0){
            selectTopic = topicList.get(0);
        }
        return mView;
    }

    @Override
    public void onClick(View view) {
        if (selectTopic == null){
            Toast.makeText(getActivity(), getString(R.string.select_topic), Toast.LENGTH_SHORT).show();
            return;
        }
        Constants.CURRENT_TOPIC = selectTopic;
        dismiss();
    }

    @Override
    public void onTopicClick(Topic topic) {
        selectTopic = topic;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        if(CameraActivity.instance!=null){
            CameraActivity.instance.hideNavigation();
        }
    }
}
