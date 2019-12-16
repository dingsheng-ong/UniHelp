package my.edu.um.fsktm.unihelp.ui.course.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.TimetableCell;

public class TimetableRowAdapter extends RecyclerView.Adapter<TimetableRowAdapter.ViewHolder> {
    private TimetableCell[][] timetable;

    public TimetableRowAdapter(TimetableCell[][] timetable) {
        this.timetable = timetable;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timeLabel, mon, tue, wed, thu, fri;

        public ViewHolder(View view) {
            super(view);
            timeLabel = view.findViewById(R.id.time);
            mon = view.findViewById(R.id.timetable_mon);
            tue = view.findViewById(R.id.timetable_tue);
            wed = view.findViewById(R.id.timetable_wed);
            thu = view.findViewById(R.id.timetable_thu);
            fri = view.findViewById(R.id.timetable_fri);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_group_timetable_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String timeLabel;
        if (position == 4) {
            timeLabel = "12 PM";
        } else if (position > 4) {
            timeLabel = String.format("%2d PM", position - 4);
        } else {
            timeLabel = String.format("%2d AM", position + 8);
        }
        holder.timeLabel.setText(timeLabel);
        TimetableCell mon = timetable[0][position],
                tue = timetable[1][position],
                wed = timetable[2][position],
                thu = timetable[3][position],
                fri = timetable[4][position];
        holder.mon.setText(mon.getText());
        holder.mon.setBackgroundResource(mon.getBg());
        holder.tue.setText(tue.getText());
        holder.tue.setBackgroundResource(tue.getBg());
        holder.wed.setText(wed.getText());
        holder.wed.setBackgroundResource(wed.getBg());
        holder.thu.setText(thu.getText());
        holder.thu.setBackgroundResource(thu.getBg());
        holder.fri.setText(fri.getText());
        holder.fri.setBackgroundResource(fri.getBg());
    }

    @Override
    public int getItemCount() {
        return 14;
    }
}
