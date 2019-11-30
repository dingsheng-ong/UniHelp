package my.edu.um.fsktm.unihelp.util;

import java.util.Arrays;

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

}
