package com.bluesky.mallframe.fragment;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseFragment;
import com.bluesky.mallframe.bean.TurnSolution;
import com.bluesky.mallframe.bean.WorkDay;
import com.bluesky.mallframe.bean.WorkDayKind;
import com.bluesky.mallframe.bean.WorkGroup;
import com.zyyoona7.picker.DatePickerView;
import com.zyyoona7.picker.base.BaseDatePickerView;
import com.zyyoona7.picker.listener.OnDateSelectedListener;
import com.zyyoona7.wheel.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author BlueSky
 * @date 2020/4/20
 * Description:
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvTitle;
    private WheelView<String> mWvWeek;
    private DatePickerView mDpvDate;
    private Button mBtnAdd, mBtnDelete, mBtnUpdate, mBtnQuery;

    private List<String> mListWeek = Arrays.asList("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日");

    TurnSolution solution;


    private void testData() {
        WorkGroup group1 = new WorkGroup();
        group1.setNumber(1);
        group1.setName("甲");
        group1.setBasedate(new BmobDate(new Date()));

        WorkGroup group2 = new WorkGroup();
        group2.setNumber(1);
        group2.setName("乙");
        group2.setBasedate(new BmobDate(new Date()));

        WorkGroup group3 = new WorkGroup();
        group3.setNumber(1);
        group3.setName("丙");
        group3.setBasedate(new BmobDate(new Date()));


        WorkDayKind kind1 = new WorkDayKind();
        kind1.setName("白班");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String startTime = sdf.format(date);
        kind1.setStarttime(startTime);

        WorkDayKind kind2 = new WorkDayKind();
        kind2.setName("中班");
        kind2.setStarttime(startTime);

        WorkDayKind kind3 = new WorkDayKind();
        kind3.setName("夜班");
        kind3.setStarttime(startTime);


        solution = new TurnSolution();
        solution.setWorkdaykinds(new ArrayList<WorkDayKind>() {{
            add(kind1);
            add(kind2);
            add(kind3);
        }});
        solution.setWorkgroups(new ArrayList<WorkGroup>() {{
            add(group1);
            add(group2);
            add(group3);

        }});
        solution.setName("唐钢二炼铁");
        solution.setYourgroup(group1);


        WorkDay day1 = new WorkDay();
        day1.setWorkgroup(group3);
        day1.setWorkdaykind(kind1);

        WorkDay day2 = new WorkDay();
        day2.setWorkgroup(group3);
        day2.setWorkdaykind(kind2);

        WorkDay day3 = new WorkDay();
        day3.setWorkgroup(group3);
        day3.setWorkdaykind(kind3);

        solution.setWorkdays(new ArrayList<WorkDay>() {{
            add(day1);
            add(day2);
            add(day3);

        }});

        LogUtils.d(solution.toString());
    }


    @Override
    protected void initEvent() {
        mDpvDate.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(BaseDatePickerView datePickerView, int year, int month, int day, @Nullable Date date) {
//                Snackbar.make();
            }
        });

        mBtnAdd.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTvTitle.setText("这是主页面fragment!");
        LogUtils.d("主页面的fragment的数据被初始化了");

        mWvWeek.setData(mListWeek);
        testData();
    }

    @Override
    protected void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_fragment_home_title);
        mWvWeek = view.findViewById(R.id.wv_week);
        mDpvDate = view.findViewById(R.id.dpv_date);

        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnDelete = view.findViewById(R.id.btn_del);
        mBtnUpdate = view.findViewById(R.id.btn_update);
        mBtnQuery = view.findViewById(R.id.btn_query);

    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                solution.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            LogUtils.d("保存成功");
                        } else {
                            LogUtils.e("保存失败" + e.toString());
                        }
                    }
                });
                break;
            case R.id.btn_del:

                break;
            case R.id.btn_update:

                break;
            case R.id.btn_query:

                break;

            default:
                break;
        }
    }
}
