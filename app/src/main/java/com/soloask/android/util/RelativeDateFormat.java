package com.soloask.android.util;

import android.content.Context;
import android.util.Log;

import com.soloask.android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lebron on 2016/7/14.
 */
public class RelativeDateFormat {
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    public static String getCurrentTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public static boolean isTimeOut(String askTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(askTime);
            long delta = new Date().getTime() - date.getTime();
            if (delta >= 48L * ONE_HOUR) {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return false;
    }

    public static String format(String time, Context context) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "just now";
        }
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            long displayTime = seconds <= 0 ? 1 : seconds;
            return context.getResources().getQuantityString(R.plurals.dealed_time_second, (int) displayTime, (int) displayTime);
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            long displayTime = minutes <= 0 ? 1 : minutes;
            return context.getResources().getQuantityString(R.plurals.dealed_time_minute, (int) displayTime, (int) displayTime);
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            long displayTime = hours <= 0 ? 1 : hours;
            return context.getResources().getQuantityString(R.plurals.dealed_time_hour, (int) displayTime, (int) displayTime);
        }
        if (delta < 48L * ONE_HOUR) {
            return "yesterday";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            long displayTime = days <= 0 ? 1 : days;
            return context.getResources().getQuantityString(R.plurals.dealed_time_day, (int) displayTime, (int) displayTime);
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            long displayTime = months <= 0 ? 1 : months;
            return context.getResources().getQuantityString(R.plurals.dealed_time_month, (int) displayTime, (int) displayTime);
        } else {
            long years = toYears(delta);
            long displayTime = years <= 0 ? 1 : years;
            return context.getResources().getQuantityString(R.plurals.dealed_time_year, (int) displayTime, (int) displayTime);
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
}