package com.bluesky.mallframe.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.RegexUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.utils.LogUtils;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtPhone;
    private TextView mTvPhoneState;
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
        //不显示图标,只显示标题
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        //设置返回键可用,4.0之前默认为true,之后默认为false
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
        mEtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.getId() == R.id.et_reg_phone && !b) {
                    LogUtils.d("手机号输入框失去焦点了!");
//                    String phone=mEtPhone.getText().toString().trim();
                    if (RegexUtils.isMobileSimple(mEtPhone.getText())) {
                        mTvPhoneState.setTextColor(Color.BLUE);
                        mTvPhoneState.setText(R.string.text_tv_phone_state_correct);
                    } else {
                        mTvPhoneState.setTextColor(Color.RED);
                        mTvPhoneState.setText(R.string.text_tv_phone_state_incorrect);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mEtPhone = findViewById(R.id.et_reg_phone);
        mTvPhoneState = findViewById(R.id.tv_reg_phone_state);
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
                getVcode(mEtPhone.getText().toString().trim());
                break;
            case R.id.btn_reg_register:
                register();
                break;
            default:
                break;
        }
    }

    private CountDownTimer mVcodeTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mBtnGetvcode.setText(new StringBuilder().append(millisUntilFinished / 100).append("s"));
            mBtnGetvcode.setText((millisUntilFinished / 100) + "s");
        }

        @Override
        public void onFinish() {
            mBtnGetvcode.setText(getString(R.string.text_btn_get_vcode));
            mBtnGetvcode.setEnabled(true);
        }
    };

    private void getVcode(String phone) {
        //检查手机号码是否有效
        if (!RegexUtils.isMobileSimple(mEtPhone.getText())) {
            Toast.makeText(this, "请输入有效的手机号码!", Toast.LENGTH_SHORT).show();
            return;
        }

        //发送手机验证码
        //TODO 创建自定义短信通知信息的模板
        BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "发送验证码成功，短信ID：" + smsId, Toast.LENGTH_SHORT).show();
//                    mTvInfo.append("发送验证码成功，短信ID：" + smsId + "\n");
                    //todo 验证码按钮显示倒计时
                    mBtnGetvcode.setEnabled(false);
                } else {
                    Toast.makeText(RegisterActivity.this, "发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    mTvInfo.append("发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                }
            }
        });
    }

    private void register() {

    }
}
