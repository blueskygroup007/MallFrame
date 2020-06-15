package com.bluesky.mallframe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.Iinformation;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.WorkDay;
import com.bluesky.mallframe.data.WorkDayKind;
import com.bluesky.mallframe.data.WorkGroup;
import com.bluesky.mallframe.ui.BSNumberPicker;

import java.util.List;

/**
 * todo:改进:每个编辑页面返回时,记录是否修改.用以统计总配置是否改动.
 */
public class EditActivity extends BaseActivity implements View.OnClickListener {
    public static final String DATA_SOLUTION = "DATA_SOLUTION";
    private Toolbar toolbar;

    private BSNumberPicker mNumberPicker;
    private TurnSolution mSolution;
    private List<WorkDayKind> mWorkDayKinds;
    private List<WorkDay> mWorkDays;
    private List<WorkGroup> mWorkGroups;

    private ListView mLvWorkDayKind;
    private ListView mLvWorkGroup;
    private ListView mLvWorkDay;

    private TextView mTvTitleWorkDayKind;
    private TextView mTvTitleWorkGroup;
    private TextView mTvTitleWorkDay;

    private Button mBtnEditWorkDayKind;
    private Button mBtnEditWorkGroup;
    private Button mBtnEditWorkDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_edit_work_day_kind:
                intent.putExtra(WorkDayKindSettingActivity.FLAG_INTENT_DATA, mSolution);
                intent.setClass(this, WorkDayKindSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_edit_work_group:
                intent.putExtra(WorkGroupSettingActivity.FLAG_INTENT_DATA, mSolution);
                intent.setClass(this, WorkGroupSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_edit_work_day:
                intent.putExtra(WorkDaySettingActivity.FLAG_INTENT_DATA, mSolution);
                intent.setClass(this, WorkDaySettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class InfoAdapter extends BaseAdapter {
        private final Context mContext;
        private final List mData;

        public InfoAdapter(List data, Context context) {
            mData = data;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //todo 如果data为空或null.那么将各组的title设置成:未设置
            TextView tvName = new TextView(mContext);
            //todo 可以在这里判断mData.get()的类型.用以区分不同类型的输出方式:(WorkDay需要序号,如果不从WorkDay.number中取的话)
            tvName.setText(String.format("%s: %s", ((Iinformation) (mData.get(position))).getInfoName(), ((Iinformation) (mData.get(position))).getInfoDescribe()));
            return tvName;
        }
    }

    /**
     * Todo 知识点:当ScroolView包裹ListView时,只能显示一行的解决方法
     *
     * @param listView
     */
    public void getListViewSelfHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        //健壮性的判断
        if (listAdapter == null) {
            return;
        }
        // 统计所有子项的总高度
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 调用measure方法 传0是测量默认的大小
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //通过父控件进行高度的申请
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //listAdapter.getCount() - 1 从零开始 listView.getDividerHeight()获取子项间分隔符占用的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void initEvent() {
        //todo 将List<object>转换成Map<String,?>
//        List<Iinformation> dataWorkDayKinds = list2map(mWorkDayKinds);
//        ArrayMap<String, String> dataWorkDays = list2map(mWorkDays);
//        ArrayMap<String, String> dataWorkGroups = list2map(mWorkGroups);
        //todo 分别生成三个listview,来展示倒班数据
        mLvWorkDayKind.setAdapter(new InfoAdapter(mWorkDayKinds, this));
        mLvWorkGroup.setAdapter(new InfoAdapter(mWorkGroups, this));
        mLvWorkDay.setAdapter(new InfoAdapter(mWorkDays, this));
        //重新计算三个ListView的高度
        getListViewSelfHeight(mLvWorkDayKind);
        getListViewSelfHeight(mLvWorkGroup);
        getListViewSelfHeight(mLvWorkDay);

        mBtnEditWorkDayKind.setOnClickListener(this);
        mBtnEditWorkGroup.setOnClickListener(this);
        mBtnEditWorkDay.setOnClickListener(this);

    }


    /*
     * Todo 知识点:将List转换为Map(给simpleAdapter使用)
     *       java8的stream和collectors.toMap方法需要API24.
     *       Guava貌似是给对象加唯一的序号 Maps.uniqueIndex
     * 未解决的疑点:除了反射外,如何调用泛型的方法,最终采用了接口代替泛型
     * */
    public <T> ArrayMap<String, String> list2map(List<T> list) {
        ArrayMap<String, String> map = new ArrayMap<>();
        for (T t :
                list) {
            map.put(((Iinformation) t).getInfoName(), ((Iinformation) t).getInfoDescribe());
        }
        return map;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mSolution = (TurnSolution) intent.getSerializableExtra(DATA_SOLUTION);
        mWorkDayKinds = mSolution.getWorkdaykinds();
        mWorkDays = mSolution.getWorkdays();
        mWorkGroups = mSolution.getWorkgroups();
    }

    @Override
    protected void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //显示返回按钮
        if (getSupportActionBar() != null) {
            LogUtils.d("toolbar found!");
            //显示左上角返回图标
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            //Toolbar的标题不能居中,使用Toolbar布局中的自定义TextView能实现
            getSupportActionBar().setTitle("编辑倒班");
            //显示应用程序图标
//            getSupportActionBar().setIcon(R.drawable.ic_launcher_foreground);
        } else {
            LogUtils.d("toolbar not found!");
        }

        mNumberPicker = findViewById(R.id.np_group_count);
        mLvWorkDayKind = findViewById(R.id.lv_work_day_kind);
        mLvWorkGroup = findViewById(R.id.lv_work_group);
        mLvWorkDay = findViewById(R.id.lv_work_day);
        mTvTitleWorkDayKind = findViewById(R.id.tv_title_work_day_kind);
        mTvTitleWorkGroup = findViewById(R.id.tv_title_work_group);
        mTvTitleWorkDay = findViewById(R.id.tv_title_work_day);
        mBtnEditWorkDayKind = findViewById(R.id.btn_edit_work_day_kind);
        mBtnEditWorkGroup = findViewById(R.id.btn_edit_work_group);
        mBtnEditWorkDay = findViewById(R.id.btn_edit_work_day);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_edit;
    }

    /**
     * 生成toolbar右上角的自定义按钮
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_edit, menu);
        return true;
    }

    /**
     * toolbar自定义按钮的点击事件处理
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_item_action_toolbar_save:
                //todo 保存倒班信息
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
