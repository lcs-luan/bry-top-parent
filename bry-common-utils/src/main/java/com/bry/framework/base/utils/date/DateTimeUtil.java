//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bry.framework.base.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;

public class DateTimeUtil {
    public static final String DATE_TIME_MSEC_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String TIME_M_PATTERN = "HH:mm";
    public static final String MONTH_DAY_PATTER = "MM-dd";
    public static final String MONTH_DAY_SS_PATTER = "MM-dd HH:mm";
    public static final String DATE_TIME_SS_PATTER = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final int MONDAY = 1;
    private static final int TUESDAY = 2;
    private static final int WEDNESDAY = 3;
    private static final int THURSDAY = 4;
    private static final int FRIDAY = 5;
    private static final int SATURDAY = 6;
    private static final int SUNDAY = 7;

    public DateTimeUtil() {
    }

    public static Date now() {
        return new Date();
    }

    public static Calendar calendar() {
        Calendar cal = Calendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(2);
        return cal;
    }

    public static String toStr(Date date, String format) {
        SimpleDateFormat customFormat = (SimpleDateFormat) DATE_TIME_FORMAT.clone();
        customFormat.applyPattern(format.trim());
        return customFormat.format(date);
    }

    public static String toStr(Date date) {
        return toStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toStr() {
        return toStr(now(), "yyyy-MM-dd HH:mm:ss");
    }

    public static Date toDate(String date, String format) throws ParseException {
        SimpleDateFormat customFormat = (SimpleDateFormat) DATE_TIME_FORMAT.clone();
        customFormat.applyPattern(format.trim());
        return customFormat.parse(date);
    }

    public static Date toDate(String date) throws ParseException {
        return toDate(date.trim(), "yyyy-MM-dd HH:mm:ss");
    }

    public static long getMillis() {
        return System.currentTimeMillis();
    }

    public static int getSecs() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public static int getMonth() {
        return getMonth(now());
    }

    public static int getMonth(Date date) {
        Calendar calendar = calendar();
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static int getDayOfMonth() {
        return getDayOfMonth(now());
    }

    public static int getDayOfMonth(Date date) {
        Calendar calendar = calendar();
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static int getDayOfWeek() {
        return getDayOfWeek(now());
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = calendar();
        calendar.setTime(date);
        return calendar.get(7);
    }

    public static int getDayOfYear() {
        return getDayOfYear(now());
    }

    public static int getDayOfYear(Date date) {
        Calendar calendar = calendar();
        calendar.setTime(date);
        return calendar.get(6);
    }

    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

    public static boolean isEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    public static boolean isBetween(Date beginDate, Date endDate, Date src) {
        return beginDate.before(src) && endDate.after(src);
    }

    public static Date getLastDayOfMonth() {
        return getLastDayOfMonth(now());
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = calendar();
        cal.setTime(date);
        cal.set(5, 1);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        cal.set(2, cal.get(2) + 1);
        cal.set(14, -1);
        return cal.getTime();
    }

    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(now());
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = calendar();
        cal.setTime(date);
        cal.set(5, 1);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    private static Date weekDay(Date date, int week) {
        Calendar cal = calendar();
        cal.setTime(date);
        cal.set(7, week);
        return cal.getTime();
    }

    public static Date getDateOfWeek(Date date, int week) {
        Date result;
        switch (week) {
            case 1:
                result = weekDay(date, 2);
                break;
            case 2:
                result = weekDay(date, 3);
                break;
            case 3:
                result = weekDay(date, 4);
                break;
            case 4:
                result = weekDay(date, 5);
                break;
            case 5:
                result = weekDay(date, 6);
                break;
            case 6:
                result = weekDay(date, 7);
                break;
            case 7:
                result = weekDay(date, 1);
                break;
            default:
                result = weekDay(date, 2);
        }

        return result;
    }

    public static Date getDateOfWeek(int week) {
        Date result;
        switch (week) {
            case 1:
                result = weekDay(now(), 2);
                break;
            case 2:
                result = weekDay(now(), 3);
                break;
            case 3:
                result = weekDay(now(), 4);
                break;
            case 4:
                result = weekDay(now(), 5);
                break;
            case 5:
                result = weekDay(now(), 6);
                break;
            case 6:
                result = weekDay(now(), 7);
                break;
            case 7:
                result = weekDay(now(), 1);
                break;
            default:
                result = weekDay(now(), 2);
        }

        return result;
    }

    public static int getSecsOfMorning(Date date) {
        Calendar cal = calendar();
        cal.setTime(date);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return (int) (cal.getTimeInMillis() / 1000L);
    }

    public static int getSecsOfMorning(int timestamp) {
        Calendar cal = calendar();
        cal.setTimeInMillis((long) ((double) timestamp * 1000.0D));
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return (int) (cal.getTimeInMillis() / 1000L);
    }

    public static int getSecsOfNight(Date date) {
        Calendar cal = calendar();
        cal.setTime(date);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(14, 999);
        return (int) (cal.getTimeInMillis() / 1000L);
    }

    public static int getSecsOfNight(int timeStamp) {
        Calendar cal = calendar();
        cal.setTimeInMillis((long) ((double) timeStamp * 1000.0D));
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(14, 999);
        return (int) (cal.getTimeInMillis() / 1000L);
    }

    public static Date getDateByTimeStamp(int timeStamp) {
        Calendar cal = calendar();
        cal.setTimeInMillis((long) ((double) timeStamp * 1000.0D));
        return cal.getTime();
    }

    public static int getTimeStampByDate(Date date) {
        Calendar cal = calendar();
        cal.setTime(date);
        return (int) (cal.getTimeInMillis() / 1000L);
    }

    public static Date addDay(Date date, int day) {
        Calendar cal = calendar();
        cal.setTime(date);
        cal.add(5, day);
        return cal.getTime();
    }

    public static int addDay(int timeStamp, int day) {
        Calendar cal = calendar();
        cal.setTimeInMillis((long) ((double) timeStamp * 1000.0D));
        cal.add(5, day);
        return (int) (cal.getTimeInMillis() / 1000L);
    }

    public static LinkedHashSet<Date> getDays(Date startDate, Date endDate) throws ParseException {
        if (startDate != null && endDate != null) {
            if (startDate.compareTo(endDate) <= 0) {
                return new LinkedHashSet();
            } else {
                LinkedHashSet<Integer> list = getDays(getTimeStampByDate(startDate), getTimeStampByDate(endDate));
                LinkedHashSet<Date> result = new LinkedHashSet();
                list.forEach((e) -> {
                    result.add(getDateByTimeStamp(e));
                });
                return result;
            }
        } else {
            return null;
        }
    }

    public static LinkedHashSet<Integer> getDays(int startTimeStamp, int endTimeStamp) throws ParseException {
        if (startTimeStamp >= endTimeStamp) {
            return new LinkedHashSet();
        } else {
            int dayInt = 86400;
            int startTimeMorning = getSecsOfMorning(startTimeStamp);
            int endTimeNight = getSecsOfNight(endTimeStamp);
            int minus = (int) Math.floor((double) (endTimeNight - startTimeMorning) * 1.0D / (double) dayInt);
            LinkedHashSet<Integer> result = new LinkedHashSet();

            for (int i = 0; i <= minus; ++i) {
                result.add(startTimeMorning + (int) ((double) (dayInt * i) * 1.0D));
            }

            return result;
        }
    }

    public static Date yearStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(1, 0);
        calendar.add(5, 0);
        calendar.add(2, 0);
        calendar.set(6, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return new Date(calendar.getTimeInMillis());
    }
}
