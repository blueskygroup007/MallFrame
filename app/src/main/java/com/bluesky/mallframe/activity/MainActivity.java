package com.bluesky.mallframe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.bean.User;

public class MainActivity extends BaseActivity {
    public static final String PARAM = "USER";
    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra(PARAM);
        if (user != null) {
            mTvInfo.setText(user.toString());
        }
    }

    @Override
    protected void initView() {
        mTvInfo = findViewById(R.id.tv_activity_info);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }
}
