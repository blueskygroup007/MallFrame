package com.bluesky.mallframe;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.TimeUtils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        System.out.println("最终时间=" + strDate);

    }

    @Test
    public void stringEqualTest() {
        final String FLAG_DEFAULT_WORKGROUP = "default";
        String flag = "default";
        System.out.print(FLAG_DEFAULT_WORKGROUP.equals(flag));
    }

    @Test
    public void listTest() {
        List<String> source = new ArrayList<>();
        source.add("aaa");
        source.add("bbb");
        source.add("ccc");
        List<String> a = new ArrayList<>();
        a.addAll(source);
        List<String> b = source;
        System.out.println(CollectionUtils.isEqualCollection(a, b));
        System.out.println(a + "---" + b);

        b.set(0, "zzz");
        System.out.println(CollectionUtils.isEqualCollection(a, b));
        System.out.println(a + "---" + b);
    }

    @Test
    public void DateTest() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.set(1978, 7, 28);
        endCalendar.set(2020, 7, 1);
        long millisOfDay = 24 * 60 * 60 * 1000;
        System.out.println("第一种算法=" + (startCalendar.getTimeInMillis() - endCalendar.getTimeInMillis()) / millisOfDay);

        DateTime start = new DateTime(1978, 7, 28, 0, 0);
        DateTime end = new DateTime(2020, 7, 1, 0, 0);
        int days = Days.daysBetween(start, end).getDays();
        Period period = new Period(start, end);
        System.out.println("year=" + period.getYears() + "month=" + period.getMonths() + "day=" + period.getDays());
        Days days1 = Days.daysBetween(start, end);
        System.out.println("第二种算法=" + Days.daysBetween(start, end).getDays());
    }
}