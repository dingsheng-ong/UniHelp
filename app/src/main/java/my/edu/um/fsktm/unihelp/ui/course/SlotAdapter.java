package my.edu.um.fsktm.unihelp.ui.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.ViewHolder> {
    private ArrayList<Slot> slotList;

    public SlotAdapter(ArrayList<Slot> slotList) { this.slotList = slotList; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView classType, classDay, classTime;

        public ViewHolder(View view) {
            super(view);
            classType = view.findViewById(R.id.classType);
            classDay = view.findViewById(R.id.classDay);
            classTime = view.findViewById(R.id.classTime);
        }
    }

    @Override
    public SlotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View slotView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_slot_item, parent, false);
        return new SlotAdapter.ViewHolder(slotView);
    }

    @Override
    public void onBindViewHolder(SlotAdapter.ViewHolder holder, int position) {
        Slot slot = slotList.get(position);
        String type = slot.getType();
        type = type.substring(0,1).toUpperCase() + type.substring(1);
        holder.classType.setText(type);
        holder.classDay.setText(slot.getDayString().substring(0,3).toUpperCase());
        holder.classTime.setText(slot.getTimeStart12HString() + " - " + slot.getTimeEnd12HString());
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }
}
