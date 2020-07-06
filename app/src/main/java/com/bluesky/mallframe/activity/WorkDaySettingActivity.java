package com.bluesky.mallframe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.WorkDay;
import com.bluesky.mallframe.data.WorkDayKind;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.bluesky.mallframe.ui.BSNumberPicker;

import java.util.ArrayList;
import java.util.List;

public class WorkDaySettingActivity extends BaseActivity {

    public static final String FLAG_INTENT_DATA = "DATA_WORK_DAY";
    public static final int REQUESTCODE = 3;

    private RecyclerView mRvWorkDays;
    private TurnSolution mSolution;
    private List<WorkDay> mWorkDays;
    private WorkDayAdapter mAdapter;
    private BSNumberPicker mPicker;

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
        //todo 不需要检查adapter的各种情况:重复,空缺等,只需要是否保存弹窗
        if (mAdapter.isChanged()) {
            showSaveDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SolutionDataSource mRemote = new SolutionRemoteDataSource();
                    mSolution.setWorkdays(mWorkDays);
                    mRemote.updateSolution(mSolution);
                    dialog.dismiss();
                    Intent data = new Intent();
                    data.putExtra(FLAG_INTENT_DATA, mSolution);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        } else {
            // todo 为什么父类方法不能finish?
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {
        mPicker.setOnNumberChangeListener(new BSNumberPicker.OnNumberChangeListener() {
            @Override
            public void onNumberInc(int number) {
                /*最多100条，最少0条*/
                int size = mWorkDays.size();
                if (number < 100) {
                    mPicker.setNumber(++number);
                    if (number > size) {
                        //todo 错误：当number=0的时候，增加一个，此时，并没有数据可以克隆。
                        WorkDay workDay = mWorkDays.get(size - 1).clone();
                        workDay.setNumber(number);
                        mWorkDays.add(workDay);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNumberDec(int number) {
                int size = mWorkDays.size();
                if (number > 2) {
                    mPicker.setNumber(--number);
                    if (number < size) {
                        mWorkDays.remove(size - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(WorkDaySettingActivity.this, getString(R.string.toast_work_day_setting_minimum), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mSolution = (TurnSolution) intent.getSerializableExtra(FLAG_INTENT_DATA);
        mWorkDays = mSolution.getWorkdays();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRvWorkDays.setLayoutManager(manager);
        mAdapter = new WorkDayAdapter(mWorkDays, mSolution.getWorkdaykinds(), this);
        mRvWorkDays.setAdapter(mAdapter);

        mPicker.setNumber(mWorkDays.size());
        LogUtils.d("倒班周期=" + mWorkDays.toString());
    }

    @Override
    protected void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //显示返回按钮
        if (getSupportActionBar() != null) {
            LogUtils.d("toolbar found!");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("编辑倒班周期");
        } else {
            LogUtils.d("toolbar not found!");
        }
        mRvWorkDays = findViewById(R.id.rv_work_days);
        mPicker = findViewById(R.id.np_work_day_count);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_work_day_setting;
    }


    class WorkDayAdapter extends RecyclerView.Adapter<WorkDayAdapter.ViewHolder> {

        private final Context mContext;
        private final List<WorkDayKind> mKinds;
        private List<WorkDay> mData;
        private List<WorkDay> mBackup = new ArrayList<>();

        public boolean isChanged() {
            LogUtils.d("周期比较=" + mData + "----" + mBackup);
            return !isListEqual(mData, mBackup);
        }

        public List<WorkDay> getBackup() {
            return mBackup;
        }

        public WorkDayAdapter(List<WorkDay> list, List<WorkDayKind> kinds, Context context) {
            this.mData = list;
            this.mContext = context;
            this.mKinds = kinds;
            for (WorkDay workDay :
                    list) {
                mBackup.add(workDay.clone());
            }
        }

        public List<WorkDay> getData() {
            return mData;
        }

        public RadioButton addButton(String name) {
            RadioButton radioButton = new RadioButton(mContext);
            //todo 知识点:动态添加时,使用weight的方法.在LayoutParams构造方法的第三个参数置1f
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            layoutParams.setMargins(10, 10, 10, 10);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setText(name);
            radioButton.setTextSize(12);
            radioButton.setButtonDrawable(android.R.color.transparent);//隐藏单选圆形按钮
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setPadding(10, 10, 10, 10);
//            radioButton.setTextColor(getResources().getColorStateList(R.color.blue));//设置选中/未选中的文字颜色
            radioButton.setBackgroundResource(R.drawable.bg_button_round_small_selector);//设置按钮选中/未选中的背景
            return radioButton;
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            holder.mRgWorkDays.setOnCheckedChangeListener(null);
            //清除所有Radiobutton的选中状态
            for (int i = 0; i < mKinds.size(); i++) {
                View childAt = holder.mRgWorkDays.getChildAt(i);
                if (childAt instanceof RadioButton) {
                    ((RadioButton) childAt).setChecked(false);
                }
            }
        }

        @NonNull
        @Override
        public WorkDayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_work_day_selector, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            //todo 知识点:动态给ViewHolder添加控件,应该放在onCreateViewHolder中.而不是onBindViewHolder
            for (WorkDayKind kind : mKinds) {
                viewHolder.mRgWorkDays.addView(addButton(kind.getName()));//将单选按钮添加到RadioGroup中
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            //todo 问题:这里的序号采用的是WorkDay对象的number.其他地方用的是position
            //numberPicker增加一项时,clone了当前项,并设置了新number
            //两种方法取一,倾向于使用number成员变量
            holder.mTvNumber.setText(String.format("第%s天", mData.get(position).getNumber()));
            holder.mRgWorkDays.clearCheck();
            //给radiobutton设置选中状态,遍历,对比name是否相等
            for (int i = 0; i < mKinds.size(); i++) {
                View childAt = holder.mRgWorkDays.getChildAt(i);
                if (childAt instanceof RadioButton) {
                    if (mData.get(position).getWorkdaykindnumber() == i) {
                        ((RadioButton) childAt).setChecked(true);
                    } else {
                        ((RadioButton) childAt).setChecked(false);

                    }
                }
            }
            holder.mRgWorkDays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    group.getCheckedRadioButtonId();
                    for (int i = 0; i < group.getChildCount(); i++) {
                        if (group.getChildAt(i).getId() == group.getCheckedRadioButtonId()) {
                            Toast.makeText(mContext, "点击的是：" + mKinds.get(i), Toast.LENGTH_SHORT).show();
                            mData.get(position).setWorkdaykindnumber(i);
                            LogUtils.d("isChange=" + isChanged());
                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTvNumber;
            RadioGroup mRgWorkDays;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvNumber = itemView.findViewById(R.id.tv_item_work_day_number);
                mRgWorkDays = itemView.findViewById(R.id.rg_item_work_day_kinds);
            }
        }
    }
}
