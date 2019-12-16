package my.edu.um.fsktm.unihelp.models;

import java.io.Serializable;

public class Slot implements Serializable {
    private String type, location, courseCode;
    private int day, timeStart, timeEnd;
    final static String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public Slot(String type, int timeStart, int timeEnd, int day) {
        this.type = type;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.day = day;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getType() {
        return type;
    }

    public String getTypePretty() { return type.substring(0,1).toUpperCase() + type.substring(1); }

    public int getDay() {
        return day;
    }

    public String getDayString() { return days[day]; }

    public int getTimeStart() {
        return timeStart;
    }

    public String getTimeStart12HString() {
        return get12HString(timeStart);
    }

    public int getTimeEnd() {
        return timeEnd;
    }

    public String getTimeEnd12HString() {
        return get12HString(timeEnd);
    }

    public String get12HString(int time) {
        String label = "AM";
        if (time >= 12) {
            label = "PM";
            if (time > 12) time -= 12;
        }
        if (time == 0) time = 12;
        return String.format("%02d:00%s", time, label);
    }

    public String getLocation() {
        return location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
