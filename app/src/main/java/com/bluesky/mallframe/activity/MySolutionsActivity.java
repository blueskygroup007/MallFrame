package com.bluesky.mallframe.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;

import java.util.ArrayList;
import java.util.List;


public class MySolutionsActivity extends BaseActivity {
    private ListView mLvSulotions;
    private SolutionRemoteDataSource mSource;
    private List<TurnSolution> mSolutions;
    private SolutionAdapter mAdapter;


    class SolutionAdapter extends BaseAdapter {
        private List<TurnSolution> mData = new ArrayList<>();

        public void setData(List<TurnSolution> data) {
            mData = data;
            notifyDataSetChanged();
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
            TextView tvSolution = new TextView(MySolutionsActivity.this);
            TurnSolution solution = mData.get(position);
            tvSolution.setText(solution.getName());
            return tvSolution;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {
        mAdapter = new SolutionAdapter();
        mLvSulotions.setAdapter(mAdapter);
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
        mLvSulotions = findViewById(R.id.lv_solutions);
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
