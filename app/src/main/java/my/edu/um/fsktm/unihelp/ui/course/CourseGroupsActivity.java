package my.edu.um.fsktm.unihelp.ui.course;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.edu.um.fsktm.unihelp.R;

public class CourseGroupsActivity extends AppCompatActivity {
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_groups_layout);

        course = (Course) getIntent().getSerializableExtra("course");

        TextView courseCode = findViewById(R.id.courseCode);
        TextView courseName = findViewById(R.id.courseName);
        TextView faculty = findViewById(R.id.faculty);

        courseCode.setText(course.getCourseCode());
        courseName.setText(course.getCourseName());
        faculty.setText(course.getFaculty().toString());

        String[] groups = new String[course.getGroups().size()];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = "Group " + (i + 1);
        }
        Spinner spinner = findViewById(R.id.spinnerCourseGroups);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, groups);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RecyclerView slotList = findViewById(R.id.courseGroupSlots);
                slotList.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(parent.getContext());
                slotList.setLayoutManager(layoutManager);
                RecyclerView.Adapter adapter = new SlotAdapter(course.getGroups().get(position).getSlots());
                slotList.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
