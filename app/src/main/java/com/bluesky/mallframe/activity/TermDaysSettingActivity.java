package com.bluesky.mallframe.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.bluesky.mallframe.data.WorkDayKind;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.bluesky.mallframe.ui.BSNumberPicker;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.app.AlertDialog.THEME_TRADITIONAL;

public class TermDaysSettingActivity extends BaseActivity {

    public static final String FLAG_INTENT_DATA = "DATA_TERM_DAY";

    //控件
    private BSNumberPicker mNumberPicker;
    private RecyclerView mRecyclerView;
    private TextView mTvMsg;

    //数据
    private TurnSolution mSolution;
    private List<WorkDayKind> mWorkDayKinds;
    private RvTermDaysAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {
        mNumberPicker.setOnNumberChangeListener(new BSNumberPicker.OnNumberChangeListener() {
            @Override
            public void onNumberInc(int number) {
                if (number > mWorkDayKinds.size()) {
                    WorkDayKind workDayKind = new WorkDayKind();
                    mAdapter.getList().add(workDayKind);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNumberDec(int number) {
                if (number < mWorkDayKinds.size()) {
                    mAdapter.getList().remove(mAdapter.getList().size() - 1);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mSolution = (TurnSolution) intent.getSerializableExtra(FLAG_INTENT_DATA);
        mWorkDayKinds = mSolution.getWorkdaykinds();
        LogUtils.d(mWorkDayKinds);

        mNumberPicker.setNumber(mWorkDayKinds.size());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RvTermDaysAdapter(mWorkDayKinds);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("编辑班次");
        } else {
            LogUtils.e("toolbar没有找到");
        }

        mNumberPicker = findViewById(R.id.np_term_days_count);
        mRecyclerView = findViewById(R.id.rv_term_days_list);
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

    public static final String MSG_BLANK_IS_EMPTY = "存在空项!";
    public static final String MSG_BLANK_IS_REPEAT = "项目重复!";
    public static final String MSG_DUFAULT_IS_UNCHECKED = "默认未设置!";
    public static final String MSG_NO_CHANGE = "无改动";
    public static final String MSG_OK = "修改正确!";

    @Override
    public void onBackPressed() {
        if (mAdapter.isEmpty()) {
            mTvMsg.setText("提示:" + MSG_BLANK_IS_EMPTY);
        }
        if (mAdapter.isRepeat()) {
            mTvMsg.setText("提示:" + MSG_BLANK_IS_REPEAT);
        }
        /*没有改动,直接退出*/
        if (!mAdapter.isChanged()) {
            mTvMsg.setText("提示:" + MSG_NO_CHANGE);
            finish();
        } else {
            /*修改正确,弹出是否保存对话框*/
            mTvMsg.setText("提示:" + MSG_OK);
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
                        mSolution.setWorkdaykinds(mWorkDayKinds);
                        mRemote.updateSolution(mSolution);
                        dialog.dismiss();
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
        return R.layout.activity_term_days_setting;

    }

    private class RvTermDaysAdapter extends RecyclerView.Adapter<RvTermDaysAdapter.ViewHolder> {
        private List<WorkDayKind> mBackupList = new ArrayList<>();
        private List<WorkDayKind> mList;

        public RvTermDaysAdapter(List<WorkDayKind> list) {
            mBackupList.addAll(list);
            mList = list;
        }

        public List<WorkDayKind> getList() {
            return mList;
        }

        /**
         * 是否有修改
         *
         * @return
         */
        public boolean isChanged() {
            return CollectionUtils.isEqualCollection(mList, mBackupList);
        }


        public boolean isEmpty() {
            for (WorkDayKind workDayKind : mList) {
                if (Strings.isNullOrEmpty(workDayKind.getName())
                        || Strings.isNullOrEmpty(workDayKind.getStarttime())
                        || Strings.isNullOrEmpty(workDayKind.getEndtime())) {
                    return true;
                }
            }
            return false;
        }

        public boolean isRepeat() {
            Set<String> repeated = new HashSet<>();
            for (WorkDayKind workDayKind : mList
            ) {
                if (!repeated.add(workDayKind.getName())) {
                    return true;
                }
            }
            return false;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_days_item_card_style_layout, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            holder.mEtName.removeTextChangedListener((TextWatcher) holder.mEtName.getTag());

        }

        @Override
        public void onBindViewHolder(@NonNull RvTermDaysAdapter.ViewHolder holder, int position) {
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mList.get(position).setName(s.toString());
                }
            };

            WorkDayKind workDayKind = mList.get(position);
            holder.mTvNumber.setText(String.valueOf(position + 1));
            holder.mEtName.setText(workDayKind.getName());
            holder.mTvSetupTime.setText(String.format("%s至%s", workDayKind.getStarttime(), workDayKind.getEndtime()));
            /*设置上下班日期的点击事件*/
            holder.mTvSetupTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseTimeDialog2(mList, position);
                }
            });
            /*班次名称修改的监听事件*/
            holder.mEtName.addTextChangedListener(watcher);
            holder.mEtName.setTag(watcher);
        }

        private void chooseTimeDialog(List<WorkDayKind> list, int position) {
            String startTime = list.get(position).getStarttime();
            String endTime = list.get(position).getEndtime();
            Calendar calendar = Calendar.getInstance();
            CostumTimePickerDialog dialog = new CostumTimePickerDialog(
                    TermDaysSettingActivity.this,
                    THEME_TRADITIONAL, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Toast.makeText(TermDaysSettingActivity.this, String.format("%s点%s分", hourOfDay, minute), Toast.LENGTH_SHORT).show();
                }
            }, 0, 0, true);

            dialog.show();
        }

        private void chooseTimeDialog2(List<WorkDayKind> list, int position) {
            String startTime = list.get(position).getStarttime();
            String endTime = list.get(position).getEndtime();
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog.Builder builder = new TimePickerDialog.Builder(TermDaysSettingActivity.this, THEME_TRADITIONAL);
            BSNumberPicker numberPicker = new BSNumberPicker(TermDaysSettingActivity.this);
            builder.setView(numberPicker);
            android.app.AlertDialog dialog = builder.create();
            //Todo 如何创建一个时间选择器,并带有一个时长调节控件
            builder.show();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTvNumber;
            EditText mEtName;
            TextView mTvSetupTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvNumber = itemView.findViewById(R.id.tv_number);
                mEtName = itemView.findViewById(R.id.et_name);
                mTvSetupTime = itemView.findViewById(R.id.tv_setup_time);
            }
        }
    }

    class CostumTimePickerDialog extends TimePickerDialog {

        public CostumTimePickerDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
            super(context, themeResId, listener, hourOfDay, minute, is24HourView);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            BSNumberPicker numberPicker = new BSNumberPicker(TermDaysSettingActivity.this);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            params.gravity = Gravity.BOTTOM;
            params.topMargin = -100;
            //获取按钮位置
            Button button = getButton(DialogInterface.BUTTON_POSITIVE);
            ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
//            getWindow().getDecorView().getLayoutParams();
            addContentView(numberPicker, params);
        }


    }
}
