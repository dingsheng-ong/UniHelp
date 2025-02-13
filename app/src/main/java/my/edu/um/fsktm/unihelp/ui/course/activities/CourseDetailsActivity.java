package my.edu.um.fsktm.unihelp.ui.course.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import java.util.HashMap;
import java.util.Set;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Course;
import my.edu.um.fsktm.unihelp.models.Group;
import my.edu.um.fsktm.unihelp.models.Instructor;
import my.edu.um.fsktm.unihelp.models.Slot;
import my.edu.um.fsktm.unihelp.ui.course.adapters.GroupAdapter;
import my.edu.um.fsktm.unihelp.ui.course.adapters.InstructorSimpleAdapter;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.LoadingScreen;
import my.edu.um.fsktm.unihelp.util.Preferences;

public class CourseDetailsActivity extends AppCompatActivity {
    private TextView courseCode, courseName, faculty, credit, ratingStat, ratingStat2, reviewsStat, reviewsStat2, capacity, seats, taken, description;
    private RatingBar ratingBar;
    private ProgressBar pb1, pb2, pb3, pb4, pb5;
    private String mCourseCode;
    private Button button;
    private AlertDialog loading;
    private int queryCounter = 0;
    private Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(CourseDetailsActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
            queryCounter--;
            if (queryCounter == 0)
                loading.dismiss();
        }
    };

    RecyclerView.Adapter instructorAdapter, groupAdapter;
    ArrayList<Instructor> instructorList;
    ArrayList<Group> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_layout);

        mCourseCode = getIntent().getStringExtra("courseCode");
        instructorList = new ArrayList<>();
        groupList = new ArrayList<>();
        loading = LoadingScreen.build(CourseDetailsActivity.this);

        renderActionBar();

        initViews();
        queryCourseDetails();
        initGroupRecyclerView();
        initInstructorRecyclerView();
        queryGroupList();
        queryInstructorList();
        queryUserCourseRelation();

        LinearLayout descriptionTab = findViewById(R.id.courseDescriptionTab);
        setOnClickListener(descriptionTab, CourseDescriptionActivity.class);

        LinearLayout instructorTab = findViewById(R.id.courseInstructorTab);
        setOnClickListener(instructorTab, CourseInstructorsActivity.class);

        LinearLayout groupTab = findViewById(R.id.courseGroupTab);
        setOnClickListener(groupTab, CourseGroupsActivity.class);

        LinearLayout ratingTab = findViewById(R.id.courseRatingTab);
        setOnClickListener(ratingTab, CourseRatingActivity.class);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        queryCourseDetails();
        queryUserCourseRelation();
    }

    private void renderActionBar() {
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Course Detail");
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

    private void initViews() {
        courseCode = findViewById(R.id.courseCode);
        courseName = findViewById(R.id.courseName);
        description = findViewById(R.id.courseDescription);


        credit = findViewById(R.id.credit);
        capacity = findViewById(R.id.capacity);
        seats = findViewById(R.id.seats);
        taken = findViewById(R.id.taken);

        faculty = findViewById(R.id.faculty);

        ratingStat = findViewById(R.id.ratingStat);
        ratingStat2 = findViewById(R.id.ratingStat2);
        reviewsStat = findViewById(R.id.reviewsStat);
        reviewsStat2 = findViewById(R.id.reviewsStat2);

        button = findViewById(R.id.button);

        ratingBar = findViewById(R.id.ratingBar);
        pb1 = findViewById(R.id.progressBar1);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);
        pb4 = findViewById(R.id.progressBar4);
        pb5 = findViewById(R.id.progressBar5);

    }

    private void initInstructorRecyclerView() {
        RecyclerView instructorRecyclerView = findViewById(R.id.courseInstructorsSummary);
        instructorRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager instructorLayoutManager = new LinearLayoutManager(this);
        instructorRecyclerView.setLayoutManager(instructorLayoutManager);
        instructorAdapter = new InstructorSimpleAdapter(instructorList);
        instructorRecyclerView.setAdapter(instructorAdapter);
    }

    private void initGroupRecyclerView() {
        RecyclerView groupRecyclerView = findViewById(R.id.courseGroupsSummary);
        groupRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager groupLayoutManager = new LinearLayoutManager(this);
        groupRecyclerView.setLayoutManager(groupLayoutManager);
        groupAdapter = new GroupAdapter(groupList);
        groupRecyclerView.setAdapter(groupAdapter);
    }

    private void setOnClickListener(View tab, final Class activity) {
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailsActivity.this, activity);
                intent.putExtra("courseCode", mCourseCode);
                startActivity(intent);
            }
        });
    }


    private void queryCourseDetails() {
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
                    description.setText(courseObject.getString("3"));

                    int capacityVal = courseObject.getInt("5");
                    int registered = courseObject.getInt("14");

                    credit.setText(String.format("%d", courseObject.getInt("4")));
                    capacity.setText(String.format("%d", capacityVal));

                    taken.setText(String.format("%d", courseObject.getInt("13")));
                    seats.setText(String.format("%d", capacityVal - registered));


                    int reviews = courseObject.getInt("7");
                    String rating = "-";
                    if (reviews > 0) {
                        rating = String.format("%.1f", courseObject.getDouble("6"));
                    }

                    ratingStat.setText(rating);
                    ratingStat2.setText(rating);
                    ratingBar.setRating((float) courseObject.getDouble("6"));

                    reviewsStat.setText(reviews + " reviews");
                    reviewsStat2.setText(reviews + " reviews");

                    if (reviews > 0) {
                        pb1.setProgress((int) (courseObject.getDouble("8") / reviews * 100));
                        pb2.setProgress((int) (courseObject.getDouble("9") / reviews * 100));
                        pb3.setProgress((int) (courseObject.getDouble("10") / reviews * 100));
                        pb4.setProgress((int) (courseObject.getDouble("11") / reviews * 100));
                        pb5.setProgress((int) (courseObject.getDouble("12") / reviews * 100));
                    } else {
                        pb1.setProgress(0);
                        pb2.setProgress(0);
                        pb3.setProgress(0);
                        pb4.setProgress(0);
                        pb5.setProgress(0);
                    }
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "WITH rating as ( " +
                "   SELECT " +
                "       course, " +
                "       AVG(rating) AS rating, " +
                "       COUNT(rating) AS count, " +
                "       SUM(CASE WHEN rating = 1 THEN 1 ELSE 0 END) AS r1, " +
                "       SUM(CASE WHEN rating = 2 THEN 1 ELSE 0 END) AS r2, " +
                "       SUM(CASE WHEN rating = 3 THEN 1 ELSE 0 END) AS r3, " +
                "       SUM(CASE WHEN rating = 4 THEN 1 ELSE 0 END) AS r4, " +
                "       SUM(CASE WHEN rating = 5 THEN 1 ELSE 0 END) AS r5" +
                "   FROM review " +
                "   GROUP BY course), " +
                "registration_count as ( " +
                "   SELECT " +
                "   course, " +
                "   SUM(CASE WHEN semester = '2019/2020' THEN 0 ELSE 1 END) AS taken, " +
                "   SUM(CASE WHEN semester = '2019/2020' THEN 1 ELSE 0 END) AS registered " +
                "   FROM registration " +
                "   WHERE course = '" + mCourseCode + "')" +
                "SELECT " +
                "   A.id, " +   // 0
                "   A.name, " +   // 1
                "   B.name, " +         // 2
                "   A.description, " +  // 3
                "   A.credit, " +       // 4
                "   A.capacity, " +     // 5
                "   IFNULL(C.rating, 0.0), " +       // 6
                "   IFNULL(C.count, 0), " +        // 7
                "   IFNULL(C.r1, 0), " +           // 8
                "   IFNULL(C.r2, 0), " +           // 9
                "   IFNULL(C.r3, 0), " +           // 10
                "   IFNULL(C.r4, 0), " +           // 11
                "   IFNULL(C.r5, 0), " +           // 12
                "   IFNULL(D.taken, 0), " +        // 13
                "   IFNULL(D.registered, 0) " +    // 14
                "FROM course A " +
                "JOIN faculty B " +
                "   ON A.faculty = B.id " +
                "LEFT JOIN rating C " +
                "   ON A.id = C.course " +
                "LEFT JOIN registration_count D " +
                "   ON A.id = D.course " +
                "WHERE A.id = '" + mCourseCode + "'";

        Database.sendQuery(this, query, listener, error);
    }

    private void queryGroupList() {
        loading.show();
        queryCounter++;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    HashMap<String, ArrayList<Slot>> groupMap = new HashMap<>();
                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
                        JSONObject entry = data.getJSONObject(i);
                        String groupId = entry.getString("0");
                        if (!groupMap.containsKey(groupId)) {
                            groupMap.put(groupId, new ArrayList<Slot>());
                        }
                        Slot slot = new Slot(
                                entry.getString("1"),
                                entry.getInt("2"),
                                entry.getInt("3"),
                                entry.getInt("4")
                        );
                        groupMap.get(groupId).add(slot);
                    }

                    groupList.clear();
                    for (Object key : groupMap.keySet().toArray()) {
                        String id = (String) key;
                        groupList.add(new Group(id, groupMap.get(id)));
                    }

                    groupAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "SELECT " +
                "   A.group_id, " +            // 0
                "   B.type, " +               // 1
                "   B.time_start, " +          // 2
                "   B.time_end, " +            // 3
                "   B.day " +                // 4
                "FROM course_group A " +
                "JOIN slot B " +
                "   ON A.slot = B.id " +
                "WHERE A.course = '" + mCourseCode + "'";

        Database.sendQuery(this, query, listener, error);
    }

    private void queryInstructorList() {
        loading.show();
        queryCounter++;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    instructorList.clear();
                    JSONObject entry = data.getJSONObject(0);
                    instructorList.add(new Instructor(
                            entry.getString("0")
                    ));
                    queryCoInstructorList();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "SELECT B.name " +
                "FROM course_instructor A " +
                "JOIN instructor B " +
                "   ON A.instructor = B.id " +
                "WHERE A.course = '" + mCourseCode + "' AND lead = 1";

        Database.sendQuery(this, query, listener, error);
    }

    private void queryCoInstructorList() {
        loading.show();
        queryCounter++;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
                        JSONObject entry = data.getJSONObject(i);
                        instructorList.add(new Instructor(
                                entry.getString("0")
                        ));
                    }
                    instructorAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "SELECT B.name " +
                "FROM course_instructor A " +
                "JOIN instructor B " +
                "   ON A.instructor = B.id " +
                "WHERE A.course = '" + mCourseCode + "' AND lead = 0";

        Database.sendQuery(this, query, listener, error);
    }

    private void queryUserCourseRelation() {
        loading.show();
        queryCounter++;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    if (resp.getInt("rowCount") > 0) {
                        button.setText("Review this course");
                        setOnClickListener(button, CourseRatingFormActivity.class);
                    } else {
                        button.setText("Register for this course");
                        setOnClickListener(button, CourseGroupsActivity.class);
                    }
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "SELECT A.semester " +
                "FROM registration A " +
                "JOIN user B " +
                "   ON A.user = B.id " +
                "WHERE A.course = '" + mCourseCode + "' AND B.email = '" + Preferences.getLogin(this).split("/")[1] + "'";

        Database.sendQuery(this, query, listener, error);
    }

}
