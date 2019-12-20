package my.edu.um.fsktm.unihelp.ui.course.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Course;
import my.edu.um.fsktm.unihelp.models.Instructor;
import my.edu.um.fsktm.unihelp.ui.course.adapters.InstructorAdapter;
import my.edu.um.fsktm.unihelp.ui.course.adapters.PrerequisiteAdapter;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.LoadingScreen;

public class CourseInstructorsActivity extends AppCompatActivity {
    private String mCourseCode;
    private TextView courseCode, courseName, faculty;
    private InstructorAdapter leadAdapter, coAdapter;
    private ArrayList<Instructor> leadInstructorList, coInstructorList;
    private AlertDialog loading;
    private int queryCounter = 0;
    private Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(CourseInstructorsActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
            queryCounter--;
            if (queryCounter == 0)
                loading.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_instructors_layout);

        leadInstructorList = new ArrayList<>();
        coInstructorList = new ArrayList<>();
        loading = LoadingScreen.build(CourseInstructorsActivity.this);

        mCourseCode = getIntent().getStringExtra("courseCode");
        renderActionBar();
        initViews();
        queryCourseDescription();
        queryInstructorList(true);
        queryInstructorList(false);
    }

    private void initViews() {
        courseCode = findViewById(R.id.courseCode);
        courseName = findViewById(R.id.courseName);
        faculty = findViewById(R.id.faculty);

        leadAdapter = new InstructorAdapter(leadInstructorList);
        initRecyclerView(R.id.leadInstructorsList, leadAdapter);

        coAdapter = new InstructorAdapter(coInstructorList);
        initRecyclerView(R.id.coInstructorsList, coAdapter);
    }

    private void initRecyclerView(int id, RecyclerView.Adapter adapter) {
        RecyclerView recyclerView = findViewById(id);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void renderActionBar() {
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Course Instructors");
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
        loading.show();
        queryCounter++;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    JSONObject courseObject = data.getJSONObject(0);

                    courseCode.setText(courseObject.getString("0"));
                    courseName.setText(courseObject.getString("1"));
                    faculty.setText(courseObject.getString("2"));
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "SELECT " +
                "   A.id, " +       // 0
                "   A.name, " +       // 1
                "   B.name " +             // 2
                "FROM course A " +
                "JOIN faculty B " +
                "   ON A.faculty = B.id " +
                "WHERE A.id = '" + mCourseCode + "'";

        Database.sendQuery(this, query, listener, error);
    }


    private void queryInstructorList(boolean lead) {
        final ArrayList<Instructor> instructors;
        final InstructorAdapter adapter;
        int leadVal;
        if (lead) {
            instructors = leadInstructorList;
            adapter = leadAdapter;
            leadVal = 1;
        } else {
            instructors = coInstructorList;
            adapter = coAdapter;
            leadVal = 0;
        }
        loading.show();
        queryCounter++;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    instructors.clear();
                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
                        JSONObject entry = data.getJSONObject(i);
                        instructors.add(new Instructor(
                                entry.getString("0"),
                                entry.getString("1"),
                                entry.getString("2"),
                                entry.getString("3")
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "SELECT " +
                "   B.email, " +
                "   B.name, " +
                "   B.department, " +
                "   C.name " +
                "FROM course_instructor A " +
                "JOIN instructor B " +
                "   ON A.instructor = B.id " +
                "JOIN faculty C " +
                "   ON B.faculty = C.id " +
                "WHERE A.course = '" + mCourseCode + "' AND A.lead = " + leadVal;

        Database.sendQuery(this, query, listener, error);
    }


}
