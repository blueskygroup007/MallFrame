package com.bluesky.mallframe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

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

    static class WorkDayAdapter extends RecyclerView.Adapter<WorkDayAdapter.ViewHolder> {

        private final Context mContext;
        private List<WorkDay> mData;
        private ListAdapter mWorkDayKindAdapter;

        public WorkDayAdapter(List<WorkDay> list, List<WorkDayKind> kinds, Context context) {

            this.mData = list;
            this.mContext = context;
            mWorkDayKindAdapter = new WorkDayKindAdapter(kinds, context);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTvNumber;
            GridView mGvWorkdays;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvNumber = itemView.findViewById(R.id.tv_item_work_day_number);
                mGvWorkdays = itemView.findViewById(R.id.gv_item_work_day_kinds);
            }
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
        }

        @NonNull
        @Override
        public WorkDayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_work_day_selector, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mTvNumber.setText(String.format("第%s天", mData.get(position).getNumber()));
            holder.mGvWorkdays.setAdapter(mWorkDayKindAdapter);
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        private static class WorkDayKindAdapter extends BaseAdapter {
            private List<WorkDayKind> mDataKinds;
            private Context mContext;

            WorkDayKindAdapter(List<WorkDayKind> data, Context context) {
                this.mDataKinds = data;
                this.mContext = context;
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
                TextView item = new TextView(mContext);
                item.setText(mDataKinds.get(position).getName());
                return item;
            }
        }
    }

}
