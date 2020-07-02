package com.bluesky.mallframe.activity;

import android.content.Intent;
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

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.User;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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

    /**
     * 倒计时计时器
     */
    private CountDownTimer mVcodeTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mBtnGetvcode.setEnabled(false);
            mBtnGetvcode.setText(new StringBuilder().append(millisUntilFinished / 1000).append("s"));
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
        BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "发送验证码成功，短信ID：" + smsId, Toast.LENGTH_SHORT).show();
                    // 验证码按钮显示倒计时
                    mVcodeTimer.start();
                } else {
                    Toast.makeText(RegisterActivity.this, "发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register2() {
        //校验手机号
        if (!RegexUtils.isMobileSimple(mEtPhone.getText())) {
            Toast.makeText(this, "请输入有效的手机号码!", Toast.LENGTH_SHORT).show();
            return;
        }
        //校验密码
        String password = mEtPassword.getText().toString().trim();
        if (!checkPwd(password)) {
            return;
        }
        String phone = mEtPhone.getText().toString().trim();
        //校验验证码
        String code = mEtVcode.getText().toString().trim();
        BmobSMS.verifySmsCode(phone, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "验证码验证成功", Toast.LENGTH_SHORT).show();
//                    mTvInfo.append("验证码验证成功，您可以在此时进行绑定操作！\n");
                    User user = BmobUser.getCurrentUser(User.class);
                    user.setUsername(phone);
                    user.setPassword(password);
                    user.setMobilePhoneNumber(phone);
                    user.setMobilePhoneNumberVerified(true);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "绑定手机号码成功", Toast.LENGTH_SHORT).show();
                            } else {
                                LogUtils.d("绑定手机号码失败" + e.getErrorCode() + "-" + e.getMessage());
                                Toast.makeText(RegisterActivity.this, "绑定手机号码失败" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "验证码验证失败：" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    LogUtils.d("验证码验证失败：" + e.getErrorCode() + "-" + e.getMessage());
                }
            }
        });


        BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "短信注册或登录成功：" + bmobUser.getUsername(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.FLAG_INTENT_DATA, bmobUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "短信注册或登录失败：" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    LogUtils.d("短信注册或登录失败：" + e.getErrorCode() + "-" + e.getMessage());
                }
            }
        });
    }

    private void register() {
        //校验手机号
        if (!RegexUtils.isMobileSimple(mEtPhone.getText())) {
            Toast.makeText(this, "请输入有效的手机号码!", Toast.LENGTH_SHORT).show();
            return;
        }
        //校验密码
        String password = mEtPassword.getText().toString().trim();
        if (!checkPwd(password)) {
            return;
        }
        String phone = mEtPhone.getText().toString().trim();
        //校验验证码
        String code = mEtVcode.getText().toString().trim();
        signOrLogin(phone, code, password);
    }

    /**
     * 一键注册或登录的同时保存其他字段的数据
     *
     * @param phone
     * @param code
     */
    private void signOrLogin(String phone, String code, String password) {
        User user = new User();
        //设置手机号码（必填）
        user.setMobilePhoneNumber(phone);
        //设置用户名，如果没有传用户名，则默认为手机号码
        user.setUsername(phone);
        //设置用户密码
        user.setPassword(password);
        //设置额外信息：此处为年龄
        user.setAge(18);
        user.signOrLogin(code, new SaveListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    LogUtils.d("短信注册或登录成功：" + user.getUsername());
                    Toast.makeText(RegisterActivity.this, "短信注册或登录成功：" + user.getUsername(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.FLAG_INTENT_DATA, user);
                    startActivity(intent);
                    finish();
                } else {
                    //todo 手机号已注册的提示
                    Toast.makeText(RegisterActivity.this, "短信注册或登录失败：" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    LogUtils.d("短信注册或登录失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                }
            }
        });
    }


    /**
     * 密码格式检测
     * 必须是6-20位的字母、数字、下划线（这里字母、数字、下划线是指任意组合，没有必须三类均包含）
     *
     * @param pwd
     * @return
     */
    public static boolean checkPwd(String pwd) {
        String regExp = "^[\\w_]{6,20}$";
        if (pwd.matches(regExp)) {
            return true;
        }
        return false;
    }

}
