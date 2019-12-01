package my.edu.um.fsktm.unihelp.ui.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Instructor;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> {
    private ArrayList<Instructor> instructorList;

    public InstructorAdapter(ArrayList<Instructor> instructorList) { this.instructorList = instructorList; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView instructorName, instructorEmail, instructorDepartment, instructorFaculty;
        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            instructorName = view.findViewById(R.id.instructorName);
            instructorEmail = view.findViewById(R.id.instructorEmail);
            instructorDepartment = view.findViewById(R.id.instructorDepartment);
            instructorFaculty = view.findViewById(R.id.instructorFaculty);
        }
    }

    @Override
    public InstructorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View instructorView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_instructor_detail_item, parent, false);
        return new InstructorAdapter.ViewHolder(instructorView);
    }

    @Override
    public void onBindViewHolder(InstructorAdapter.ViewHolder holder, int position) {
        Instructor instructor = instructorList.get(position);
        holder.instructorName.setText(instructor.getName());
        holder.instructorEmail.setText(instructor.getEmail());
        holder.instructorDepartment.setText(instructor.getDepartment());
        holder.instructorFaculty.setText(instructor.getFaculty().toString());
    }

    @Override
    public int getItemCount() {
        return instructorList.size();
    }
}
