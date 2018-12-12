package utils;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DateUtils {
    /**
     * 将Date转换成String
     * @param date
     * @return
     */
    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }
}
