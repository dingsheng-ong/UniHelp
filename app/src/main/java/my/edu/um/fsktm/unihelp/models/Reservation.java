package my.edu.um.fsktm.unihelp.models;

import my.edu.um.fsktm.unihelp.util.Message;

public class Reservation {

    private final String time;
    private Message status;

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

    public void setStatus(Message status) { this.status = status; }
}
