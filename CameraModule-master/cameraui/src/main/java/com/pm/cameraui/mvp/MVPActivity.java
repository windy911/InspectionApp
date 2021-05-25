package com.pm.cameraui.mvp;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pm.cameraui.Constants;
import com.pm.cameraui.R;
import com.pm.cameraui.base.BaseActivity;
import com.pm.cameraui.bean.InspectRecord;
import com.pm.cameraui.bean.UserInfo;
import com.pm.cameraui.utils.TimeUtil;


public class MVPActivity extends BaseActivity<MvpPresenter> implements MvpView {

    Button btnLogin, btnTopic, btnGetAppConfiguration, btnNewInspectRecord, btnGetInspectionList;
    TextView tvJson;

    @Override
    protected MvpPresenter createPresenter() {
        return new MvpPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mvp;
    }

    @Override
    protected void addListener() {

    }

    @Override
    protected void initView() {
        btnLogin = findViewById(R.id.btnLogin);
        btnTopic = findViewById(R.id.btnTopic);
        btnGetAppConfiguration = findViewById(R.id.btnGetAppConfiguration);
        btnNewInspectRecord = findViewById(R.id.btnNewInspectRecord);
        btnGetInspectionList = findViewById(R.id.btnGetInspectionList);
        tvJson = findViewById(R.id.tv_json);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login("13601751330", "123456");
            }
        });

        btnTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getInspectionTopic();
            }
        });

        btnGetAppConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getAppConfiguration();
            }
        });

        btnNewInspectRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InspectRecord inspectRecord = new InspectRecord();
                inspectRecord.setStartTimeLong(System.currentTimeMillis());
                inspectRecord.setStartTime(TimeUtil.getFormatDateTime(inspectRecord.getStartTimeLong()));
                if (Constants.CURRENT_TOPIC != null) {
                    inspectRecord.setTopicId(Constants.CURRENT_TOPIC.getId());
                } else {
                    //这里根据选择场景的TopicID来填入，139是硬编码“车间巡检”
                    inspectRecord.setTopicId(139l);
                }
                presenter.newInspectRecord(inspectRecord);
            }
        });

        btnGetInspectionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getInspectionList();
            }
        });
    }

    @Override
    public void onLogin(UserInfo userInfo) {
        if (userInfo != null) {
            Constants.userInfo = userInfo;
        }
        tvJson.setText(new Gson().toJson(userInfo));
    }

    @Override
    public void showJsonText(String text) {
        tvJson.setText(text);
    }
}
