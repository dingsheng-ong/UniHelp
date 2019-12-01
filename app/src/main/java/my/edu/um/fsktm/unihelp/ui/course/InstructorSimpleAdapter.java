package my.edu.um.fsktm.unihelp.ui.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;

public class InstructorSimpleAdapter extends RecyclerView.Adapter<InstructorSimpleAdapter.ViewHolder> {
    private ArrayList<Instructor> instructorList;

    public InstructorSimpleAdapter(ArrayList<Instructor> instructorList) { this.instructorList = instructorList; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View instructorView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_instructor_simple_item, parent, false);
        return new InstructorSimpleAdapter.ViewHolder(instructorView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Instructor instructor = instructorList.get(position);
        holder.instructorName.setText(instructor.getName());
    }

    @Override
    public int getItemCount() {
        return instructorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView instructorName;

        public ViewHolder(View itemView) {
            super(itemView);
            instructorName = itemView.findViewById(R.id.instructorName);
        }
    }
}
