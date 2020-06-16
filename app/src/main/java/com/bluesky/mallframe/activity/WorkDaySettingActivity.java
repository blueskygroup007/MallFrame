package com.bluesky.mallframe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
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
import com.bluesky.mallframe.ui.BSNumberPicker;

import java.util.List;

public class WorkDaySettingActivity extends BaseActivity {

    public static final String FLAG_INTENT_DATA = "DATA_WORK_DAY";
    private RecyclerView mRvWorkDays;
    private TurnSolution mSolution;
    private List<WorkDay> mWorkDays;
    private WorkDayAdapter mAdapter;
    private BSNumberPicker mPicker;

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
                        //todo 思考：是应该最少剩一个条目，还是允许删光。
                        WorkDay workDay = mWorkDays.get(size - 1).clone();
                        mWorkDays.add(workDay);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNumberDec(int number) {
                int size = mWorkDays.size();
                if (number > 0) {
                    mPicker.setNumber(--number);
                    if (number < size) {
                        mWorkDays.remove(size - 1);
                        mAdapter.notifyDataSetChanged();
                    }
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
        private WorkDayKindAdapter mWorkDayKindAdapter;


        public WorkDayAdapter(List<WorkDay> list, List<WorkDayKind> kinds, Context context) {

            this.mData = list;
            this.mContext = context;
            this.mKinds = kinds;
            mWorkDayKindAdapter = new WorkDayKindAdapter(mKinds, context);

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

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTvNumber;
            GridView mGvWorkdays;
            RadioGroup mRgWorkDays;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvNumber = itemView.findViewById(R.id.tv_item_work_day_number);
//                mGvWorkdays = itemView.findViewById(R.id.gv_item_work_day_kinds);
                mRgWorkDays = itemView.findViewById(R.id.rg_item_work_day_kinds);
            }
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            holder.mRgWorkDays.setOnCheckedChangeListener(null);
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
            holder.mTvNumber.setText(String.format("第%s天", mData.get(position).getNumber()));

            holder.mRgWorkDays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    group.getCheckedRadioButtonId();
                    for (int i = 0; i < group.getChildCount() - 1; i++) {
                        if (group.getChildAt(i).getId() == group.getCheckedRadioButtonId()) {
                            Toast.makeText(mContext, "点击的是：" + mKinds.get(i), Toast.LENGTH_SHORT).show();

                        }
                    }
                    mData.get(position).setWorkdaykind(mKinds.get(position).clone());
                }
            });
/*            mWorkDayKindAdapter.setOnSelectListener(new OnSelectListener() {
                @Override
                public void onSelected(int pos) {
                    mData.get(position).setWorkdaykind(mKinds.get(pos).clone());
                }
            });
            holder.mGvWorkdays.setAdapter(mWorkDayKindAdapter);*/
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }


        class WorkDayKindAdapter extends BaseAdapter {
            private List<WorkDayKind> mDataKinds;
            private Context mContext;
            private OnSelectListener mListener;

            WorkDayKindAdapter(List<WorkDayKind> data, Context context) {
                this.mDataKinds = data;
                this.mContext = context;
            }

            public void setOnSelectListener(OnSelectListener listener) {
                mListener = listener;
            }

            @Override
            public int getCount() {
                return mDataKinds.size();
            }

            @Override
            public Object getItem(int position) {
                return mDataKinds.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                RadioButton item = new RadioButton(mContext);
                item.setText(mDataKinds.get(position).getName());
                item.setBackgroundResource(R.drawable.bg_button_round_small_selector);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            item.setSelected(!item.isSelected());
                            mListener.onSelected(position);
                        }
                    }
                });
                return item;
            }
        }
    }

    public interface OnSelectListener {
        void onSelected(int position);
    }
}
