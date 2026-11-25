//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bry.framework.base.utils.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeAgoUtils {
    private static final long ONE_SECOND = 1000L;
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";

    public TimeAgoUtils() {
    }

    public static String getTimeAgo(Date date) {
        date = date == null ? new Date() : date;
        LocalDateTime time = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long compareTime = getTimestampOfDateTime(time);
        LocalDateTime now = LocalDateTime.now();
        long rightTime = getTimestampOfDateTime(now);
        long delta = rightTime - compareTime;
        long hours;
        if (delta < 60000L) {
            hours = toSeconds(delta);
            return (hours <= 0L ? 1L : hours) + "秒前";
        } else if (delta < 3600000L) {
            hours = toMinutes(delta);
            return (hours <= 0L ? 1L : hours) + "分钟前";
        } else if (delta < 86400000L) {
            hours = toHours(delta);
            return (hours <= 0L ? 1L : hours) + "小时前";
        } else {
            return time.getYear() == now.getYear() ? time.format(DateTimeFormatter.ofPattern("MM-dd")) : time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    public static String getTimeShow(Date date) {
        if (date == null) {
            date = new Date();
        }

        LocalDateTime time = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (DateTimeUtil.getSecsOfMorning(date) == DateTimeUtil.getSecsOfMorning(DateTimeUtil.getSecs())) {
            return time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            LocalDateTime now = LocalDateTime.now();
            return time.getYear() == now.getYear() ? time.format(DateTimeFormatter.ofPattern("MM-dd HH:mm")) : time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return date / 60000L;
    }

    private static long toHours(long date) {
        return date / 3600000L;
    }

    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }
}
