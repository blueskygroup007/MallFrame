package com.bluesky.mallframe.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseFragment;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.WorkDay;
import com.bluesky.mallframe.data.WorkDayKind;
import com.bluesky.mallframe.data.WorkGroup;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bluesky.mallframe.base.AppConstant.FORMAT_ONLY_DATE;

/**
 * @author BlueSky
 * @date 2020/4/20
 * Description: todo 修改了默认方案的话,重新回到CalendarFragment,需要刷新
 * //todo Bug:因为WorkDay里面存储的是序列化的对象,所以当WorkGroup和WorkDayKind被修改后,workdays里面存储的还是原来的对象
 * //todo 解决方案:WorkDay里只存储WorkDayKind的序号,就像solution里存储的getDefaultWorkGroup是序号一样.
 */
public class CalendarFragment extends BaseFragment implements CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    private TurnSolution mSolution = null;

    //    GroupRecyclerView mRecyclerView;

    int[] workDayColor = new int[]{0xFF000000, 0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFF800000, 0xFFff8c00, 0xFF808000, 0xFF00ffff};
    private Map<String, Calendar> mCalendarMap;
    private Map<String, Calendar> mGeneralMap;


    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_calendar);
        toolbar.setTitle("日历");
//        toolbar.setLogo(R.drawable.star);
        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.menu_fragment_calendar);


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

    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_calendar;

    }

    @Override
    public void setData(Map<String, Calendar> map) {
        if (mCalendarMap == null) {
            mCalendarMap = map;
            if (mCalendarView != null) {
                mCalendarView.setSchemeDate(map);
            }
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
/*        generateCalendarData(false);
        new MapTask().execute();*/
        if (mCalendarMap != null) {
            mCalendarView.setSchemeDate(mCalendarMap);
        }
    }

    class MapTask extends AsyncTask<Void, Void, Map<String, Calendar>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(mContext, "启动生成Map线程!!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Map<String, Calendar> doInBackground(Void... voids) {
            generateCalendarData(true);
            return null;
        }

        @Override
        protected void onPostExecute(Map<String, Calendar> stringCalendarMap) {
            super.onPostExecute(stringCalendarMap);
            Toast.makeText(mContext, "所有年份的Map已经生成!!!", Toast.LENGTH_SHORT).show();
        }
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
                    if (isAll) {
                        generateMapManyYear(mSolution);
                    } else {
                        generateMapCurrentYear(mSolution);
                    }
                }
            }

            @Override
            public void onDataNotAvailable() {
                LogUtils.d("CalendarFragment中获取solutions失败.....");
            }
        });

    }

    /**
     * 生成日历所需的map
     *
     * @param solution
     */
    private void generateMapManyYear(TurnSolution solution) {
        LogUtils.d("开始生成Map:-----所有年");

        //获取周期list和天数
        List<WorkDay> workdays = solution.getWorkdays();
        final int termDays = workdays.size();
        //获取默认班组和基准日期
//        final WorkGroup defaultWorkGroup = solution.getWorkgroups().get(solution.getYourgroup());
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
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        LogUtils.d("当前year=" + year + "年" + month + "月");
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        DateTime everyDay;
        Map<String, Calendar> map = new HashMap<>();

        //1.遍历当前年的每一天
        for (int y = 1950; y < 2050; y++) {
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
                        map.put(getSchemeCalendar(y, i, j, workDayColor[number], workdaykind.getName()).toString(),
                                getSchemeCalendar(y, i, j, workDayColor[number], workdaykind.getName()));
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mGeneralMap=map;
        mCalendarView.setSchemeDate(map);
        LogUtils.d("开始生成Map:-----所有年----完成");

    }

    /**
     * 生成日历所需的map
     *
     * @param solution
     */
    private void generateMapCurrentYear(TurnSolution solution) {
        LogUtils.d("开始生成Map:-----当前年");

        //获取周期list和天数
        List<WorkDay> workdays = solution.getWorkdays();
        final int termDays = workdays.size();
        //获取默认班组和基准日期
//        final WorkGroup defaultWorkGroup = solution.getWorkgroups().get(solution.getYourgroup());
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
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        LogUtils.d("当前year=" + year + "年" + month + "月");
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        DateTime everyDay;
        Map<String, Calendar> map = new HashMap<>();

        //1.遍历当前年的每一天
        calendar.set(java.util.Calendar.YEAR, year);
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
                    map.put(getSchemeCalendar(year, i, j, workDayColor[number], workdaykind.getName()).toString(),
                            getSchemeCalendar(year, i, j, workDayColor[number], workdaykind.getName()));
                } catch (Exception e) {
                    LogUtils.e(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
        LogUtils.d("开始生成Map:-----当前年----完成");

    }

    private int daysBetween(DateTime start, DateTime end) {
        return Days.daysBetween(start, end).getDays();
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


    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
        //todo 未决:是将year传递进去,根据切换的年份来生成当前年的Map,
        //          还是一次性生成若干年的.
//        generateCalendarData();

    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

}
