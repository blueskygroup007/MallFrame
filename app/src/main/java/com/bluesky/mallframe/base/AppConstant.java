package com.bluesky.mallframe.base;

import com.bluesky.mallframe.data.WorkDayKind;

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
    public static final SimpleDateFormat FORMAT_NO_SECS_DATE = new SimpleDateFormat("yyyy-MM-dd - HH:mm", Locale.getDefault());

    public static final SimpleDateFormat FORMAT_ONLY_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat FORMAT_ONLY_TIME = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat FORMAT_ONLY_TIME_NO_SECS = new SimpleDateFormat("HH:mm", Locale.getDefault());

    //休班常量
    public static final WorkDayKind REST_DAY = new WorkDayKind();

    static {
        REST_DAY.setName("休班");
        REST_DAY.setStarttime("0:00");
        REST_DAY.setEndtime("24:00");

    }

}
