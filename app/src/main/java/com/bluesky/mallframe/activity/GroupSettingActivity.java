package com.bluesky.mallframe.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.WorkGroup;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.bluesky.mallframe.ui.BSNumberPicker;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.bluesky.mallframe.base.AppConstant.FORMAT_ONLY_DATE;
import static com.bluesky.mallframe.data.WorkGroup.FLAG_DEFAULT_WORKGROUP;


public class GroupSettingActivity extends BaseActivity {

    public static final String FLAG_INTENT_DATA = "DATA_WORK_GROUP";
    //控件
    private BSNumberPicker mNumberPicker;
    private RecyclerView mRecyclerView;
    private TextView mTvMsg;
    //数据
//    private List<TurnSolution> mSolutions = new ArrayList<>();
    private TurnSolution mSolution;
    private List<WorkGroup> mWorkgroups;
    private RvGroupAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {
        mNumberPicker.setOnNumberChangeListener(new BSNumberPicker.OnNumberChangeListener() {
            @Override
            public void onNumberInc(int number) {
                if (number > mWorkgroups.size()) {
                    WorkGroup workGroup = new WorkGroup();
                    mAdapter.getList().add(workGroup);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNumberDec(int number) {
                if (number < mWorkgroups.size()) {
                    mAdapter.getList().remove(mAdapter.getList().size() - 1);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void initData() {
        //todo 如果有传过来的数据,那么按照参数设置界面初始值(List<WorkGroup>)

        //获取Intent传递来的List数据
        //todo 不应该是solution,只需List<WorkGroup>
        Intent intent = getIntent();
        mSolution = (TurnSolution) intent.getSerializableExtra(FLAG_INTENT_DATA);

        mWorkgroups = mSolution.getWorkgroups();
        LogUtils.d(mWorkgroups);

        mNumberPicker.setNumber(mWorkgroups.size());//默认就是1


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RvGroupAdapter(mWorkgroups);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //显示返回按钮
        if (getSupportActionBar() != null) {
            LogUtils.d("toolbar found!");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("编辑班组");
        } else {
            LogUtils.d("toolbar not found!");
        }

        mNumberPicker = findViewById(R.id.np_group_count);
        mRecyclerView = findViewById(R.id.rv_group_list);
        mTvMsg = findViewById(R.id.tv_group_setting_message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        String result = checkAllSetting(mAdapter.getList());
        mTvMsg.setText("提示:" + result);
        /*没有改动,直接退出*/
        if (result.equals(MSG_NO_CHANGE)) {
            finish();
        }
        /*修改正确,弹出是否保存对话框*/
        if (result.equals(MSG_OK)) {
            showNormalDialog();
        }
    }

    /**
     * 是否保存的对话框
     */
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.ic_save_black_24dp);
        normalDialog.setTitle("保存");
        normalDialog.setMessage("您修改了设置,是否保存?");

        normalDialog.setPositiveButton("保存",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        SolutionDataSource mRemote = new SolutionRemoteDataSource();
                        mSolution.setWorkgroups(mWorkgroups);
                        mRemote.updateSolution(mSolution);
                        dialog.dismiss();
                        finish();
                    }
                });
        normalDialog.setNegativeButton("不保存",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                        finish();
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_group_setting;
    }


    /**
     * RecyclerView的自定义适配器
     */
    private class RvGroupAdapter extends RecyclerView.Adapter<RvGroupAdapter.ViewHolder> {
        private List<WorkGroup> mBackupList = new ArrayList<>();
        private List<WorkGroup> mList;

        public List<WorkGroup> getList() {
            return mList;
        }

        public boolean isChanged() {
            LogUtils.d(mList.toString() + "-----" + mBackupList.toString());
            return !isListEqual(mList, mBackupList);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTvNumber;
            EditText mEtName;
            TextView mTvBaseDate;
            CheckBox mCbDefault;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvNumber = itemView.findViewById(R.id.tv_number);
                mEtName = itemView.findViewById(R.id.et_name);
                mTvBaseDate = itemView.findViewById(R.id.tv_base_date);
                mCbDefault = itemView.findViewById(R.id.cb_yours);

            }
        }

        public RvGroupAdapter(List<WorkGroup> list) {
            mList = list;
            for (WorkGroup workGroup :
                    list) {
                mBackupList.add(workGroup.clone());
            }

            LogUtils.d("两个list的内容=" + mList + "---" + mBackupList);
            LogUtils.d("两个list的hashcode=" + mList.hashCode() + "---" + mBackupList.hashCode());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_card_style_layout, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            /*在这里清除监听器(成功!)*/
            holder.mEtName.removeTextChangedListener((TextWatcher) holder.mEtName.getTag());
            /*继续清除checkbox的监听器*/
            holder.mCbDefault.setOnCheckedChangeListener(null);
        }

        /**
         * //todo 知识点:关于在该方法中,给Edittext绑定TextChangeListener时,遇到ViewHolder的复用会导致显示混乱问题的解决方案.
         * //todo (参考网上文章自己实现的)因为Edittext的TextChangeListener可以add多个.所以重复后就会乱.因此,在onViewRecycled
         * //todo 方法中,将TextWatcher(使用setTag()方法绑定在任意的控件上)移除.
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(@NonNull RvGroupAdapter.ViewHolder holder, int position) {
            //1.使用强制停用RecycleView的复用,来解决监听错乱问题(好用)
//            holder.setIsRecyclable(false);
            //2.也可以通过view的setTag()方法解决Recyclerview的复用(测试不可用)

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
//                    mList.get(Integer.parseInt(holder.mTvNumber.getText().toString()) - 1).setName(s.toString());
                    mList.get(position).setName(s.toString());
                }
            };

            //todo 已经在回收方法里移除了.这里应该就不必要了.但是有这一句也不影响
            holder.mEtName.removeTextChangedListener(textWatcher);

            //3.先清除监听器,修改后再设置监听器(也未成功....)
            WorkGroup workGroup = mList.get(position);
            holder.mTvNumber.setText(String.valueOf(position + 1));
            if (!(workGroup.getName() == null) && !("".equals(workGroup.getName()))) {
                holder.mEtName.setText(workGroup.getName());
            } else {
                holder.mEtName.setText("");
            }
            holder.mTvBaseDate.setText(workGroup.getBasedate());
            holder.mCbDefault.setChecked(FLAG_DEFAULT_WORKGROUP.equals(workGroup.getFlag()));
            LogUtils.d(FLAG_DEFAULT_WORKGROUP.equals(workGroup.getFlag()));
            /*给基准日期设置弹窗监听*/
            holder.mTvBaseDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseDateDialog(mList, position);
                }
            });
            /*勾选默认,给checkbox设置监听*/
            holder.mCbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mList.get(position).setFlag(FLAG_DEFAULT_WORKGROUP);
                    } else {
                        mList.get(position).setFlag("");

                    }
                    LogUtils.d(mList.get(position).getFlag() + "----" + mBackupList.get(position).getFlag());
                }
            });
            holder.mEtName.addTextChangedListener(textWatcher);
            holder.mEtName.setTag(textWatcher);
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

        /**
         * 弹出一个日期选择框
         *
         * @param list     整个儿RecycleView的list
         * @param position 当前设置的日期所属的item
         */
        public void chooseDateDialog(List<WorkGroup> list, int position) {

            String date = list.get(position).getBasedate();
            //字符串日期转化成Date和Calendar
            Date curDate = stringToDate(date);


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);

            LogUtils.d("传入date=" + date);
            LogUtils.d("传入Date=" + curDate);
            LogUtils.d("传入Calendar=" + calendar);

            //弹日期选择窗
            DatePickerDialog dialog = new DatePickerDialog(
                    GroupSettingActivity.this,
                    DatePickerDialog.THEME_TRADITIONAL,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            //日期设定回调
                            /*不使用Date,因为year存在1900年起始问题*/
                            LogUtils.d("返回的日期是:%d年%d月%d日", year, month, dayOfMonth);
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(year, month, dayOfMonth);
                            String modifyStringDate = FORMAT_ONLY_DATE.format(calendar1.getTime());
                            LogUtils.d("返回的Calendar=" + modifyStringDate);

                            //todo 选定日期后的处理
                            list.get(position).setBasedate(modifyStringDate);
                            notifyDataSetChanged();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }


    }

    /**
     * todo 知识点:判断原始list和修改后的list是否不同(即是否当前页面被修改)
     *
     * @param list
     * @param backupList
     * @return
     */
    private boolean isListEqual(List<WorkGroup> list, List<WorkGroup> backupList) {
        /*这里采用了第三方库
         * 方法1:使用List.contains()方法互相比较
         *
         * */
        return CollectionUtils.isEqualCollection(list, backupList);
    }

    /**
     * todo 方法1:使用List.containsAll()方法互相比较,containsAll又依赖list中对象的Equals(),所以必须重写
     * todo 查看WorkGroup类的Equals()方法,了解Equals和HashCode的区别与不同
     *
     * @param list
     * @param backupList
     * @return
     */
    private boolean isListEqual1(List list, List backupList) {
        if (list.containsAll(backupList)) {
            if (backupList.containsAll(list))
                return true;
        }
        return false;
    }

    /**
     * 基准时间从字符串转换成Date
     *
     * @param date
     * @return
     */
    private Date stringToDate(String date) {
        if (date == null || date.isEmpty()) {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            return calendar.getTime();
        } else {
            try {
                return FORMAT_ONLY_DATE.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static final String MSG_BLANK_IS_EMPTY = "存在空项!";
    public static final String MSG_BLANK_IS_REPEAT = "项目重复!";
    public static final String MSG_DUFAULT_IS_UNCHECKED = "默认未设置!";
    public static final String MSG_NO_CHANGE = "无改动";
    public static final String MSG_OK = "修改正确!";


    /**
     * 检查页面所有设置,(填写错误,未勾选,无改动等等)
     *
     * @param srcList
     * @return
     */
    private String checkAllSetting(List<WorkGroup> srcList) {
        /*1.不能为空*/
        for (WorkGroup workGroup :
                srcList) {
            if (Strings.isNullOrEmpty(workGroup.getName()) || Strings.isNullOrEmpty(workGroup.getBasedate())) {
                LogUtils.d("为空检查结果是:" + MSG_BLANK_IS_EMPTY);
                return MSG_BLANK_IS_EMPTY;
            }
        }

        /*2.重复项检测--班组名称*/
        if (hasRepeatValue(srcList)) {
            LogUtils.d("重复检查结果是:" + MSG_BLANK_IS_REPEAT);
            return MSG_BLANK_IS_REPEAT;
        }

        /*默认勾选检测*/
        boolean hasDefault = false;
        for (WorkGroup workGroup :
                srcList) {
            if (workGroup.getFlag().equals(FLAG_DEFAULT_WORKGROUP)) {
                hasDefault = true;
            }
        }
        if (!hasDefault) {
            return MSG_DUFAULT_IS_UNCHECKED;
        }

        /*无改动*/
        if (!mAdapter.isChanged()) {
            return MSG_NO_CHANGE;
        }
        return MSG_OK;
    }

    /**
     * //todo 知识点:查找list中的重复项
     *
     * @param resData
     * @return
     */
    public boolean hasRepeatValue(List<WorkGroup> resData) {
        LogUtils.d("元数据=" + resData.toString());
        boolean isHas = false;
        /*创建一个HashSet,因为不允许重复值存在*/
        Set<String> repeated = new HashSet<>();
        List<String> results = new ArrayList<>();
        for (WorkGroup data : resData) {
            /*如果add时,值已经存在,那么返回false*/
            if (!repeated.add(data.getName())) {
                results.add(data.getName());
                isHas = true;
                LogUtils.d("重复的班组是:" + data.getName() + "  " + data.getBasedate());
            }

        }
        System.out.println(results);
        LogUtils.d("最后的set=" + results);
        return isHas;

        /*其他方法*/
        /*1.流操作需要api24*/
//        curLists.stream().collect(Collectors.toMap(WorkGroup::getNumber,WorkGroup::getName));


        /*2,使用guava,将list转换成map*/
/*        Map<String, WorkGroup> mapWorkGroup = Maps.uniqueIndex(curLists, new Function<WorkGroup, String>() {
            @NullableDecl
            @Override
            public String apply(@NullableDecl WorkGroup input) {
                return input.getName();
            }
        });*/
    }
}
