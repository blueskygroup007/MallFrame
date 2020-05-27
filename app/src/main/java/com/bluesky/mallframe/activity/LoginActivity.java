package com.bluesky.mallframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.bluesky.mallframe.base.BaseActivity;
import com.bluesky.mallframe.data.User;
import com.bluesky.mallframe.dialog.CustomDialog;
import com.bluesky.mallframe.utils.PreferenceUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private TextView mEtForget;
    private Button mBtnLogin;
    private Button mBtnRegister;
    private CheckBox mCheckBoxRemember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvent() {
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mEtForget.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mEtUsername = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);
        mEtForget = findViewById(R.id.tv_forget);
        mCheckBoxRemember = findViewById(R.id.cb_remember);

        String sp_username = PreferenceUtils.getString("username", "");
        String sp_password = PreferenceUtils.getString("password", "");
        if (!TextUtils.isEmpty(sp_username) & !TextUtils.isEmpty(sp_password)) {
            mEtUsername.setText(sp_username);
            mEtPassword.setText(sp_password);
            mCheckBoxRemember.setChecked(true);
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_forget:
                Intent intent_pw = new Intent(this, PasswordForgetActivity.class);
                startActivity(intent_pw);
                break;
            default:

        }
    }

    /**
     * 另一种登陆方式(通过username,phone,email三者之一)
     *
     * @param dialog
     * @param username
     * @param pwd
     */
    private void bmobLoginByAccount(CustomDialog dialog, String username, String pwd) {
        BmobUser.loginByAccount(username, pwd, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                dialog.dismiss();
                if (e == null) {
                    //登陆成功后,再做记住密码操作
                    if (mCheckBoxRemember.isChecked()) {
                        //Todo 密码改成MD5加密保存
                        PreferenceUtils.put("username", username);
                        PreferenceUtils.put("password", pwd);
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    //因为后台没有开启邮箱验证,所以,如果启用,会报错9015
                    /*if (user.getEmailVerified()) {
                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "请前往验证邮箱!", Toast.LENGTH_SHORT).show();
                    }*/
                } else {

                    //todo 没有网络的处理
                    //ERROR_CODE=9015ERROR=errorCode:9015,errorMsg:java.net.UnknownHostException: Unable to resolve host "open2.bmob.cn": No address associated with hostname
                    PreferenceUtils.put("username", "");
                    PreferenceUtils.put("password", "");
                    LogUtils.e("username=" + username + "  pwd=" + pwd + "\n" + "ERROR_CODE=" + e.getErrorCode() + "ERROR=" + e.toString());
                    Toast.makeText(LoginActivity.this, "登陆失败!" + "  错误代码是:" + e.toString(), Toast.LENGTH_SHORT).show();
                    LogUtils.e("USER=" + user.getUsername() + "\n" + " ERROR=" + e.getMessage());
                }
            }

        });
    }

    private void login() {
        /*BasePopupView popupView = new XPopup.Builder(this)
                .asLoading("正在加载中")
                .show();*/
        final CustomDialog dialog = new CustomDialog(this, 100, 100,
                R.layout.dialog_loading,
                R.style.dialog_custom,
                Gravity.CENTER);

        dialog.setCancelable(false);
        final String username = mEtUsername.getText().toString().trim();
        final String pwd = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username) | TextUtils.isEmpty(pwd)) {
            return;
        }
        dialog.show();
        //todo bmobLogin()方法有时会报错,但bmobLoginByAccount就不报错
        //bmobLogin(dialog, username, pwd);
        bmobLoginByAccount(dialog, username, pwd);
    }

    private void bmobLogin(CustomDialog dialog, String username, String pwd) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);
        LogUtils.d("username=" + username + "  pwd=" + pwd + "\n");

        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {

                dialog.dismiss();
//                popupView.delayDismiss(300);
                if (e == null) {
                    //登陆成功后,再做记住密码操作
                    if (mCheckBoxRemember.isChecked()) {
                        //Todo 密码改成MD5加密保存
                        PreferenceUtils.put("username", username);
                        PreferenceUtils.put("password", pwd);
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    //因为后台没有开启邮箱验证,所以,如果启用,会报错9015
                    /*if (user.getEmailVerified()) {
                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "请前往验证邮箱!", Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    PreferenceUtils.put("username", "");
                    PreferenceUtils.put("password", "");
                    LogUtils.e("username=" + username + "  pwd=" + pwd + "\n" + "ERROR_CODE=" + e.getErrorCode() + "ERROR=" + e.toString());
                    Toast.makeText(LoginActivity.this, "登陆失败!" + "  错误代码是:" + e.toString(), Toast.LENGTH_SHORT).show();
                    LogUtils.e("USER=" + user.getUsername() + "\n" + " ERROR=" + e.getMessage());
                }
            }
        });
    }
}
