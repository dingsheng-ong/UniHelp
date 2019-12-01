package my.edu.um.fsktm.unihelp.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Course;

public class CourseDetailsActivity extends AppCompatActivity {
    private Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_layout);

        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Course Detail");
        } catch (NullPointerException e) { Log.e("Error", e.toString()); }

        course = (Course) getIntent().getSerializableExtra("course");

        TextView courseCode = findViewById(R.id.courseCode);
        TextView courseName = findViewById(R.id.courseName);
        TextView faculty = findViewById(R.id.faculty);
        TextView credit = findViewById(R.id.credit);
        TextView ratingStat = findViewById(R.id.ratingStat);
        TextView ratingStat2 = findViewById(R.id.ratingStat2);
        TextView reviewsStat = findViewById(R.id.reviewsStat);
        TextView reviewsStat2 = findViewById(R.id.reviewsStat2);
        TextView capacity = findViewById(R.id.capacity);
        TextView seats = findViewById(R.id.seats);
        TextView taken = findViewById(R.id.taken);
        TextView description = findViewById(R.id.courseDescription);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ProgressBar pb1 = findViewById(R.id.progressBar1),
                pb2 = findViewById(R.id.progressBar2),
                pb3 = findViewById(R.id.progressBar3),
                pb4 = findViewById(R.id.progressBar4),
                pb5 = findViewById(R.id.progressBar5);


        courseCode.setText(course.getCourseCode());
        courseName.setText(course.getCourseName());
        faculty.setText(course.getFaculty().toString());
        credit.setText(Integer.toString(course.getCredits()));
        String rating = "-";
        if (course.getReviews().size() > 0) {
            rating = String.format("%.1f", course.getAverageRating());
        }
        ratingStat.setText(rating);
        ratingStat2.setText(rating);
        ratingBar.setRating(course.getAverageRating());
        reviewsStat.setText(course.getReviews().size() + " reviews");
        reviewsStat2.setText(course.getReviews().size() + " reviews");
        int[] percentages = course.getRatingPercentage();
        pb1.setProgress(percentages[0]);
        pb2.setProgress(percentages[1]);
        pb3.setProgress(percentages[2]);
        pb4.setProgress(percentages[3]);
        pb5.setProgress(percentages[4]);

        capacity.setText(Integer.toString(course.getCapacity()));
        seats.setText(Integer.toString(course.getSeats()));
        taken.setText(Integer.toString(course.getTaken()));
        description.setText(course.getDescription());

        RecyclerView instructorListView = findViewById(R.id.courseInstructorsSummary);
        instructorListView.setHasFixedSize(true);
        RecyclerView.LayoutManager instructorLayoutManager = new LinearLayoutManager(this);
        instructorListView.setLayoutManager(instructorLayoutManager);
        RecyclerView.Adapter instructorShortAdapter = new InstructorSimpleAdapter(course.getInstructors());
        instructorListView.setAdapter(instructorShortAdapter);

        RecyclerView groupListView = findViewById(R.id.courseGroupsSummary);
        groupListView.setHasFixedSize(true);
        RecyclerView.LayoutManager groupLayoutManager = new LinearLayoutManager(this);
        groupListView.setLayoutManager(groupLayoutManager);
        RecyclerView.Adapter groupShortAdapter = new GroupAdapter(course.getGroups());
        groupListView.setAdapter(groupShortAdapter);

        LinearLayout descriptionTab = findViewById(R.id.courseDescriptionTab);
        descriptionTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailsActivity.this, CourseDescriptionActivity.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });

        LinearLayout instructorTab = findViewById(R.id.courseInstructorTab);
        instructorTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailsActivity.this, CourseInstructorsActivity.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });

        LinearLayout groupTab = findViewById(R.id.courseGroupTab);
        groupTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailsActivity.this, CourseGroupsActivity.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });

        LinearLayout ratingTab = findViewById(R.id.courseRatingTab);
        ratingTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailsActivity.this, CourseRatingActivity.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
