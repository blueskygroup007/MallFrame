package com.bluesky.mallframe.base;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.LogUtils;
import com.bluesky.mallframe.R;
import com.heweather.plugin.view.HeWeatherConfig;

import cn.bmob.v3.Bmob;

import static com.bluesky.mallframe.base.AppConstant.HE_WEATHER_ID;

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

        //初始化LogUtils
        //全局TAG
        LogUtils.getConfig().setGlobalTag(getString(R.string.tag_global));

        //初始化Bmob
        Bmob.initialize(this, AppConstant.BMOB_APP_ID);
        //初始化天气插件
/**
 * 初始化
 *
 * @param key   用户的ID
 * @param location  地址详解，若不传或为空则调用Android源生定位
 */
        HeWeatherConfig.init(HE_WEATHER_ID, "唐山");
    }

    public static Context getContext() {
        return mContext;
    }

    static class DialogResource {
        int icon;
        String title;
        String message;
        String positiveText;
        String negativeText;

        public DialogResource(int icon, String title, String message, String positiveText, String negativeText) {
            this.icon = icon;
            this.title = title;
            this.message = message;
            this.positiveText = positiveText;
            this.negativeText = negativeText;
        }
    }

    public static void showDialog(Context context, DialogResource res, DialogInterface.OnClickListener onSave, DialogInterface.OnClickListener onCancel) {

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(res.icon);
        normalDialog.setTitle(res.title);
        normalDialog.setMessage(res.message);

        normalDialog.setPositiveButton(res.positiveText,
                onSave);
        if (onCancel != null) {
            normalDialog.setNegativeButton(res.negativeText,
                    onCancel);
        } else {
            normalDialog.setNegativeButton(res.negativeText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        // 显示
        normalDialog.show();
    }
}
