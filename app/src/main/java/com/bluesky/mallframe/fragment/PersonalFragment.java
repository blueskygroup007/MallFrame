package com.bluesky.mallframe.fragment;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bluesky.mallframe.R;
import com.bluesky.mallframe.activity.MySolutionsActivity;
import com.bluesky.mallframe.activity.UpLoadActivity;
import com.bluesky.mallframe.base.BaseFragment;
import com.bluesky.mallframe.data.User;
import com.haibin.calendarview.Calendar;

import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * @author BlueSky
 * @date 2020/4/20
 * Description:
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mIvPhoto, mIvSex;
    private TextView mTvUsername, mTvDesc;
    private Button mBtnMySolutions;
    private Button mBtnUpLoad;

    @Override
    protected void initEvent() {
        mBtnMySolutions.setOnClickListener(this);
        mBtnUpLoad.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        User currentUser = BmobUser.getCurrentUser(User.class);
        mIvSex.setImageResource(currentUser.getGender() ? R.drawable.ic_male : R.drawable.ic_female);
        mIvPhoto.setImageResource(R.drawable.ic_owl);
        mTvUsername.setText(currentUser.getMobilePhoneNumber());
        mTvDesc.setText(currentUser.getDesc());
    }

    @Override
    protected void initView(View view) {
        //设置toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("我的");

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

        mIvPhoto = view.findViewById(R.id.iv_personal_photo);
        mIvSex = view.findViewById(R.id.iv_personal_sex);
        mTvUsername = view.findViewById(R.id.tv_fragment_personal_username);
        mTvDesc = view.findViewById(R.id.tv_personal_desc);

        mBtnMySolutions = view.findViewById(R.id.btn_personal_my_solution);
        mBtnUpLoad = view.findViewById(R.id.btn_personal_upload);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void setData(Map<String, Calendar> map) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_personal_my_solution:
                intent.setClass(mContext, MySolutionsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_personal_upload:
                intent.setClass(mContext, UpLoadActivity.class);
                startActivity(intent);
                break;
            default:

        }
    }
}
