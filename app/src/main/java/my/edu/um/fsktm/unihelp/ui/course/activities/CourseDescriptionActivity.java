package my.edu.um.fsktm.unihelp.ui.course;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Course;

public class CourseDescriptionActivity extends AppCompatActivity {

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_description_layout);

        course = (Course) getIntent().getSerializableExtra("course");

        TextView courseCode = findViewById(R.id.courseCode);
        TextView courseName = findViewById(R.id.courseName);
        TextView faculty = findViewById(R.id.faculty);

        courseCode.setText(course.getCourseCode());
        courseName.setText(course.getCourseName());
        faculty.setText(course.getFaculty().toString());

        TextView courseDescription = findViewById(R.id.courseDescription);
        TextView courseLearningOutcomes = findViewById(R.id.courseLearningOutcomes);
        courseDescription.setText(course.getDescription());
        courseLearningOutcomes.setText(course.getLearningOutcome());

        RecyclerView preqListView = findViewById(R.id.coursePrerequisiteList);
        preqListView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        preqListView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new PrerequisiteAdapter(course.getPrerequisite());
        preqListView.setAdapter(adapter);
    }
}
