package my.edu.um.fsktm.unihelp.ui.course.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Course;
import my.edu.um.fsktm.unihelp.ui.course.adapters.PrerequisiteAdapter;
import my.edu.um.fsktm.unihelp.util.Database;

public class CourseDescriptionActivity extends AppCompatActivity {

    private String mCourseCode;
    private TextView courseCode, courseName, faculty, description, learningOutcome;
    private RecyclerView.Adapter adapter;
    private ArrayList<Course> preqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_description_layout);

        preqList = new ArrayList<>();
        mCourseCode = getIntent().getStringExtra("courseCode");

        renderActionBar();
        initViews();
        queryCourseDescription();
        queryPreqList();
    }

    private void initViews() {
        courseCode = findViewById(R.id.courseCode);
        courseName = findViewById(R.id.courseName);
        faculty = findViewById(R.id.faculty);
        description = findViewById(R.id.courseDescription);
        learningOutcome = findViewById(R.id.courseLearningOutcomes);

        RecyclerView recyclerView = findViewById(R.id.coursePrerequisiteList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PrerequisiteAdapter(preqList);
        recyclerView.setAdapter(adapter);
    }


    private void renderActionBar() {
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Course Description");
        } catch (NullPointerException e) {
            Log.e("Error", e.toString());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void queryCourseDescription() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    JSONObject courseObject = data.getJSONObject(0);

                    courseCode.setText(courseObject.getString("0"));
                    courseName.setText(courseObject.getString("1"));
                    faculty.setText(courseObject.getString("2"));
                    description.setText(courseObject.getString("3"));
                    learningOutcome.setText(courseObject.getString("4"));
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
            }
        };

        String query = "SELECT " +
                "   A.id, " +       // 0
                "   A.name, " +       // 1
                "   B.name, " +             // 2
                "   A.description, " +      // 3
                "   A.outcome " +   // 4
                "FROM course A " +
                "JOIN faculty B " +
                "   ON A.faculty = B.id " +
                "WHERE A.id = '" + mCourseCode + "'";

        Database.sendQuery(this, query, listener);
    }

    private void queryPreqList() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    preqList.clear();
                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
                        JSONObject entry = data.getJSONObject(i);
                        preqList.add(new Course(
                                entry.getString("0"),
                                entry.getString("1")
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
            }
        };

        String query = "SELECT " +
                "   A.preq, " +
                "   B.name " +
                "FROM preq A " +
                "JOIN course B " +
                "   ON A.preq = B.id " +
                "WHERE A.course = '" + mCourseCode + "'";

        Database.sendQuery(this, query, listener);
    }
}
