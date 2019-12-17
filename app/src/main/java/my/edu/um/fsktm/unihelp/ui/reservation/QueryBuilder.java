package my.edu.um.fsktm.unihelp.ui.reservation;

import java.util.Calendar;
import java.util.Locale;

import my.edu.um.fsktm.unihelp.util.DateParser;

public class QueryBuilder {

    public final static String reservationCount = "WITH reservation_count AS (          \n" +
                                                  "    SELECT A.id AS id,               \n" +
                                                  "           COUNT(A.id) AS count      \n" +
                                                  "      FROM location A                \n" +
                                                  "      JOIN reservation B             \n" +
                                                  "        ON A.id = B.location         \n" +
                                                  "     GROUP BY A.id                   \n" +
                                                  ") SELECT A.id,                       \n" +
                                                  "         A.name,                     \n" +
                                                  "         C.name,                     \n" +
                                                  "         IFNULL(count, 0)            \n" +
                                                  "    FROM location A                  \n" +
                                                  "    LEFT JOIN reservation_count B    \n" +
                                                  "      ON A.id = B.id                 \n" +
                                                  "    JOIN faculty C                   \n" +
                                                  "      ON A.faculty = C.id            \n" +
                                                  "   ORDER BY count DESC               ";

    public final static String roomSchedule(String date, String location) {
        String query = "SELECT location,\n" +
                      "       user,\n" +
                      "       time_start,\n" +
                      "       time_end,\n" +
                      "       strftime('%%Y-%%m-%%d', time_start) AS \"date\"\n" +
                      "  FROM reservation\n" +
                      " WHERE location = '%s'\n" +
                      "   AND (\"date\" = '%s' OR \"date\" = '2000-01-01')\n" +
                      "   AND day = strftime('%%w', '%s')";
        return String.format(Locale.US, query, location, date, date);
    }

    public static String bookRoom(String user, String location, Calendar timeStart, Calendar timeEnd) {
        String query = "INSERT INTO reservation (user, location, time_start, time_end, day)\n" +
                       "SELECT '%s', '%s', '%s', '%s', %d\n" +
                       "WHERE NOT EXISTS(\n" +
                       "   SELECT strftime('%%Y-%%m-%%d', time_start) AS \"date\",\n" +
                       "          CAST(strftime('%%H', time_start) AS NUMBER) AS \"hourStart\",\n" +
                       "          CAST(strftime('%%H', time_end) AS NUMBER) AS \"hourEnd\"\n" +
                       "     FROM reservation\n" +
                       "    WHERE (\"date\" = '%s' OR \"date\" = '2000-01-01')\n" +
                       "      AND (\n" +
                       "                %d BETWEEN \"hourStart\" AND \"hourEnd\"\n" +
                       "             OR %d BETWEEN \"hourStart\" AND \"hourEnd\"\n" +
                       "             OR \"hourStart\" BETWEEN %d AND %d\n" +
                       "             OR \"hourEnd\" BETWEEN %d AND %d\n" +
                       "          )\n" +
                       "      AND day = %d\n" +
                       "      AND %d != \"hourEnd\"\n" +
                       "      AND %d != \"hourStart\"\n" +
                       "      AND location = '%s'\n" +
                       ")";

        String from = DateParser.calendarToString(timeStart, "yyyy-MM-dd HH");
        String to = DateParser.calendarToString(timeEnd, "yyyy-MM-dd HH");
        from += ":00:00"; to += ":00:00";
        int day = timeStart.get(Calendar.DAY_OF_WEEK) - 1;
        int hourStart = timeStart.get(Calendar.HOUR_OF_DAY);
        int hourEnd   = timeEnd.get(Calendar.HOUR_OF_DAY);
        String date = DateParser.calendarToString(timeStart, "yyyy-MM-dd");

        return String.format(Locale.US, query,
            user, location, from, to, day,
            date,
            hourStart,
            hourEnd,
            hourStart, hourEnd,
            hourStart, hourEnd,
            day,
            hourStart,
            hourEnd,
            location
        );
    }

    public static String cancelRoom(String user, String location, String from , String to) {
        String query = "DELETE FROM reservation\n" +
                       " WHERE user = '%s'\n" +
                       "   AND location = '%s'\n" +
                       "   AND time_start >= '%s'\n" +
                       "   AND time_end <= '%s'";
        return String.format(Locale.US, query, user, location, from, to);
    }
}
