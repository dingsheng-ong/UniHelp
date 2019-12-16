package my.edu.um.fsktm.unihelp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String id;
    private ArrayList<Slot> slots;

    public Group(String id, ArrayList<Slot> slots) {
        this.id = id;
        this.slots = slots;
    }

    public void setSlots(ArrayList<Slot> slots) {
        this.slots = slots;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Slot> getSlots() {
        return slots;
    }


    public String getId() {
        return id;
    }
}
