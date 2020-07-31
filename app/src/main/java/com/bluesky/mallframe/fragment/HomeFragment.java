package com.bluesky.mallframe.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseFragment;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.User;
import com.bluesky.mallframe.data.WorkDay;
import com.bluesky.mallframe.data.WorkDayKind;
import com.bluesky.mallframe.data.WorkGroup;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.heweather.plugin.view.HeContent;
import com.heweather.plugin.view.HorizonView;
import com.heweather.plugin.view.LeftLargeView;
import com.heweather.plugin.view.RightLargeView;
import com.heweather.plugin.view.VerticalView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.bluesky.mallframe.base.AppConstant.FORMAT_ONLY_DATE;
import static com.bluesky.mallframe.base.AppConstant.FORMAT_ONLY_TIME_NO_SECS;

/**
 * @author BlueSky
 * @date 2020/4/20
 * Description:
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener {
    private List<TurnSolution> mListSolutions = new ArrayList<>();
    private Button mBtnAdd, mBtnDelete, mBtnUpdate, mBtnQuery;

    private List<String> mListWeek = Arrays.asList("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日");

    TurnSolution solution;
    private SolutionDataSource mRemote = new SolutionRemoteDataSource();


    //日历控件相关
    TextView mTextMonthDay;
    TextView mTextYear;
    TextView mTextLunar;
    TextView mTextCurrentDay;
    CalendarView mCalendarView;
    RelativeLayout mRelativeTool;
    CalendarLayout mCalendarLayout;
    private int mYear;

    private TurnSolution mSolution = null;
    private Map<String, com.haibin.calendarview.Calendar> mCalendarMap;

    //当日班次显示
    private TextView mTvWorkKind;


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
        /*第一组*/
        Calendar calendarWdk = Calendar.getInstance(Locale.getDefault());
        Date dateWdk = calendarWdk.getTime();
        String startTimeWdk = FORMAT_ONLY_TIME_NO_SECS.format(dateWdk);
        calendarWdk.add(Calendar.HOUR_OF_DAY, 8);
        dateWdk = calendarWdk.getTime();
        String endTimeWd = FORMAT_ONLY_TIME_NO_SECS.format(dateWdk);
        WorkDayKind kind1 = new WorkDayKind();
        kind1.setName("白班");
        kind1.setStarttime(startTimeWdk);
        kind1.setEndtime(endTimeWd);
        /*第二组*/
        startTimeWdk = FORMAT_ONLY_TIME_NO_SECS.format(dateWdk);
        calendarWdk.add(Calendar.HOUR_OF_DAY, 8);
        dateWdk = calendarWdk.getTime();
        endTimeWd = FORMAT_ONLY_TIME_NO_SECS.format(dateWdk);
        WorkDayKind kind2 = new WorkDayKind();
        kind2.setName("中班");
        kind2.setStarttime(startTimeWdk);
        kind2.setEndtime(endTimeWd);
        /*第三组*/
        startTimeWdk = FORMAT_ONLY_TIME_NO_SECS.format(dateWdk);
        calendarWdk.add(Calendar.HOUR_OF_DAY, 8);
        dateWdk = calendarWdk.getTime();
        endTimeWd = FORMAT_ONLY_TIME_NO_SECS.format(dateWdk);
        WorkDayKind kind3 = new WorkDayKind();
        kind3.setName("夜班");
        kind3.setStarttime(startTimeWdk);
        kind3.setEndtime(endTimeWd);


        solution = new TurnSolution();
        solution.getWorkdaykinds().add(kind1);
        solution.getWorkdaykinds().add(kind2);
        solution.getWorkdaykinds().add(kind3);

        solution.getWorkgroups().add(group1);
        solution.getWorkgroups().add(group2);
        solution.getWorkgroups().add(group3);

        WorkDay day1 = new WorkDay();
        day1.setNumber(1);
        day1.setWorkdaykindnumber(0);

        WorkDay day2 = new WorkDay();
        day2.setNumber(2);
        day1.setWorkdaykindnumber(1);

        WorkDay day3 = new WorkDay();
        day3.setNumber(3);
        day1.setWorkdaykindnumber(2);

        solution.getWorkdays().add(day1);
        solution.getWorkdays().add(day2);
        solution.getWorkdays().add(day3);

        solution.setCompany("二炼铁锅炉四期");
        solution.setName("加了休班222");
        solution.setUser(BmobUser.getCurrentUser(User.class));
        LogUtils.d(solution.toString());
    }


    @Override
    protected void initEvent() {
        mBtnAdd.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        LogUtils.d("主页面的fragment的数据被初始化了");
        if (mCalendarMap != null) {
            mCalendarView.setSchemeDate(mCalendarMap);
        }
        testData();
    }

    @Override
    protected void initView(View view) {
        //todo 知识点:fragment和activity中使用toolbar的不同.
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("今日");
        //todo 知识点:fragment中使用右上角菜单.
        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.menu_fragment_home);

        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnDelete = view.findViewById(R.id.btn_del);
        mBtnUpdate = view.findViewById(R.id.btn_update);
        mBtnQuery = view.findViewById(R.id.btn_query);


        mTextMonthDay = view.findViewById(R.id.tv_month_day);
        mTextYear = view.findViewById(R.id.tv_year);
        mTextLunar = view.findViewById(R.id.tv_lunar);
        mRelativeTool = view.findViewById(R.id.rl_tool);
        mCalendarView = view.findViewById(R.id.calendarView);
        mTextCurrentDay = view.findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        view.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = view.findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        //第三方字库测试
        mTvWorkKind = view.findViewById(R.id.tv_home_work_kind);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.kuhei);
        mTvWorkKind.setTypeface(typeface);

        //天气测试按钮
        Button btnWeather = view.findViewById(R.id.btn_weather);
        btnWeather.setOnClickListener(this);

        //天气控件
        initWeatherView(view);

    }

    private void initWeatherView(View view) {

//横向布局
        HorizonView horizonView = view.findViewById(R.id.horizon_view);
//取消默认背景
        horizonView.setDefaultBack(false);
//设置布局的背景圆角角度，颜色，边框宽度，边框颜色
        horizonView.setStroke(5, Color.BLUE, 1, Color.BLUE);
//添加地址文字描述，第一个参数为文字大小，单位：sp ，第二个参数为文字颜色，默认白色
        horizonView.addLocation(14, Color.WHITE);
//添加预警图标，参数为图标大小，单位：dp
        horizonView.addAlarmIcon(14);
//添加预警文字
        horizonView.addAlarmTxt(14);
//添加温度描述
        horizonView.addTemp(14, Color.WHITE);
//添加天气图标
        horizonView.addWeatherIcon(14);
//添加天气描述
        horizonView.addCond(14, Color.WHITE);
//添加风向图标
        horizonView.addWindIcon(14);
//添加风力描述
        horizonView.addWind(14, Color.WHITE);
//添加文字：AQI
        horizonView.addAqiText(14, Color.WHITE);
//添加空气质量描述
        horizonView.addAqiQlty(14);
//添加空气质量数值描述
        horizonView.addAqiNum(14);
//添加降雨图标
        horizonView.addRainIcon(14);
//添加降雨描述
        horizonView.addRainDetail(14, Color.WHITE);
//设置控件的对齐方式，默认居中
        horizonView.setViewGravity(HeContent.GRAVITY_CENTER);
//设置控件的内边距，默认为0
        horizonView.setViewPadding(10, 10, 10, 10);
//显示控件
        horizonView.show();

//左侧大布局右侧双布局控件
        LeftLargeView llView = view.findViewById(R.id.ll_view);

//获取左侧大布局
        LinearLayout leftLayout = llView.getLeftLayout();
//获取右上布局
        LinearLayout rightTopLayout = llView.getRightTopLayout();
//获取右下布局
        LinearLayout rightBottomLayout = llView.getRightBottomLayout();

//设置布局的背景圆角角度（单位：dp），颜色，边框宽度（单位：px），边框颜色
        llView.setStroke(5, Color.parseColor("#313a44"), 1, Color.BLACK);

//添加温度描述到左侧大布局
//第一个参数为需要加入的布局
//第二个参数为文字大小，单位：sp
//第三个参数为文字颜色，默认白色
        llView.addTemp(leftLayout, 40, Color.WHITE);
//添加温度图标到右上布局，第二个参数为图标宽高（宽高1：1，单位：dp）
        llView.addWeatherIcon(rightTopLayout, 14);
//添加预警图标到右上布局
        llView.addAlarmIcon(rightTopLayout, 14);
//添加预警描述到右上布局
        llView.addAlarmTxt(rightTopLayout, 14);
//添加文字AQI到右上布局
        llView.addAqiText(rightTopLayout, 14);
//添加空气质量到右上布局
        llView.addAqiQlty(rightTopLayout, 14);
//添加空气质量数值到右上布局
        llView.addAqiNum(rightTopLayout, 14);
//添加地址信息到右上布局
        llView.addLocation(rightTopLayout, 14, Color.WHITE);
//添加天气描述到右下布局
        llView.addCond(rightBottomLayout, 14, Color.WHITE);
//添加风向图标到右下布局
        llView.addWindIcon(rightBottomLayout, 14);
//添加风力描述到右下布局
        llView.addWind(rightBottomLayout, 14, Color.WHITE);
//添加降雨图标到右下布局
        llView.addRainIcon(rightBottomLayout, 14);
//添加降雨描述到右下布局
        llView.addRainDetail(rightBottomLayout, 14, Color.WHITE);
//设置控件的对齐方式，默认居中
        llView.setViewGravity(HeContent.GRAVITY_LEFT);
//显示布局
        llView.show();


        RightLargeView rlView = view.findViewById(R.id.rl_view);
//方法参数同（7）左侧大布局右侧双布局
        LinearLayout rightLayout = rlView.getRightLayout();
        LinearLayout leftTopLayout = rlView.getLeftTopLayout();
        LinearLayout leftBottomLayout = rlView.getLeftBottomLayout();

//取消默认背景
        rlView.setDefaultBack(false);
        rlView.setStroke(0, Color.GRAY, 1, Color.WHITE);
        rlView.addLocation(leftTopLayout, 14, Color.WHITE);
        rlView.addAqiText(leftTopLayout, 14);
        rlView.addAqiQlty(leftTopLayout, 14);
        rlView.addAqiNum(leftTopLayout, 14);
        rlView.addAlarmIcon(leftTopLayout, 14);
        rlView.addAlarmTxt(leftTopLayout, 14);
        rlView.addWeatherIcon(leftTopLayout, 14);

        rlView.addRainIcon(leftBottomLayout, 14);
        rlView.addRainDetail(leftBottomLayout, 14, Color.WHITE);
        rlView.addWindIcon(leftBottomLayout, 14);
        rlView.addWind(leftBottomLayout, 14, Color.WHITE);
        rlView.addCond(leftBottomLayout, 14, Color.WHITE);

        rlView.addTemp(rightLayout, 40, Color.WHITE);
        rlView.setViewGravity(HeContent.GRAVITY_RIGHT);
        rlView.show();


        VerticalView verticalView = view.findViewById(R.id.vertical_view);
        verticalView.setDefaultBack(false);
//方法参数同（6）横向布局
        verticalView.addLocation(14, Color.WHITE);
        verticalView.addTemp(14, Color.WHITE);
        verticalView.addWeatherIcon(14);
        verticalView.addCond(14, Color.WHITE);
        verticalView.addWindIcon(14);
        verticalView.addWind(14, Color.WHITE);
        verticalView.addAqiText(14, Color.WHITE);
        verticalView.addAqiQlty(14);
        verticalView.addAqiNum(14);
        verticalView.addAlarmIcon(14);
        verticalView.addAlarmTxt(14);
        verticalView.addRainIcon(14);
        verticalView.addRainDetail(14, Color.WHITE);
        verticalView.show();

    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void setData(Map<String, com.haibin.calendarview.Calendar> map) {
        if (mCalendarMap == null) {
            mCalendarMap = map;
            if (mCalendarView != null) {
                mCalendarView.setSchemeDate(map);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_weather:

                break;
            case R.id.btn_add:
                solution.setCompany("二炼铁" + Math.random());
                solution.setName("方案" + Math.random());
                addSolution();
                break;
            case R.id.btn_del:
                break;
            case R.id.btn_update:
                User user = BmobUser.getCurrentUser(User.class);
//                user.setSolution(solution);
                user.setDesc("更新了2次数据");
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            solution.setYourgroup(1);
                            mRemote.updateSolution(solution);
                            LogUtils.d("user更新成功");
                        } else {
                            LogUtils.d("保存失败" + e.toString());
                        }
                    }
                });

                break;
            case R.id.btn_query:
                mRemote.loadSolutions(new SolutionDataSource.LoadSolutionsCallback() {
                    @Override
                    public void onSolutionsLoaded(List<TurnSolution> solutions) {
                        LogUtils.d("查找所有solution:" + solutions.toString());
                        mListSolutions.clear();
                        mListSolutions.addAll(solutions);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        LogUtils.d("查找所有solution失败!");
                    }
                });
                break;

            default:
                break;
        }
    }


    private void addSolution() {
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
    }

    @Override
    public void onCalendarOutOfRange(com.haibin.calendarview.Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(com.haibin.calendarview.Calendar calendar, boolean isClick) {
//        mTextLunar.setVisibility(View.VISIBLE);
//        mTextYear.setVisibility(View.VISIBLE);
//        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
//        mTextYear.setText(String.valueOf(calendar.getYear()));
//        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onYearChange(int year) {
//        mTextMonthDay.setText(String.valueOf(year));
    }
}
