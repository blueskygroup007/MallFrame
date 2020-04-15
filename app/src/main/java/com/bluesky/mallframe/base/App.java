package com.bluesky.mallframe.base;

import android.app.Application;
import android.content.Context;


import cn.bmob.v3.Bmob;

/**
 * @author BlueSky
 * @date 2020/3/30
 * Description:Application类,获取公共context
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        //初始化xutils
/*        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);*/

        //初始化Bmob
        Bmob.initialize(this, AppConstant.BMOB_APP_ID);
    }

    public static Context getContext() {
        return mContext;
    }
}
