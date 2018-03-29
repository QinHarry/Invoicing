package com.halen.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static String getCurrentDateStr() throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static Date formatString(String str, String format) throws Exception {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(str);
    }

    public static String formatDate(Date date, String format) throws Exception {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null) {
            result = sdf.format(date);
        }
        return result;
    }

    public static List<String> getRangeDates(String begin, String end) throws Exception {
        List<String> datas = new ArrayList<>();
        Calendar cb = Calendar.getInstance();
        Calendar ce = Calendar.getInstance();
        cb.setTime(formatString(begin, "yyyy-MM-dd"));
        ce.setTime(formatString(end, "yyyy-MM-dd"));
        datas.add(begin);
        while (cb.before(ce)) {
            cb.add(Calendar.DAY_OF_MONTH, 1);
            datas.add(formatDate(cb.getTime(), "yyyy-MM-dd"));
        }
        return datas;
    }

    public static List<String> getRangeMonths(String begin, String end) throws Exception {
        List<String> months = new ArrayList<>();
        Calendar cb = Calendar.getInstance();
        Calendar ce = Calendar.getInstance();
        cb.setTime(formatString(begin, "yyyy-MM"));
        ce.setTime(formatString(end, "yyyy-MM"));
        months.add(begin);
        while (cb.before(ce)) {
            cb.add(Calendar.MONTH, 1);
            months.add(formatDate(cb.getTime(), "yyyy-MM"));
        }
        return months;
    }
}
