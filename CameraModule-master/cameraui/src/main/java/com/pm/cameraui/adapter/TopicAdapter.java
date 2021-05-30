package com.pm.cameraui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.pm.cameraui.R;
import com.pm.cameraui.bean.Topic;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private List<Topic> mTopicList;
    private Context mContext;
    private int selectIndex = 0;

    public TopicAdapter(Context mContext, List<Topic> mTopicList) {
        this.mTopicList = mTopicList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_topic_select_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        try {
            Topic topic = mTopicList.get(position);
            holder.topicText.setText(topic.getName());

            if (selectIndex == position){
                holder.topicText.setTextColor(mContext.getResources().getColor(R.color.color_blue));
                holder.topicText.setBackgroundResource(R.drawable.bg_round_4_stroke_1_bule);
            }else {
                holder.topicText.setTextColor(mContext.getResources().getColor(R.color.black_333333));
                holder.topicText.setBackgroundResource(R.drawable.bg_round_4_ccd3f1);
            }

            holder.topicText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectIndex = position;
                    if (onTopicItemClickListener != null) {
                        onTopicItemClickListener.onTopicClick(mTopicList.get(position));
                    }
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mTopicList == null ? 0 : mTopicList.size();
    }

    class TopicViewHolder extends RecyclerView.ViewHolder{

        TextView topicText;
        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicText = itemView.findViewById(R.id.item_topic_select_text);
        }

    }

    public void setOnTopicItemClickListener(TopicAdapter.onTopicItemClickListener onTopicItemClickListener) {
        this.onTopicItemClickListener = onTopicItemClickListener;
    }

    private onTopicItemClickListener onTopicItemClickListener;
    public interface onTopicItemClickListener{
        void onTopicClick(Topic topic);
    }
}
