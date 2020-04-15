package com.bluesky.mallframe.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtPhone;
    private EditText mEtVcode;
    private Button mBtnGetvcode;
    private EditText mEtPassword;
    private CheckBox mCbAgreement;
    private TextView mTvUserAgreement;
    private TextView mTvPrivacyAgreement;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        //TODO 返回键没效果
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initEvent() {
        mBtnGetvcode.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mEtPhone = findViewById(R.id.et_reg_phone);
        mEtVcode = findViewById(R.id.et_reg_vcode);
        mBtnGetvcode = findViewById(R.id.btn_reg_get_vcode);
        mEtPassword = findViewById(R.id.et_reg_password);
        mCbAgreement = findViewById(R.id.cb_reg_agreement);
        mTvUserAgreement = findViewById(R.id.tv_reg_user_agreement);
        mTvPrivacyAgreement = findViewById(R.id.tv_reg_privacy_agreement);
        mBtnRegister = findViewById(R.id.btn_reg_register);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reg_get_vcode:

                break;
            case R.id.btn_reg_register:

                break;
            default:
                break;
        }
    }
}
