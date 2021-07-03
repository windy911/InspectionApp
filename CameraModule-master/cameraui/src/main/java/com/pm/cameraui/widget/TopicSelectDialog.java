package com.pm.cameraui.widget;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pm.cameraui.CameraActivity;
import com.pm.cameraui.Constants;
import com.pm.cameraui.R;
import com.pm.cameraui.base.MyGestureListener;
import com.pm.cameraui.base.MyRightLeftListener;
import com.pm.cameraui.bean.Topic;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TopicSelectDialog extends DialogFragment implements View.OnClickListener {

    private TextView confirmButton, tvHide, tvExit;
    private MyButton btnExit;
    private Topic selectTopic;
    private List<Topic> topicList;
    private LinearLayout llContent;
    private ArrayList<TopicView> topicViews;
    private View arrowLeft, arrowRight;
    private MyHorizontalScrollView hscrollView;
    RelativeLayout rlTopicContent;
    private int currentIndex = 0;
    private OnDialogAction onDialogAction;
    GestureDetector detector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public TopicSelectDialog(List<Topic> topics, OnDialogAction cb) {
        setStyle(STYLE_NO_TITLE, R.style.topic_dialog_style);
        this.topicList = topics;
        this.onDialogAction = cb;
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
        btnExit = mView.findViewById(R.id.btnExit);
        tvExit = mView.findViewById(R.id.tvExit);
        tvHide = mView.findViewById(R.id.tvHide);
        hscrollView.setOnTouchListener(null);

        hscrollView.setFocusableInTouchMode(false);
        hscrollView.setTouchscreenBlocksFocus(false);
        hscrollView.fling(0);
        rlTopicContent = mView.findViewById(R.id.rlTopicContent);
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
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraActivity.instance.finish();
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
            clearIndexSign();
        }


        confirmButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                confirmButton.setAlpha(b?1.0f:0.3f);
            }
        });
        confirmButton.requestFocus();


        btnExit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                tvExit.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });


        getDialog().getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onTouchEvent(motionEvent);
                return true;
            }
        });
        initGestureDetector();
        return mView;
    }

    public void initGestureDetector() {
        detector = new GestureDetector(getActivity(), new MyGestureListener(
                new MyRightLeftListener() {
                    @Override
                    public void onRight() {
                        slideFocusChange(MyGestureListener.DIR_RIGHT);
                    }

                    @Override
                    public void onLeft() {
                        slideFocusChange(MyGestureListener.DIR_LEFT);
                    }

                    @Override
                    public void onUp() {
                        slideFocusChange(MyGestureListener.DIR_UP);
                    }

                    @Override
                    public void onDown() {
                        slideFocusChange(MyGestureListener.DIR_DOWN);
                    }

                    @Override
                    public void onSlide() {
                    }

                    @Override
                    public void onSingleTapUp() {
                        clicked();
                    }
                }));
    }

    public void moveContent(boolean isLeft) {
        hscrollView.smoothScrollBy(isLeft ? -480 : 480, 0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.topic_tag_confirm) {
            Constants.CURRENT_TOPIC = selectTopic;
            dismiss();
            if (onDialogAction != null) {
                onDialogAction.onDismiss();
            }
        }
    }

    public void selectedIndex(int index) {
        currentIndex = index;
        selectTopic = topicList.get(index);
        for (int i = 0; i < topicViews.size(); i++) {
            topicViews.get(i).setSelected(index == i);
            topicViews.get(i).setTextColorWhite();
            if (index == i) {
                //计算屏幕的宽度
                WindowManager wm1 = getActivity().getWindowManager();
                int screenWidth = wm1.getDefaultDisplay().getWidth();
                int rb_px = (int) topicViews.get(i).getX() + topicViews.get(i).getWidth() - topicViews.get(i).getWidth() / 3;
                hscrollView.smoothScrollTo(rb_px - screenWidth / 2, 0);
            }
        }
        hscrollView.clearFocus();
    }

    public void clearIndexSign() {
        for (int i = 0; i < topicViews.size(); i++) {
            topicViews.get(i).setSelected2(currentIndex == i);
        }
    }

    public void addIndexSing() {
        for (int i = 0; i < topicViews.size(); i++) {
            topicViews.get(i).setSelected(currentIndex == i);
        }
    }

    public void selectNextTopic() {
        if (currentIndex < topicList.size() - 1) {
            selectedIndex(currentIndex + 1);
        }
    }

    public void selectPreTopic() {
        if (currentIndex > 0) {
            selectedIndex(currentIndex - 1);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        confirmButton.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (CameraActivity.instance != null) {
            CameraActivity.instance.hideNavigation();
        }
    }


    public void slideFocusChange(int DIR) {
        if (DIR == MyGestureListener.DIR_DOWN) {
            if (tvHide.isFocused()) {
                confirmButton.requestFocus();
                clearIndexSign();
            } else if (btnExit.isFocused()) {
                tvHide.requestFocus();
                addIndexSing();
            }
        } else if (DIR == MyGestureListener.DIR_UP) {
            if (confirmButton.isFocused()) {
                tvHide.requestFocus();
                addIndexSing();
            } else if (tvHide.isFocused()) {
                btnExit.requestFocus();
                clearIndexSign();
            } else if (btnExit.isFocused()) {
                clearIndexSign();
                confirmButton.requestFocus();
            }
        } else if (DIR == MyGestureListener.DIR_LEFT) {
            confirmButton.clearFocus();
            tvHide.requestFocus();
            btnExit.clearFocus();
            selectPreTopic();
        } else if (DIR == MyGestureListener.DIR_RIGHT) {
            confirmButton.clearFocus();
            btnExit.clearFocus();
            tvHide.requestFocus();
            selectNextTopic();
        }
    }


    public void clicked() {
        if (btnExit.isFocused()) {
            btnExit.performClick();
            doClickAnim(btnExit);
        } else {
            if (!confirmButton.isFocused()) {
                confirmButton.requestFocus();
                clearIndexSign();
            } else if (confirmButton.isFocused()) {
                doClickAnim(confirmButton);
                confirmButton.performClick();
            }
        }
    }


    /**
     * 触屏事件
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    public interface OnDialogAction {
        void onDismiss();
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
