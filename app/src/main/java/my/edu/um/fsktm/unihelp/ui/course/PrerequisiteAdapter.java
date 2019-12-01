package my.edu.um.fsktm.unihelp.ui.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;

public class PrerequisiteAdapter extends RecyclerView.Adapter<PrerequisiteAdapter.ViewHolder> {
    private ArrayList<Course> preqList;

    public PrerequisiteAdapter(ArrayList<Course> preqList) { this.preqList = preqList; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView courseCode, courseName;
        public ViewHolder(View view) {
            super(view);
            courseCode = view.findViewById(R.id.courseCode);
            courseName = view.findViewById(R.id.courseName);
        }
    }

    @Override
    public PrerequisiteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View preqView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_prerequisite_item, parent, false);
        return new PrerequisiteAdapter.ViewHolder(preqView);
    }

    @Override
    public void onBindViewHolder(PrerequisiteAdapter.ViewHolder holder, int position) {
        Course preq = preqList.get(position);

        holder.courseCode.setText(preq.getCourseCode());
        holder.courseName.setText(preq.getCourseName());
    }

    @Override
    public int getItemCount() {
        return preqList.size();
    }

}
