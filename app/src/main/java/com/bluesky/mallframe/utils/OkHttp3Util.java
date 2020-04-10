package com.bluesky.mallframe.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author BlueSky
 * @date 2020/4/9
 * Description:照抄网上封装代码以理解思路
 */
public class OkHttp3Util {
    private static int TimeOut = 120;
    //单例获取okhttp3对象
    private static OkHttpClient okHttp = null;

    public static synchronized OkHttpClient getInstance() {
        if (okHttp == null) {
            okHttp = new OkHttpClient.Builder()
                    .readTimeout(TimeOut, TimeUnit.SECONDS)
                    .connectTimeout(TimeOut, TimeUnit.SECONDS)
                    .writeTimeout(TimeOut, TimeUnit.SECONDS)
                    .build();
        }
        return okHttp;
    }

    static class MyCallBack implements Callback {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {

        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

        }
    }

    public static void doPost(String url, Map<String, Object> map, Callback callback) {
        FormBody.Builder formBody = new FormBody.Builder();
        //解析转化map
        if (null != map && map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()
            ) {
                formBody.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody requestBody = formBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        getInstance().newCall(request).enqueue(callback);
    }

    public static void doGet(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        getInstance().newCall(request).enqueue(callback);
    }
}
