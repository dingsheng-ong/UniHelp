package my.edu.um.fsktm.unihelp.ui.course;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;

public class CourseInstructorsActivity extends AppCompatActivity {
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_instructors_layout);

        course = (Course) getIntent().getSerializableExtra("course");

        TextView courseCode = findViewById(R.id.courseCode);
        TextView courseName = findViewById(R.id.courseName);
        TextView faculty = findViewById(R.id.faculty);

        courseCode.setText(course.getCourseCode());
        courseName.setText(course.getCourseName());
        faculty.setText(course.getFaculty().toString());


        ArrayList<Instructor> leadInstructors = new ArrayList<>();
        ArrayList<Instructor> coInstructors = (ArrayList<Instructor>) course.getInstructors().clone();
        leadInstructors.add(coInstructors.remove(0));

        RecyclerView leadInstructorListView = findViewById(R.id.leadInstructorsList);
        leadInstructorListView.setHasFixedSize(true);
        RecyclerView.LayoutManager leadInstructorLayoutManager = new LinearLayoutManager(this);
        leadInstructorListView.setLayoutManager(leadInstructorLayoutManager);
        RecyclerView.Adapter leadInstructorAdapter = new InstructorAdapter(leadInstructors);
        leadInstructorListView.setAdapter(leadInstructorAdapter);

        RecyclerView coInstructorListView = findViewById(R.id.coInstructorsList);
        coInstructorListView.setHasFixedSize(true);
        RecyclerView.LayoutManager coInstructorLayoutManager = new LinearLayoutManager(this);
        coInstructorListView.setLayoutManager(coInstructorLayoutManager);
        RecyclerView.Adapter coInstructorAdapter = new InstructorAdapter(coInstructors);
        coInstructorListView.setAdapter(coInstructorAdapter);
    }
}
