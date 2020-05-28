package com.bluesky.mallframe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;


public class SplashActivity2 extends AppCompatActivity {

    private static final String TAG = SplashActivity2.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        Log.d(TAG, "SDK版本=" + android.os.Build.VERSION.SDK_INT);
        LogUtils.d("SDK版本=" + android.os.Build.VERSION.SDK_INT);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            /*AnimatedVectorDrawable anim = (AnimatedVectorDrawable) getResource().getDrawable(R.drawable.anim);
             *将getResource去掉，以消除警告
             *has unresolved theme attributes! Consider using Resources.getDrawable(int, Theme) or Context.getDrawable(int)
             */
            AnimatedVectorDrawable anim = (AnimatedVectorDrawable) getDrawable(R.drawable.anim);
            ImageView img = findViewById(R.id.iv_logo);
            img.setImageDrawable(anim);
            anim.start();
        } else {
            Log.d(TAG, "SDK版本不足以显示svg动画。。。");
        }
    }

    public void onLogin(View view) {
        //进入登录页面
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * 三球测试
     *
     * @param view
     */
    public void onTestThreeBall(View view) {

        /*todo 知识点:矢量动画可以做得比较复杂,例如模拟三球围绕旋转,但也许会有问题.*/
        /*AnimatedVectorDrawable anim = (AnimatedVectorDrawable) getResource().getDrawable(R.drawable.anim);
         *将getResource去掉，以消除警告
         *has unresolved theme attributes! Consider using Resources.getDrawable(int, Theme) or Context.getDrawable(int)
         */

        /*AnimatedVectorDrawable anim = (AnimatedVectorDrawable) getDrawable(R.drawable.anim_three_ball);
        ImageView img = findViewById(R.id.iv_three_ball);
        img.setImageDrawable(anim);
        anim.start();*/
    }

    /**
     * 小猫测试
     *
     * @param view
     */
    public void onTestCat(View view) {
        ImageView img = findViewById(R.id.iv_logo);
        Animatable animatable = (Animatable) img.getDrawable();
        animatable.start();
    }
}
