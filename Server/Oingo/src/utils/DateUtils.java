package utils;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class DateUtils {
    /**
     * 将Date转换成String
     * @param date
     * @return
     */
    public static String date2String(Date date) {
        Calendar calendar =new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, +18);
        calendar.add(Calendar.MINUTE, 30);
        java.util.Date utilDate = calendar.getTime();
        Date newDate =new Date(utilDate.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(newDate);
        return dateStr;
    }


    public static String date2String29(Date date) {
        Calendar calendar =new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, +29);
        java.util.Date utilDate = calendar.getTime();
        Date newDate =new Date(utilDate.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(newDate);
        return dateStr;
    }

    public static Date str2Date(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static java.sql.Date str2SqlDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }


    public static java.sql.Time str2Time(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Time time = new java.sql.Time(d.getTime());
        return time;
    }
}
