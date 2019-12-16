package my.edu.um.fsktm.unihelp.ui.course.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Course;
import my.edu.um.fsktm.unihelp.ui.course.activities.CourseDetailsActivity;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private ArrayList<Course> courseList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView courseCode, courseName, rating, faculty, leadInstructor, credits, capacity, reviewStat;
        public RatingBar ratingBar;
        public CardView courseCardView;
        public Context context;

        public ViewHolder(View view) {
            super(view);
            courseCode = view.findViewById(R.id.courseCode);
            courseName = view.findViewById(R.id.courseName);
            rating = view.findViewById(R.id.rating);
            ratingBar = view.findViewById(R.id.ratingBar);
            faculty = view.findViewById(R.id.faculty);
            leadInstructor = view.findViewById(R.id.leadInstructor);
            credits = view.findViewById(R.id.credits);
            capacity = view.findViewById(R.id.capacity);
            reviewStat = view.findViewById(R.id.reviewStat);
            courseCardView = view.findViewById(R.id.courseCardView);
            context = view.getContext();
        }
    }

    public CourseAdapter(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View courseCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new ViewHolder(courseCardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Course course = courseList.get(position);
        final Context context = holder.context;

        holder.courseCode.setText(course.getCourseCode());
        holder.courseName.setText(course.getCourseName());

        String rating = "-";
        if (course.getReviews() > 0) {
            rating = String.format("%.1f", course.getRating());
        }
        holder.rating.setText(rating);
        holder.ratingBar.setRating((float) course.getRating());
        holder.reviewStat.setText(String.format("%d reviews", course.getReviews()));
        holder.faculty.setText(course.getFaculty());
        holder.leadInstructor.setText(course.getLeadInstructor());
        holder.capacity.setText(String.format("Capacity: %d", course.getCapacity()));
        holder.credits.setText(String.format("Credit: %d", course.getCredits()));



        holder.courseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseDetailsActivity.class);
                intent.putExtra("courseCode", course.getCourseCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
