package com.example.tgpmsystem.utils;

public class Constants {

    int DEFAULT_SIZE = 30;

    public interface Student {
        String STUDENT_TOKEN_KEY = "student_token_key";
        String STUDENT_COOKIE_TOKEN_KEY = "student_cookie_token_key";
    }

    public interface Teacher {
        String TEACHER_TOKEN_KEY = "teacher_token_key";
        String TEACHER_COOKIE_TOKEN_KEY = "teacher_cookie_token_key";
    }

    public interface Admin {
        String ADMIN_TOKEN_KEY = "admin_token_key";
        String ADMIN_COOKIE_TOKEN_KEY = "admin_cookie_token_key";
    }

    public interface Setting{
        String ADMIN_ACCOUNT_INIT_STATUS = "admin_account_init_status";
    }

    /**
     * 单位是毫秒
     */
    public interface TimeValueInMillions {
        long MIN = 60 * 1000;
        long HOUR = 60 * MIN;
        long HOUR_2 = 60 * MIN * 2;
        long DAY = 24 * HOUR;
        long WEEK = 7 * DAY;
        long MONTH = 30 * DAY;
    }

    /**
     * 单位是秒
     */
    public interface TimeValueInSecond {
        int MIN = 60;
        int HOUR = 60 * MIN;
        int HOUR_2 = 60 * MIN * 2;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
    }

}
