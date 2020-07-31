package com.bluesky.mallframe.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.base.BaseFragment;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.User;
import com.bluesky.mallframe.data.WorkDay;
import com.bluesky.mallframe.data.WorkDayKind;
import com.bluesky.mallframe.data.WorkGroup;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.bluesky.mallframe.fragment.CalendarFragment;
import com.bluesky.mallframe.fragment.HomeFragment;
import com.bluesky.mallframe.fragment.PersonalFragment;
import com.haibin.calendarview.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

import static com.bluesky.mallframe.base.AppConstant.FORMAT_ONLY_DATE;

public class MainActivity extends BaseActivity {
    public static final String FLAG_INTENT_DATA = "DATA_USER";

    private RadioGroup mRgMain;

    private List<BaseFragment> mFragments;
    private BaseFragment currentFragment;
    private int position = 0;
    private TurnSolution mSolution;

    private Map<String, Calendar> mCalendarMap = new HashMap<>();

    /**
     * 给fragment提供Map数据
     *
     * @return
     */
    public Map<String, Calendar> getCalendarMap() {
        return mCalendarMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initEvent() {

        initFragment();

        mRgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        position = 0;
                        break;
                    case R.id.rb_calendar:
                        position = 1;
                        break;
                    case R.id.rb_personal:
                        position = 2;
                        break;
                    default:
                        break;
                }
                BaseFragment nextFragment = mFragments.get(position);
                switchFragment(currentFragment, nextFragment);
            }
        });
        //设置默认首页(会引发RadioGroup的Change事件)
        mRgMain.check(R.id.rb_home);
    }

    @Override
    protected void initData() {
        LogUtils.d("MainActivity initData()");
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra(FLAG_INTENT_DATA);
        if (user != null) {
            //todo 计划在这里获取三个fragment所需的所有后台数据。
        } else {
            Toast.makeText(this, "获取用户失败！！", Toast.LENGTH_SHORT).show();
        }

        generateCalendarData(true);
    }

    @Override
    protected void initView() {
/*        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //显示返回按钮
        if (getSupportActionBar() != null) {
            LogUtils.d("toolbar found!");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("倒班本首页");
        } else {
            LogUtils.d("toolbar not found!");
        }*/
        mRgMain = findViewById(R.id.rg_main);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new CalendarFragment());
        mFragments.add(new PersonalFragment());

    }

    /**
     * 从集合中取fragment
     */
    private BaseFragment getFragment(int position) {
        if (mFragments != null && mFragments.size() > 0) {
            return mFragments.get(position);
        }

        return null;
    }

    private void switchFragment(BaseFragment fromFragment, BaseFragment nextFragment) {
        if (currentFragment != nextFragment) {
            currentFragment = nextFragment;
            //目标fragment不能为空
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //目标fragment没有被add过的情况:
                if (!nextFragment.isAdded()) {
                    //源fragment不为空则隐藏
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    //添加目标fragment并提交
                    transaction.add(R.id.main_frame, nextFragment).commit();
                } else {
                    //目标fragment已经被add过的情况:
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(nextFragment).commit();
                }
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BmobUser.logOut();
    }


    private void generateCalendarData(boolean isAll) {
        //1.获取active的solution
        SolutionRemoteDataSource source = new SolutionRemoteDataSource();
        source.loadSolutions(new SolutionDataSource.LoadSolutionsCallback() {
            @Override
            public void onSolutionsLoaded(List<TurnSolution> solutions) {
                LogUtils.d("CalendarFragment中获取solutions==" + solutions);
                for (TurnSolution solution :
                        solutions) {
                    if (solution.getActive()) {
                        mSolution = solution;
                    }
                }
                //2.根据solution来生成map
                if (mSolution != null) {
                    try {
                        mCalendarMap = generateMapManyYear(mSolution, isAll);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e(e.getMessage());
                    }
                    setMaptoFragment(mCalendarMap);
                }
            }

            @Override
            public void onDataNotAvailable() {
                LogUtils.d("CalendarFragment中获取solutions失败.....");
            }
        });

    }

    /**
     * 给每个fragment发送map数据
     *
     * @param map
     */
    private void setMaptoFragment(Map<String, Calendar> map) {
        for (BaseFragment fragment :
                mFragments) {
            fragment.setData(map);
        }
    }

    int[] workDayColor = new int[]{0xFF000000, 0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFF800000, 0xFFff8c00, 0xFF808000, 0xFF00ffff};

    /**
     * 生成每个班次对应的颜色
     *
     * @param solution
     * @return
     * @throws Exception
     */
    private List<Integer> generateWorkDayColor(TurnSolution solution) throws Exception {
        List<WorkDayKind> workDayKinds = solution.getWorkdaykinds();
        int count = workDayKinds.size();
        if (count > 250) {
            throw new Exception("the work day cann't above 250");
        }
        List<Integer> HSVColors = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            if (0 == i) {
                HSVColors.add(0xFF000000);
            } else {
                //todo 知识点：获取渐变色,均分color,适合用HSV.(H取值360,S取值0~1,V取值0~1)
                HSVColors.add(Color.HSVToColor(new float[]{i * 360 / count, 1.0f, 1.0f}));
            }
        }
        LogUtils.d("颜色值为:" + HSVColors.toString());


        return HSVColors;
    }

    /**
     * 生成日历所需的map
     *
     * @param solution
     */
    private Map<String, Calendar> generateMapManyYear(TurnSolution solution, boolean fullYear) throws Exception {
        LogUtils.d("开始生成Map:-----所有年");

        //获取周期list和天数
        List<WorkDay> workdays = solution.getWorkdays();
        final int termDays = workdays.size();
        //获取默认班组和基准日期
        final WorkGroup defaultWorkGroup = solution.getWorkgroups().get(solution.getDefaultWorkGroup());
        java.util.Calendar baseDate = java.util.Calendar.getInstance();

        try {
            Date date = FORMAT_ONLY_DATE.parse(defaultWorkGroup.getBasedate());
            baseDate.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateTime baseTime = new DateTime(baseDate);

        LogUtils.d("baseTime=" + baseTime);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        DateTime everyDay;
        Map<String, Calendar> map = new HashMap<>();
        //设置Scheme的两个范围:当前年,所有年.
        java.util.Calendar curCalendar = java.util.Calendar.getInstance();
        int currentYear = curCalendar.get(java.util.Calendar.YEAR);
        int startYear = currentYear;
        int endYear = currentYear;
        if (fullYear) {
            startYear = currentYear - 50;
            endYear = currentYear + 50;
        } else {
            startYear = currentYear;
            endYear = currentYear;
        }
        List<Integer> workDayColors = generateWorkDayColor(solution);
        //1.遍历当前年的每一天
        for (int y = startYear; y < endYear; y++) {
            calendar.set(java.util.Calendar.YEAR, y);
            for (int i = 1; i <= 12; i++) {
                calendar.set(java.util.Calendar.MONTH, i - 1);
                for (int j = 1; j <= calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH); j++) {
                    calendar.set(java.util.Calendar.DAY_OF_MONTH, j);
                    everyDay = new DateTime(calendar);
                    //2.计算两个日期相差天数,可以为负值,所以用绝对值
                    int interval = Math.abs(daysBetween(everyDay, baseTime));
//                LogUtils.d("everyDay=" + everyDay + "| interval=" + interval);
                    //取余
                    int number = interval % termDays;
                    //todo 3.生成每一个Scheme
                    try {
                        WorkDayKind workdaykind = mSolution.getWorkdaykinds().get(workdays.get(number).getWorkdaykindnumber());
                        map.put(getSchemeCalendar(y, i, j, workDayColors.get(workdaykind.getNumber()), workdaykind.getName()).toString(),
                                getSchemeCalendar(y, i, j, workDayColors.get(workdaykind.getNumber()), workdaykind.getName()));

                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        return map;
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
//        calendar.addScheme(new Calendar.Scheme());
//        calendar.addScheme(0xFF008800, "假");
//        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    private int daysBetween(DateTime start, DateTime end) {
        return Days.daysBetween(start, end).getDays();
    }
}
