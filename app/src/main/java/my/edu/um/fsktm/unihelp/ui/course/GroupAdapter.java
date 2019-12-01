package my.edu.um.fsktm.unihelp.ui.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private ArrayList<Group> groupList;

    public GroupAdapter(ArrayList<Group> groupList) { this.groupList = groupList; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupIndex;
        public RecyclerView slotList;
        public Context context;

        public ViewHolder(View view) {
            super(view);
            groupIndex = view.findViewById(R.id.groupIndex);
            slotList = view.findViewById(R.id.slotList);
            context = view.getContext();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groupView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_group_item, parent, false);
        return new ViewHolder(groupView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Group group = groupList.get(position);
        int index = position + 1;
        holder.groupIndex.setText("Group " + index);

        holder.slotList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.context);
        holder.slotList.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new SlotAdapter(group.getSlots());
        holder.slotList.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
