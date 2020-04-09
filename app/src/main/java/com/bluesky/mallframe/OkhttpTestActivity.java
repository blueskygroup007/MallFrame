package com.bluesky.mallframe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpTestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvInfo;
    private Button mBtnTest1, mBtnTest2, mBtnTest3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_test);
        mTvInfo = findViewById(R.id.tv_info);
        mBtnTest3 = findViewById(R.id.btn_test3);
        mBtnTest2 = findViewById(R.id.btn_test2);
        mBtnTest1 = findViewById(R.id.btn_test1);

        mBtnTest1.setOnClickListener(this);
        mBtnTest2.setOnClickListener(this);
        mBtnTest3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test1:
                doTest1();
                break;
            case R.id.btn_test2:
                doTest2();
                break;
            case R.id.btn_test3:
                doTest3();
                break;
            default:
                break;
        }
    }

    private void doTest3() {

    }

    private void doTest2() {

    }

    private void doTest1() {

        String url = "http://www.baidu.com";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                LogUtil.d("网络访问失败！");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                //response.body().string()只能调用一次
                final String resp = response.body().string();
                LogUtil.d("网络访问成功：" + resp);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvInfo.setText(resp);
                    }
                });
            }
        });
    }
}
