package com.bluesky.mallframe;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void dateTest() {
        Long msec = TimeUtils.getTimeSpanByNow(new Date(2020 - 1900, 3, 28, 0, 0, 0), TimeConstants.SEC);
        String startTime = TimeUtils.date2String(new Date(msec), new SimpleDateFormat());
        System.out.println("今日秒数=" + msec + "  当前时间=" + startTime);
        String curDate = new Date().toString();
        System.out.println("当前日期=" + curDate);
        String newDate = new Date(2020 - 1900, 3, 28, 0, 0, 0).toString();
        System.out.println("当前日期222=" + newDate);
        LocalTime time = LocalTime.now();
        System.out.println("当前时间=" + time.toString());

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(date);
        System.out.println("最终时间="+strDate);

    }
}