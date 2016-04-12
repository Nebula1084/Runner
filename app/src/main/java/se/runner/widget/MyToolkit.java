package se.runner.widget;

import java.util.Calendar;

/**
 * Created by zieng on 4/12/16.
 */
public class MyToolKit
{
    public static String milles_to_chinese_format(long time_in_mills)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( time_in_mills );
        return calendar.get(Calendar.YEAR)+"年"+calendar.get(Calendar.MONTH)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日"+calendar.get(Calendar.HOUR_OF_DAY)+"时"+calendar.get(Calendar.MINUTE)+"分"+calendar.get(Calendar.SECOND)+"秒";
    }
}
