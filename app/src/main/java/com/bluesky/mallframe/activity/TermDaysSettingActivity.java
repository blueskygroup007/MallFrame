package com.bluesky.mallframe.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.WorkDayKind;
import com.bluesky.mallframe.data.WorkGroup;
import com.bluesky.mallframe.ui.BSNumberPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TermDaysSettingActivity extends BaseActivity {

    public static final String FLAG_INTENT_DATA = "DATA_TERM_DAY";

    //控件
    private BSNumberPicker mNumberPicker;
    private RecyclerView mRecyclerView;
    private TextView mTvMsg;

    //数据
    private TurnSolution mSolution;
    private List<WorkGroup> mWorkgroups;
    private RvTermDaysAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

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
                    chooseTimeDialog(mList,position);
                }
            });
            /*班次名称修改的监听事件*/
            holder.mEtName.addTextChangedListener(watcher);
            holder.mEtName.setTag(watcher);
        }

        private void chooseTimeDialog(List<WorkDayKind> list, int position) {
            String startTime=list.get(position).getStarttime();
            String endTime=list.get(position).getEndtime();

            Calendar calendar=Calendar.getInstance();

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


}
