package my.edu.um.fsktm.unihelp.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DateParser {

    private static final String[] WEEKDAY = {
        "",
        "Sun",
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat"
    };

    private static final String[] MONTH = {
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
    };

    public static String parseWeekday(int day) {
        return WEEKDAY[day];
    }

    public static String parseMonth(int month) {
        return MONTH[month];
    }

    public static int revParseWeekday(String day) {
        return Arrays.asList(WEEKDAY).indexOf(day);
    }

    public static int revParseMonth(String month) {
        return Arrays.asList(MONTH).indexOf(month);
    }

    public static Calendar stringToCalendar(String timeStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(timeStr);
            calendar.setTime(date);
        } catch (ParseException e) {
            Log.e("DP 62", e.toString());
        }
        return calendar;
    }

    public static String calendarToString(Calendar calendar, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }

}
