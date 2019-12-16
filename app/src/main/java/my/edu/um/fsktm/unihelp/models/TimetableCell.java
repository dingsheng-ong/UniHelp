package my.edu.um.fsktm.unihelp.models;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;

public class TimetableCell {
    private int time;
    private ArrayList<Slot> slots;
    private boolean primary;

    public TimetableCell(int time) {
        this.slots = new ArrayList<>();
        this.primary = false;
        this.time = time;
    }

    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getBg() {
        if (slots.size() > 1) {
            return R.drawable.timetable_border_conflict;
        }
        if (slots.size() == 1) {
            if (primary) {
                return R.drawable.timetable_border_accent;
            }
            return R.drawable.timetable_border_secondary;
        }
        return R.drawable.timetable_border;
    }

    public String getText() {
        if (slots.size() == 1 && slots.get(0).getTimeStart() == time) {
            Slot slot = slots.get(0);
            return String.format("%s\n%s", slot.getCourseCode(), slot.getTypePretty());
        }
        if (slots.size() > 1) {
            String conflict = "! Conflict";
            for (Slot slot : slots) {
                conflict += String.format("\n%s - %s", slot.getCourseCode(), slot.getTypePretty());
            }
            return conflict;
        }
        return "";
    }

    public void clear() {
        this.slots.clear();
        this.primary = false;
    }
}
