package com.bluesky.mallframe.base;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author BlueSky
 * @date 2020/4/13
 * Description:全局常量
 */
public class AppConstant {
    //sharedpreference常量


    //Bugly app id

    //Bmob app id
    public static final String BMOB_APP_ID = "222d9d20cb02f1ce9278b0e584d887dd";


    //时间格式
    public static final SimpleDateFormat FORMAT_FULL_DATE = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat FORMAT_NO_SECS_DATE = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault());

    public static final SimpleDateFormat FORMAT_ONLY_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat FORMAT_ONLY_TIME = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());


}
