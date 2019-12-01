package my.edu.um.fsktm.unihelp.models;

import java.io.Serializable;

public class Slot implements Serializable {
    private String type;
    private int day;
    private int[] timeStart, timeEnd;
    private Location location;
    final static String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public Slot() {
        timeStart = new int[2];
        timeEnd = new int[2];
    }

    public String getType() {
        return type;
    }

    public int getDay() {
        return day;
    }

    public String getDayString() { return days[day]; }

    public int[] getTimeStart() {
        return timeStart;
    }

    public String getTimeStart12HString() {
        return get12HString(timeStart);
    }

    public int[] getTimeEnd() {
        return timeEnd;
    }

    public String getTimeEnd12HString() {
        return get12HString(timeEnd);
    }

    public String get12HString(int[] time) {
        String label = "AM";
        int hour = time[0];
        if (hour >= 12) {
            label = "PM";
            if (hour > 12) hour -= 12;
        }
        if (hour == 0) hour = 12;
        return String.format("%02d", hour) + ":" + String.format("%02d", time[1]) + label;
    }

    public Location getLocation() {
        return location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setTimeStart(int[] timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeStart(int hour, int min) {
        timeStart[0] = hour;
        timeStart[1] = min;
    }

    public void setTimeEnd(int[] timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setTimeEnd(int hour, int min) {
        timeEnd[0] = hour;
        timeEnd[1] = min;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
