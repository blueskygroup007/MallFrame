package com.bluesky.mallframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.fragment.app.FragmentTransaction;

import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.base.BaseFragment;
import com.bluesky.mallframe.data.User;
import com.bluesky.mallframe.fragment.CalendarFragment;
import com.bluesky.mallframe.fragment.HomeFragment;
import com.bluesky.mallframe.fragment.PersonalFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    public static final String PARAM = "USER";
    private RadioGroup mRgMain;

    private List<BaseFragment> mFragments;
    private BaseFragment currentFragment;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initEvent() {

        initFragment();

        mRgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        position = 0;
                        break;
                    case R.id.rb_calendar:
                        position = 1;
                        break;
                    case R.id.rb_personal:
                        position = 2;
                        break;
                    default:
                        break;
                }
                BaseFragment nextFragment = mFragments.get(position);
                switchFragment(currentFragment, nextFragment);
            }
        });
        //设置默认首页(会引发RadioGroup的Change事件)
        mRgMain.check(R.id.rb_home);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra(PARAM);
        if (user != null) {
//            mTvInfo.setText(user.toString());
        }
    }

    @Override
    protected void initView() {
/*        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //显示返回按钮
        if (getSupportActionBar() != null) {
            LogUtils.d("toolbar found!");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("倒班本首页");
        } else {
            LogUtils.d("toolbar not found!");
        }*/
        mRgMain = findViewById(R.id.rg_main);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new CalendarFragment());
        mFragments.add(new PersonalFragment());

    }

    /**
     * 从集合中取fragment
     */
    private BaseFragment getFragment(int position) {
        if (mFragments != null && mFragments.size() > 0) {
            return mFragments.get(position);
        }

        return null;
    }

    private void switchFragment(BaseFragment fromFragment, BaseFragment nextFragment) {
        if (currentFragment != nextFragment) {
            currentFragment = nextFragment;
            //目标fragment不能为空
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //目标fragment没有被add过的情况:
                if (!nextFragment.isAdded()) {
                    //源fragment不为空则隐藏
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    //添加目标fragment并提交
                    transaction.add(R.id.main_frame, nextFragment).commit();
                } else {
                    //目标fragment已经被add过的情况:
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(nextFragment).commit();
                }
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        BmobUser.logOut();
    }
}
