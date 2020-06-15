package com.bluesky.mallframe.fragment;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.activity.EditActivity;
import com.bluesky.mallframe.activity.WorkGroupSettingActivity;
import com.bluesky.mallframe.activity.MySolutionsActivity;
import com.bluesky.mallframe.activity.WorkDayKindSettingActivity;
import com.bluesky.mallframe.base.BaseFragment;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.data.source.SolutionDataSource;
import com.bluesky.mallframe.data.source.remote.SolutionRemoteDataSource;

import java.util.List;

/**
 * @author BlueSky
 * @date 2020/4/20
 * Description:
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvTitle;
    private Button mBtnEdit, mBtnOne, mBtnTwo, mBtnThree, mBtnFour;
    private SolutionDataSource mRemote = new SolutionRemoteDataSource();

    private Button mBtnMySolutions;

    @Override
    protected void initEvent() {
        mBtnMySolutions.setOnClickListener(this);
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
        //设置toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        /*todo 知识点:在fragment中使用toolbar
         *  不再需要onCreateOptionsMenu和onOptionsItemSelected
         * 步骤一,告诉fragment,我有菜单
         * */
        setHasOptionsMenu(true);
        /*步骤二,填充菜单*/
        toolbar.inflateMenu(R.menu.menu_fragment_personal);
        /*步骤三,设置监听*/
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.menu.menu_fragment_personal:
                        Toast.makeText(mContext, "你电击了设置按钮!", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        mTvTitle = view.findViewById(R.id.tv_fragment_personal_title);
        mBtnEdit = view.findViewById(R.id.btn_personal_edit);

        mBtnOne = view.findViewById(R.id.btn_one);
        mBtnTwo = view.findViewById(R.id.btn_two);
        mBtnThree = view.findViewById(R.id.btn_three);
        mBtnFour = view.findViewById(R.id.btn_four);

        mBtnMySolutions = view.findViewById(R.id.btn_personal_my_solution);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_personal_my_solution:
                intent.setClass(mContext, MySolutionsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_personal_edit:
                intent.setClass(mContext, EditActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_one:
                intent.setClass(mContext, WorkGroupSettingActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_two:
                intent.setClass(mContext, WorkDayKindSettingActivity.class);
                startActivity(intent);

                break;

            case R.id.btn_three:
                //班组设置
                //todo 这里耗时太长了.应该先显示activity,在activity里面去取数据库,回调时,更新list
                mRemote.loadSolutions(new SolutionDataSource.LoadSolutionsCallback() {
                    @Override
                    public void onSolutionsLoaded(List<TurnSolution> solutions) {

                        intent.putExtra(WorkGroupSettingActivity.FLAG_INTENT_DATA, solutions.get(0));
                        intent.setClass(mContext, WorkGroupSettingActivity.class);
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

                        intent.putExtra(WorkDayKindSettingActivity.FLAG_INTENT_DATA, solutions.get(0));
                        intent.setClass(mContext, WorkDayKindSettingActivity.class);
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
