package com.bluesky.mallframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.activity.EditActivity;
import com.bluesky.mallframe.base.BaseFragment;

/**
 * @author BlueSky
 * @date 2020/4/20
 * Description:
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvTitle;
    private Button mBtnEdit;

    @Override
    protected void initEvent() {
        mBtnEdit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTvTitle.setText("这是我的页面fragment!");
        LogUtils.d("我的页面的fragment的数据被初始化了");
    }

    @Override
    protected void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_fragment_personal_title);
        mBtnEdit = view.findViewById(R.id.btn_personal_edit);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personal_edit:
                Intent intent = new Intent(mContext, EditActivity.class);
                startActivity(intent);
                break;

            default:

        }
    }
}
