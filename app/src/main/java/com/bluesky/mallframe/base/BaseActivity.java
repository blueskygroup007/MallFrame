package com.bluesky.mallframe.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.CollectionUtils;
import com.bluesky.mallframe.R;

import java.util.List;

/**
 * @author BlueSky
 * @date 2020/3/30
 * Description:Activity基类,还可以加Toast,Dialog,加载动画,toolbar统一处理
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //设置布局
        setContentView(initLayout());
        //初始化视图控件
        initView();
        //初始化数据
        initData();
        //初始化事件
        initEvent();
    }

    protected abstract void initEvent();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract int initLayout();


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    /**
     * 是否保存的对话框
     */
    protected void showSaveDialog(DialogInterface.OnClickListener onSave, DialogInterface.OnClickListener onCancel) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setIcon(R.drawable.ic_save_black_24dp);
        normalDialog.setTitle("保存");
        normalDialog.setMessage("您修改了设置,是否保存?");

        normalDialog.setPositiveButton("保存",
                onSave);
        normalDialog.setNegativeButton("不保存",
                onCancel);
        // 显示
        normalDialog.show();
    }

    /**
     * 两个集合是否相等,但忽略顺序
     *
     * @param list
     * @param backupList
     * @return
     */
    protected boolean isListEqual(List list, List backupList) {
        //todo 知识点:判断原始list和修改后的list是否不同(即是否当前页面被修改)
        //注意:要重写对象的equals和hashCode方法(类中涉及的所有对象都重写了这俩方法时,自动生成即可)
        return CollectionUtils.isEqualCollection(list, backupList);
    }
}
