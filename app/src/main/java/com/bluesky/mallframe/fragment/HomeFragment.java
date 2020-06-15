package com.bluesky.mallframe.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private TextView mTvTitle;
    //    private WheelView<String> mWvWeek;
//    private DatePickerView mDpvDate;
    private ListView mLvSolutions;
    private SolutionAdapter mAdapter;
    private List<TurnSolution> mListSolutions = new ArrayList<>();
    private Button mBtnAdd, mBtnDelete, mBtnUpdate, mBtnQuery;

    private List<String> mListWeek = Arrays.asList("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日");

    TurnSolution solution;
    private SolutionDataSource mRemote = new SolutionRemoteDataSource();


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
        solution.setCompany("二炼铁锅炉四期");
        solution.getWorkdaykinds().add(kind1);
        solution.getWorkdaykinds().add(kind2);
        solution.getWorkdaykinds().add(kind3);

        solution.getWorkgroups().add(group1);
        solution.getWorkgroups().add(group2);
        solution.getWorkgroups().add(group3);

        WorkDay day1 = new WorkDay();
        day1.setNumber(1);
        day1.setWorkgroup(group3);
        day1.setWorkdaykind(kind1);

        WorkDay day2 = new WorkDay();
        day1.setNumber(2);
        day2.setWorkgroup(group3);
        day2.setWorkdaykind(kind2);

        WorkDay day3 = new WorkDay();
        day1.setNumber(3);
        day3.setWorkgroup(group3);
        day3.setWorkdaykind(kind3);

        solution.getWorkdays().add(day1);
        solution.getWorkdays().add(day2);
        solution.getWorkdays().add(day3);

        solution.setName("加了休班222");
        solution.setUser(BmobUser.getCurrentUser(User.class));
        LogUtils.d(solution.toString());
    }


    @Override
    protected void initEvent() {
/*        mDpvDate.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(BaseDatePickerView datePickerView, int year, int month, int day, @Nullable Date date) {
//                Snackbar.make();
            }
        });*/

        mBtnAdd.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
        mLvSolutions.setOnItemClickListener(this);
        mLvSolutions.setOnItemLongClickListener(this);
    }

    @Override
    protected void initData() {
        mTvTitle.setText("这是主页面fragment!");
        LogUtils.d("主页面的fragment的数据被初始化了");

        mAdapter = new SolutionAdapter();
        mLvSolutions.setAdapter(mAdapter);

//        mWvWeek.setData(mListWeek);
        testData();

    }

    @Override
    protected void initView(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.menu_fragment_home);


        mTvTitle = view.findViewById(R.id.tv_fragment_home_title);
//        mWvWeek = view.findViewById(R.id.wv_week);
//        mDpvDate = view.findViewById(R.id.dpv_date);

        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnDelete = view.findViewById(R.id.btn_del);
        mBtnUpdate = view.findViewById(R.id.btn_update);
        mBtnQuery = view.findViewById(R.id.btn_query);
        mLvSolutions = view.findViewById(R.id.lv_solution);

    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
//                solution.setName(solution.getName());
                addSolution();
                break;
            case R.id.btn_del:
                int position = mLvSolutions.getCheckedItemPosition();
                LogUtils.d("准备删除的位置是" + position);
                TurnSolution curSolution = mListSolutions.get(position);
                mRemote.deleteSolution(curSolution.getObjectId());
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
                        mAdapter.notifyDataSetChanged();
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

    /**
     * ListView的Item点击处理事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtils.d("点击了第" + position + "个选项=" + mListSolutions.get(position).getName());
        mLvSolutions.setSelection(position);
    }

    /**
     * ListView的Item长按点击处理事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //todo 显示删除按钮
        return false;
    }

    class SolutionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mListSolutions.size();
        }

        @Override
        public Object getItem(int position) {
            return mListSolutions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tvSolution = new TextView(HomeFragment.this.mContext);
            TurnSolution solution = mListSolutions.get(position);
            tvSolution.setText(solution.getName());
            return tvSolution;
        }
    }
}
