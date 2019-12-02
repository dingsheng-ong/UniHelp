package my.edu.um.fsktm.unihelp.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.sql.Timestamp;

public class ScheduleItem implements Serializable {
    private final String type;
    private final String title;
    private final String description;
    private final Timestamp timeStart;
    private final Timestamp timeEnd;
    private final String venue;
    private final String lecturer;

    public ScheduleItem(
            String type, // booking, event, class, divider
            String title,
            String description,
            Timestamp timeStart,
            Timestamp timeEnd,
            String venue,
            String lecturer
    ) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.venue = venue;
        this.lecturer  = lecturer;
    }

    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Timestamp getTimeStart() { return timeStart;}
    public Timestamp getTimeEnd() { return timeEnd; }
    public String getVenue() { return venue;  }
    public String getLecturer() { return lecturer; }
}
