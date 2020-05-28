package com.bluesky.mallframe.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.activity.EditActivity;
import com.bluesky.mallframe.activity.GroupSettingActivity;
import com.bluesky.mallframe.activity.TermDaysSettingActivity;
import com.bluesky.mallframe.activity.Test2Activity;
import com.bluesky.mallframe.activity.TestActivity;
import com.bluesky.mallframe.base.BaseFragment;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BlueSky
 * @date 2020/4/20
 * Description:
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvTitle;
    private Button mBtnEdit, mBtnOne, mBtnTwo, mBtnThree, mBtnFour;
    private SolutionDataSource mRemote;

    @Override
    protected void initEvent() {
        mBtnEdit.setOnClickListener(this);

        mBtnOne.setOnClickListener(this);
        mBtnTwo.setOnClickListener(this);
        mBtnThree.setOnClickListener(this);
        mBtnFour.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTvTitle.setText("这是我的页面fragment!");
        LogUtils.d("我的页面的fragment的数据被初始化了");
    }

    @Override
    protected void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_fragment_personal_title);
        mBtnEdit = view.findViewById(R.id.btn_personal_edit);

        mBtnOne = view.findViewById(R.id.btn_one);
        mBtnTwo = view.findViewById(R.id.btn_two);
        mBtnThree = view.findViewById(R.id.btn_three);
        mBtnFour = view.findViewById(R.id.btn_four);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_personal_edit:
                intent.setClass(mContext, EditActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_one:
                intent.setClass(mContext, TestActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_two:
                intent.setClass(mContext, Test2Activity.class);
                startActivity(intent);

                break;

            case R.id.btn_three:
                //班组设置
                //todo 这里耗时太长了.应该先显示activity,在activity里面去取数据库,回调时,更新list
                mRemote = new SolutionRemoteDataSource();
                mRemote.loadSolutions(new SolutionDataSource.LoadSolutionsCallback() {
                    @Override
                    public void onSolutionsLoaded(List<TurnSolution> solutions) {

                        intent.putExtra(GroupSettingActivity.FLAG_INTENT_DATA, solutions.get(0));
                        intent.setClass(mContext, GroupSettingActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });


                break;
            case R.id.btn_four:
                //周期设置
                mRemote.loadSolutions(new SolutionDataSource.LoadSolutionsCallback() {
                    @Override
                    public void onSolutionsLoaded(List<TurnSolution> solutions) {

                        intent.putExtra(TermDaysSettingActivity.FLAG_INTENT_DATA,solutions.get(0) );
                        intent.setClass(mContext, TermDaysSettingActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
                break;
            default:

        }
    }
}
