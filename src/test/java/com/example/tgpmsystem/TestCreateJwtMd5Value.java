package com.example.tgpmsystem;

import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestCreateJwtMd5Value {
    public static void main(String[] args) {

        Date date = parse(new Date()+"", "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        String now_time = format(date, "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String on_time = "2021-5-9 00:00:00";
        String off_time = "2021-5-13 00:00:00";


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = dateFormat.parse(now_time);
            Date date2 = dateFormat.parse(on_time);
            Date date3 = dateFormat.parse(off_time);

            if (date1.compareTo(date2) == 1 && date1.compareTo(date3) == -1) {
                System.out.println("在时间段内");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(now_time);



    }
    public static Date parse(String str, String pattern, Locale locale) {
        if(str == null || pattern == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern, locale).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String format(Date date, String pattern, Locale locale) {
        if(date == null || pattern == null) {
            return null;
        }
        return new SimpleDateFormat(pattern, locale).format(date);
    }

    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }


}
