package com.bluesky.mallframe.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseFragment;
import com.zyyoona7.picker.DatePickerView;
import com.zyyoona7.picker.base.BaseDatePickerView;
import com.zyyoona7.picker.listener.OnDateSelectedListener;
import com.zyyoona7.wheel.WheelView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author BlueSky
 * @date 2020/4/20
 * Description:
 */
public class HomeFragment extends BaseFragment {
    private TextView mTvTitle;
    private WheelView<String> mWvWeek;
    private DatePickerView mDpvDate;

    private List<String> mListWeek = Arrays.asList("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日");


    @Override
    protected void initEvent() {
        mDpvDate.setOnDateSelectedListener(new OnDateSelectedListener(){
            @Override
            public void onDateSelected(BaseDatePickerView datePickerView, int year, int month, int day, @Nullable Date date) {
//                Snackbar.make();
            }
        });
    }

    @Override
    protected void initData() {
        mTvTitle.setText("这是主页面fragment!");
        LogUtils.d("主页面的fragment的数据被初始化了");

        mWvWeek.setData(mListWeek);
    }

    @Override
    protected void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_fragment_home_title);
        mWvWeek = view.findViewById(R.id.wv_week);
        mDpvDate = view.findViewById(R.id.dpv_date);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_home;
    }
}
