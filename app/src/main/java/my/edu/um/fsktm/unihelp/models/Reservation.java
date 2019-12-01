package my.edu.um.fsktm.unihelp.ui.reservation;

import my.edu.um.fsktm.unihelp.util.Message;

public class Reservation {

    private final String time;
    private final Message status;

    public Reservation(String time, Message status) {
        this.time = time;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public Message getStatus() {
        return status;
    }
}
