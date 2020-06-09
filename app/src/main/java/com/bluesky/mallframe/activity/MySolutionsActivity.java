package com.bluesky.mallframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;
import com.google.common.base.Strings;
import com.google.common.primitives.Booleans;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MySolutionsActivity extends BaseActivity {
    private SolutionRemoteDataSource mSource;
    private List<TurnSolution> mSolutions;
    private SolutionAdapter mAdapter;
    private RecyclerView mRvSolution;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.ViewHolder> {
        private List<TurnSolution> mListData = new ArrayList<>();
        private OnItemClickListener mListener = null;

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public void setData(List<TurnSolution> listData) {
            mListData = listData;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SolutionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MySolutionsActivity.this).inflate(R.layout.item_solution, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            holder.mCbDefault.setOnCheckedChangeListener(null);

        }

        @Override
        public void onBindViewHolder(@NonNull SolutionAdapter.ViewHolder holder, int position) {
            TurnSolution solution = mListData.get(position);
            holder.mTvNumber.setText(String.format(Locale.CHINA, "%d#", position + 1));
            holder.mTvName.setText(solution.getName());
            holder.mTvInfo.setText(String.format(Locale.CHINA, "天数:%d  班组:%d  %s", solution.getWorkdays().size(), solution.getWorkgroups().size(), solution.getDefaultWorkGroup()));
            String company = Strings.isNullOrEmpty(solution.getCompany()) ? "无" : solution.getCompany();
            holder.mTvCompany.setText(String.format(Locale.CHINA, "公司:", company));

            holder.mCbDefault.setChecked(solution.getActive());
            holder.mCbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mListData.get(position).setActive(isChecked);
                }
            });
            if (mListener != null) {
                holder.mCvRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(v, position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTvNumber;
            TextView mTvName;
            TextView mTvInfo;
            TextView mTvCompany;
            CheckBox mCbDefault;
            CardView mCvRoot;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvNumber = itemView.findViewById(R.id.tv_number);
                mTvName = itemView.findViewById(R.id.tv_name);
                mTvInfo = itemView.findViewById(R.id.tv_info);
                mTvCompany = itemView.findViewById(R.id.tv_company);
                mCbDefault = itemView.findViewById(R.id.cb_default);
                mCvRoot = itemView.findViewById(R.id.card_root);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRvSolution.setLayoutManager(layoutManager);
        mAdapter = new SolutionAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(EditActivity.DATA_SOLUTION, mSolutions.get(position));
                intent.setClass(MySolutionsActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
        mRvSolution.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        mSource = new SolutionRemoteDataSource();
        mSource.loadSolutions(new SolutionDataSource.LoadSolutionsCallback() {
            @Override
            public void onSolutionsLoaded(List<TurnSolution> solutions) {
                LogUtils.d("获取的倒班表=" + solutions.toString());
                mSolutions = solutions;
                mAdapter.setData(mSolutions);
            }

            @Override
            public void onDataNotAvailable() {
                Toast.makeText(MySolutionsActivity.this, "没有可用的倒班表", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //Toolbar的标题不能居中,使用Toolbar布局中的自定义TextView能实现
        toolbar.setTitle("我的倒班");
        setSupportActionBar(toolbar);
        //显示返回按钮
        if (getSupportActionBar() != null) {
            LogUtils.d("toolbar found!");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

        } else {
            LogUtils.d("toolbar not found!");
        }

        //查找控件
        mRvSolution = findViewById(R.id.rv_solutions);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_my_solutions;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
