package my.edu.um.fsktm.unihelp.ui.reservation;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Location implements Serializable {

    private final String locationId;
    private final String name;
    private final String faculty;
    private final double rating;
    private final int reservations;
    private final int iconResID;

    public Location(
        String locationId,
        String name,
        String faculty,
        double rating,
        int reservations,
        int iconResID
    ) {
        this.locationId = locationId;
        this.name = name;
        this.faculty = faculty;
        this.rating = rating;
        this.reservations = reservations;
        this.iconResID = iconResID;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public String getFaculty() {
        return faculty;
    }

    public double getRating() {
        return rating;
    }

    public int getReservations() {
        return reservations;
    }

    public int getIconResID() {
        return iconResID;
    }
}
