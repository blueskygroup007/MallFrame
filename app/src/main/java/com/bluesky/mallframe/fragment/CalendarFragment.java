package com.bluesky.mallframe.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseFragment;

/**
 * @author BlueSky
 * @date 2020/4/20
 * Description:
 */
public class CalendarFragment extends BaseFragment {

    private TextView mTvTitle;

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        mTvTitle.setText("这是日历页面fragment!");
        LogUtils.d("日历页面的fragment的数据被初始化了");
    }

    @Override
    protected void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_fragment_calendar_title);

    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_calendar;

    }
}
