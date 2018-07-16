package greencode.ir.pillcci.utils;

import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 6/24/18.
 */

public class PersianCalculater {
    public static String getHourseAndMin(long milis){
        PersianDate date = new PersianDate(milis);
        return ((date.getHour()>=10?date.getHour()+"":"0"+date.getHour())+":"+(date.getMinute()>=10?date.getMinute()+"":"0"+date.getMinute()));
    }

    public static String getMonthAndDay(long time) {
        PersianDate date = new PersianDate(time);
        return ((date.getShMonth()>=10?date.getShMonth()+"":"0"+date.getShMonth())+"/"+(date.getShDay()>=10?date.getShDay()+"":"0"+date.getShDay()));
    }
    public static String getYearMonthAndDay(long time) {
        PersianDate date = new PersianDate(time);
        return (date.getShYear()+"/"+(date.getShMonth()>=10?date.getShMonth()+"":"0"+date.getShMonth())+"/"+(date.getShDay()>=10?date.getShDay()+"":"0"+date.getShDay()));

    }

    public static String getYearMonthAndDay(int year, int month, int day) {
        return year+"/"+(month>=10?month+"":"0"+month)+"/"+(day>=10?day+"":"0"+day);

    }

    public static String getHourseAndMin(int stardHour, int startMin) {
        return ((stardHour>=10?stardHour+"":"0"+stardHour)+":"+(startMin>=10?startMin+"":"0"+startMin));

    }
}
