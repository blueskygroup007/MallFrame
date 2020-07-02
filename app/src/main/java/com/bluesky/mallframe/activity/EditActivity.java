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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.Iinformation;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.User;
import com.bluesky.mallframe.data.WorkDay;
import com.bluesky.mallframe.data.WorkDayKind;
import com.bluesky.mallframe.data.WorkGroup;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.bluesky.mallframe.ui.BSNumberPicker;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * todo:改进:每个编辑页面返回时,记录是否修改.用以统计总配置是否改动.
 */
public class EditActivity extends BaseActivity implements View.OnClickListener {
    public static final String FLAG_INTENT_DATA = "DATA_SOLUTION";
    public static final int REQUESTCODE = 4;
    private static boolean FLAG_MODIFIED = false;
    private Toolbar toolbar;
    private BSNumberPicker mNumberPicker;
    private TurnSolution mSolution;
    private EditText mEtCompany;
    private EditText mEtFlag;
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
    private InfoAdapter mAdapterWorkDayKind;
    private InfoAdapter mAdapterWorkGroup;
    private InfoAdapter mAdapterWorkDay;
    //    private CheckBox mCbDefault;
    private EditText mEtName;
    private TurnSolution mBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume(); /*当从其他设置页面返回时，更新Edit页面的预览信息（3个list，1个自动生成标签的hint）*//*todo 哪些放在onActivityResult中？*/
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_edit_work_day_kind:
                intent.putExtra(WorkDayKindSettingActivity.FLAG_INTENT_DATA, mSolution).setClass(this, WorkDayKindSettingActivity.class);
                startActivityForResult(intent, WorkDayKindSettingActivity.REQUESTCODE);
                break;
            case R.id.btn_edit_work_group:
                intent.putExtra(WorkGroupSettingActivity.FLAG_INTENT_DATA, mSolution).setClass(this, WorkGroupSettingActivity.class);
                startActivityForResult(intent, WorkGroupSettingActivity.REQUESTCODE);
                break;
            case R.id.btn_edit_work_day:
                intent.putExtra(WorkDaySettingActivity.FLAG_INTENT_DATA, mSolution).setClass(this, WorkDaySettingActivity.class);
                startActivityForResult(intent, WorkDaySettingActivity.REQUESTCODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //返回RESULT_OK就说明数据被修改过了.设定EditActivity的修改标志位
            FLAG_MODIFIED = true;
            switch (requestCode) {
                case WorkDayKindSettingActivity.REQUESTCODE:
                    mSolution = (TurnSolution) data.getSerializableExtra(WorkDayKindSettingActivity.FLAG_INTENT_DATA);
                    mAdapterWorkDayKind.setData(mSolution.getWorkdaykinds());
                    /* todo 知识点:notifyDataSetChanged()与 setAdapter()都能更新list.
                            但是由于getListViewSelfHeight计算list高度后,需要setAdapter.
                     */
                    //因为可能新增了item,所以这里重新计算list高度,并重设adapter
                    getListViewSelfHeight(mLvWorkDayKind);
                    mLvWorkDayKind.setAdapter(mAdapterWorkDayKind);
                    break;
                case WorkGroupSettingActivity.REQUESTCODE:
                    mSolution = (TurnSolution) data.getSerializableExtra(WorkGroupSettingActivity.FLAG_INTENT_DATA);
                    mAdapterWorkGroup.setData(mSolution.getWorkgroups());
                    getListViewSelfHeight(mLvWorkGroup);
                    mLvWorkGroup.setAdapter(mAdapterWorkGroup);
                    break;
                case WorkDaySettingActivity.REQUESTCODE:
                    mSolution = (TurnSolution) data.getSerializableExtra(WorkDaySettingActivity.FLAG_INTENT_DATA);
                    mAdapterWorkDay.setData(mSolution.getWorkdays());
                    getListViewSelfHeight(mLvWorkDay);
                    mLvWorkDay.setAdapter(mAdapterWorkDay);
                    break;
                default:
                    break;
            }
        }

    }

    class InfoAdapter extends BaseAdapter {
        private final Context mContext;
        private List mData;

        public InfoAdapter(List data, Context context) {
            mData = data;
            mContext = context;
        }

        public void setData(List data) {
            mData = data;
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
        public View getView(int position, View convertView, ViewGroup parent) { /*todo 如果data为空或null.那么将各组的title设置成:未设置*/
            TextView tvName = new TextView(mContext); /*todo 可以在这里判断mData.get()的类型.用以区分不同类型的输出方式:(WorkDay需要序号,如果不从WorkDay.number中取的话)*/
            tvName.setText(String.format("%s: %s", ((Iinformation) (mData.get(position))).getInfoName(), ((Iinformation) (mData.get(position))).getInfoDescribe()));
            return tvName;
        }
    }

    /**
     * Todo 知识点:当ScroolView包裹ListView时,只能显示一行的解决方法 @param listView
     */
    public void getListViewSelfHeight(ListView listView) { /* 获取ListView对应的Adapter*/
        ListAdapter listAdapter = listView.getAdapter(); /*健壮性的判断*/
        if (listAdapter == null) return;
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { /* listAdapter.getCount()返回数据项的数目*/
            View listItem = listAdapter.getView(i, null, listView); /* 调用measure方法 传0是测量默认的大小*/
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        } /*通过父控件进行高度的申请*/
        ViewGroup.LayoutParams params = listView.getLayoutParams(); /*listAdapter.getCount() - 1 从零开始 listView.getDividerHeight()获取子项间分隔符占用的高度*/
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void initEvent() {//todo 将List<object>转换成Map<String,?> List<Iinformation> dataWorkDayKinds =
//        list2map(mWorkDayKinds);
//        ArrayMap<String, String> dataWorkDays = list2map(mWorkDays);
//        ArrayMap<String, String> dataWorkGroups = list2map(mWorkGroups);
        /*todo 分别生成三个listview,来展示倒班数据*/
        mAdapterWorkDayKind = new InfoAdapter(mWorkDayKinds, this);
        mLvWorkDayKind.setAdapter(mAdapterWorkDayKind);
        mAdapterWorkGroup = new InfoAdapter(mWorkGroups, this);
        mLvWorkGroup.setAdapter(mAdapterWorkGroup);
        mAdapterWorkDay = new InfoAdapter(mWorkDays, this);
        mLvWorkDay.setAdapter(mAdapterWorkDay);
        /*重新计算三个ListView的高度*/
        getListViewSelfHeight(mLvWorkDayKind);
        getListViewSelfHeight(mLvWorkGroup);
        getListViewSelfHeight(mLvWorkDay);

        mBtnEditWorkDayKind.setOnClickListener(this);
        mBtnEditWorkGroup.setOnClickListener(this);
        mBtnEditWorkDay.setOnClickListener(this);
        //根据data初始化所有控件
        mEtName.setText(mSolution.getName());
        mEtCompany.setText(mSolution.getCompany());
        mEtFlag.setText(mSolution.getFlags());
//        mCbDefault.setChecked(mSolution.getActive());

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
        mSolution = (TurnSolution) intent.getSerializableExtra(FLAG_INTENT_DATA);
        mWorkDayKinds = mSolution.getWorkdaykinds();
        mWorkDays = mSolution.getWorkdays();
        mWorkGroups = mSolution.getWorkgroups();
        //todo 进度:1.备份solution
        //          2.返回时,把控件的值存回mSolution
        //          3.比对两个solution
        mBackup = mSolution.clone();
    }

    @Override
    protected void initView() {
        /*设置toolbar*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*显示返回按钮*/
        if (getSupportActionBar() != null) {
            LogUtils.d("toolbar found!");
            /*显示左上角返回图标*/
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            /*Toolbar的标题不能居中,使用Toolbar布局中的自定义TextView能实现*/
            getSupportActionBar().setTitle("编辑倒班");
            //显示应用程序图标
            /*            getSupportActionBar().setIcon(R.drawable.ic_launcher_foreground);*/
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
        mEtName = findViewById(R.id.et_solution_edit_name);
        mEtCompany = findViewById(R.id.et_solution_edit_company);
        mEtFlag = findViewById(R.id.et_solution_edit_flag);
//        mCbDefault = findViewById(R.id.cb_edit_default);
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

    @Override
    public void onBackPressed() {
        ifNeedSave();
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
                onBackPressed();
                return true;
            case R.id.menu_item_action_toolbar_save:
                //保存倒班信息(暂时废弃)
//                saveSolution();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void ifNeedSave() {
        String name = mEtName.getText().toString().trim();
        String company = mEtCompany.getText().toString().trim();
        String flag = mEtFlag.getText().toString().trim();
//        boolean active = mCbDefault.isChecked();

        LogUtils.d(name + mSolution.getName() + "---" + company + mSolution.getCompany()
                + "---" + flag + mSolution.getFlags()
        );
        if ((!FLAG_MODIFIED) && (name.equals(mSolution.getName())
                && company.equals(mSolution.getCompany())
                && flag.equals(mSolution.getFlags())
        )) {
            setResult(RESULT_CANCELED);
            finish();
        } else {

            //todo 未完成部分:这里应该加入空项检测
            //当前页面项修改过,或者3个list修改过,就直接保存退出
            Intent data = new Intent();
            data.putExtra(FLAG_INTENT_DATA, mSolution);
            setResult(RESULT_OK, data);
            saveSolution();
            finish();
        }
    }

    private void saveSolution(/*String name, String company, String flag, boolean active*/) {
        SolutionDataSource mRemote = new SolutionRemoteDataSource();
        mSolution.setName(mEtName.getText().toString().trim());
        mSolution.setCompany(mEtCompany.getText().toString().trim());
        mSolution.setFlags(mEtFlag.getText().toString().trim());

        mRemote.updateSolution(mSolution);


    }
}
