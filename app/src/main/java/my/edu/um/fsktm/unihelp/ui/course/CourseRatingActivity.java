package my.edu.um.fsktm.unihelp.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.edu.um.fsktm.unihelp.R;

public class CourseRatingActivity extends AppCompatActivity {
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_rating_layout);

        course = (Course) getIntent().getSerializableExtra("course");

        TextView courseCode = findViewById(R.id.courseCode);
        TextView courseName = findViewById(R.id.courseName);
        TextView faculty = findViewById(R.id.faculty);

        courseCode.setText(course.getCourseCode());
        courseName.setText(course.getCourseName());
        faculty.setText(course.getFaculty().toString());

        TextView ratingStat = findViewById(R.id.ratingStat);
        TextView reviewStat = findViewById(R.id.reviewsStat);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        ProgressBar pb1 = findViewById(R.id.progressBar1),
                pb2 = findViewById(R.id.progressBar2),
                pb3 = findViewById(R.id.progressBar3),
                pb4 = findViewById(R.id.progressBar4),
                pb5 = findViewById(R.id.progressBar5);

        String rating = "-";
        if (course.getReviews().size() > 0) {
            rating = String.format("%.1f", course.getAverageRating());
        }
        ratingStat.setText(rating);
        reviewStat.setText(course.getReviews().size()+ " reviews");
        ratingBar.setRating(course.getAverageRating());

        int[] percentages = course.getRatingPercentage();
        pb1.setProgress(percentages[0]);
        pb2.setProgress(percentages[1]);
        pb3.setProgress(percentages[2]);
        pb4.setProgress(percentages[3]);
        pb5.setProgress(percentages[4]);

        RecyclerView reviewListView = findViewById(R.id.courseReviewsList);
        reviewListView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reviewListView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ReviewAdapter(course.getReviews());
        reviewListView.setAdapter(adapter);

        Button rateButton = findViewById(R.id.button);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseRatingActivity.this, CourseRatingFormActivity.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });
    }
}
