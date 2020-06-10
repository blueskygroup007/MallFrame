package com.bluesky.mallframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.TurnSolution;
import com.bluesky.mallframe.ui.BSNumberPicker;

import java.io.Serializable;

public class EditActivity extends BaseActivity {
    public static final String DATA_SOLUTION = "DATA_SOLUTION";
    private Toolbar toolbar;

    private BSNumberPicker mNumberPicker;
    private TurnSolution mSolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mSolution = (TurnSolution) intent.getSerializableExtra(DATA_SOLUTION);
    }

    @Override
    protected void initView() {
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //显示返回按钮
        if (getSupportActionBar() != null) {
            LogUtils.d("toolbar found!");
            //显示左上角返回图标
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

            //Toolbar的标题不能居中,使用Toolbar布局中的自定义TextView能实现
            getSupportActionBar().setTitle("编辑倒班");
            //显示应用程序图标
//            getSupportActionBar().setIcon(R.drawable.ic_launcher_foreground);
        } else {
            LogUtils.d("toolbar not found!");
        }

        mNumberPicker = findViewById(R.id.np_group_count);

    }

    @Override
    protected int initLayout() {
        return R.layout.activity_edit;
    }

    /**
     * 生成toolbar右上角的自定义按钮
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_edit, menu);
        return true;
    }

    /**
     * toolbar自定义按钮的点击事件处理
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_item_action_toolbar_save:
                //todo 保存倒班信息
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
