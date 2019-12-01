package my.edu.um.fsktm.unihelp.ui.course;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Course;

public class CourseRatingFormActivity extends AppCompatActivity {
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_rating_form_layout);

        course = (Course) getIntent().getSerializableExtra("course");

        TextView courseCode = findViewById(R.id.courseCode);
        TextView courseName = findViewById(R.id.courseName);
        TextView faculty = findViewById(R.id.faculty);

        courseCode.setText(course.getCourseCode());
        courseName.setText(course.getCourseName());
        faculty.setText(course.getFaculty().toString());
    }
}
