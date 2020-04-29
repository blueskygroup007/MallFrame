package com.bluesky.mallframe.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseFragment;
import com.bluesky.mallframe.bean.TurnSolution;
import com.bluesky.mallframe.bean.User;
import com.bluesky.mallframe.bean.WorkDay;
import com.bluesky.mallframe.bean.WorkDayKind;
import com.bluesky.mallframe.bean.WorkGroup;
import com.zyyoona7.picker.DatePickerView;
import com.zyyoona7.picker.base.BaseDatePickerView;
import com.zyyoona7.picker.listener.OnDateSelectedListener;
import com.zyyoona7.wheel.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.bluesky.mallframe.base.AppConstant.FORMAT_NO_SECS_DATE;
import static com.bluesky.mallframe.base.AppConstant.FORMAT_ONLY_DATE;

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

        //新建几个班组
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        Date date = calendar.getTime();
        String onlyDate = FORMAT_ONLY_DATE.format(date);
        WorkGroup group1 = new WorkGroup();
        group1.setNumber(1);
        group1.setName("甲");
        group1.setBasedate(onlyDate);

        WorkGroup group2 = new WorkGroup();
        group2.setNumber(1);
        group2.setName("乙");
        //Calendar.DAY_OF_YEAR和Calendar.DAY_OF_MONTH的区别应该是周期,比如2月28再加一就变成1号了
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        date = calendar.getTime();
        onlyDate = FORMAT_ONLY_DATE.format(date);

        group2.setBasedate(onlyDate);

        WorkGroup group3 = new WorkGroup();
        group3.setNumber(1);
        group3.setName("丙");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        date = calendar.getTime();
        onlyDate = FORMAT_ONLY_DATE.format(date);
        group3.setBasedate(onlyDate);

        //新建几个班型,白班,中班,夜班
        Calendar calendarWdk = Calendar.getInstance(Locale.getDefault());
        Date dateWdk = calendarWdk.getTime();
        String startTimeWdk = FORMAT_NO_SECS_DATE.format(dateWdk);

        calendarWdk.add(Calendar.HOUR_OF_DAY, 8);
        dateWdk = calendarWdk.getTime();
        String endTimeWd = FORMAT_NO_SECS_DATE.format(dateWdk);
        WorkDayKind kind1 = new WorkDayKind();
        kind1.setName("白班");
        kind1.setStarttime(startTimeWdk);
        kind1.setEndtime(endTimeWd);

        calendarWdk.add(Calendar.HOUR_OF_DAY, 8);
        dateWdk = calendarWdk.getTime();
        endTimeWd = FORMAT_NO_SECS_DATE.format(dateWdk);

        WorkDayKind kind2 = new WorkDayKind();
        kind2.setName("中班");
        kind2.setStarttime(startTimeWdk);
        kind2.setEndtime(endTimeWd);

        calendarWdk.add(Calendar.HOUR_OF_DAY, 8);
        dateWdk = calendarWdk.getTime();
        endTimeWd = FORMAT_NO_SECS_DATE.format(dateWdk);

        WorkDayKind kind3 = new WorkDayKind();
        kind3.setName("夜班");
        kind3.setStarttime(startTimeWdk);
        kind3.setEndtime(endTimeWd);


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
                            LogUtils.d("班组保存成功");

                        } else {
                            LogUtils.e("保存失败" + e.toString());
                        }
                    }
                });

                break;
            case R.id.btn_del:

                break;
            case R.id.btn_update:
                User user = BmobUser.getCurrentUser(User.class);
                user.setSolution(solution);
                user.setDesc("更新了2次数据");
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            solution = user.getSolution();
                            solution.setYourgroup(1);
                            solution.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        LogUtils.d("solution更新成功");
                                    } else {
                                        LogUtils.e("保存失败" + e.toString());

                                    }
                                }
                            });
                            LogUtils.d("user更新成功");
                        } else {
                            LogUtils.d("保存失败" + e.toString());
                        }
                    }
                });

                break;
            case R.id.btn_query:
                user = BmobUser.getCurrentUser(User.class);
                TurnSolution solution = user.getSolution();

                BmobQuery<TurnSolution> query = new BmobQuery<TurnSolution>();
//                query.include("yourgroup");
                query.getObject(solution.getObjectId(), new QueryListener<TurnSolution>() {
                    @Override
                    public void done(TurnSolution solution, BmobException e) {
                        if (e == null) {
                            List<WorkDayKind> listWorkDayKinds = solution.getWorkdaykinds();
                            List<WorkDay> listWorkDays = solution.getWorkdays();
                            LogUtils.d(solution.toString());
                            LogUtils.d(listWorkDayKinds.toString());
                            LogUtils.d(listWorkDays.toString());
                        } else {
                            LogUtils.e("查询失败" + e.toString());

                        }
                    }
                });


                break;

            default:
                break;
        }
    }
}
