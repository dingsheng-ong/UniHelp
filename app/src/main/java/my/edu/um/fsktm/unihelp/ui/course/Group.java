package my.edu.um.fsktm.unihelp.ui.course;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    ArrayList<Slot> slots;

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<Slot> slots) {
        this.slots = slots;
    }
}
