package my.edu.um.fsktm.unihelp.ui.course.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import my.edu.um.fsktm.unihelp.models.Instructor;
import my.edu.um.fsktm.unihelp.models.Review;
import my.edu.um.fsktm.unihelp.ui.course.adapters.InstructorAdapter;
import my.edu.um.fsktm.unihelp.ui.course.adapters.ReviewAdapter;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.Preferences;

public class CourseRatingActivity extends AppCompatActivity {
    private TextView courseCode, courseName, faculty, ratingStat, reviewStat;
    private RatingBar ratingBar;
    private ProgressBar pb1, pb2, pb3, pb4, pb5;
    private RecyclerView.Adapter adapter;
    private Button button;
    private String mCourseCode;
    private ArrayList<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_rating_layout);

        renderActionBar();

        mCourseCode = getIntent().getStringExtra("courseCode");
        reviewList = new ArrayList<>();

        initViews();
        queryCourseDetails();
        queryReviewList();
        queryUserCourseRegistered();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        queryCourseDetails();
        queryReviewList();
        queryUserCourseRegistered();
    }

    private void initViews() {
        courseCode = findViewById(R.id.courseCode);
        courseName = findViewById(R.id.courseName);
        faculty = findViewById(R.id.faculty);

        ratingStat = findViewById(R.id.ratingStat);
        reviewStat = findViewById(R.id.reviewsStat);
        ratingBar = findViewById(R.id.ratingBar);

        pb1 = findViewById(R.id.progressBar1);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);
        pb4 = findViewById(R.id.progressBar4);
        pb5 = findViewById(R.id.progressBar5);

        adapter = new ReviewAdapter(reviewList);
        initRecyclerView(R.id.courseReviewsList, adapter);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseRatingActivity.this, CourseRatingFormActivity.class);
                intent.putExtra("courseCode", mCourseCode);
                startActivity(intent);
            }
        });
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
            actionBar.setTitle("Course Reviews");
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

    private void queryCourseDetails() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    JSONObject courseObject = data.getJSONObject(0);

                    courseCode.setText(courseObject.getString("0"));
                    courseName.setText(courseObject.getString("1"));
                    faculty.setText(courseObject.getString("2"));

                    int reviews = courseObject.getInt("4");
                    String rating = "-";
                    if (reviews > 0) {
                        rating = String.format("%.1f", courseObject.getDouble("3"));
                    }

                    ratingStat.setText(rating);
                    ratingBar.setRating((float) courseObject.getDouble("3"));

                    reviewStat.setText(reviews + " reviews");

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
                "   GROUP BY course) " +
                "SELECT " +
                "   A.id, " +   // 0
                "   A.name, " +   // 1
                "   B.name, " +         // 2
                "   C.rating, " +       // 3
                "   C.count, " +        // 4
                "   C.r1, " +           // 5
                "   C.r2, " +           // 6
                "   C.r3, " +           // 7
                "   C.r4, " +           // 8
                "   C.r5 " +            // 9
                "FROM course A " +
                "JOIN faculty B " +
                "   ON A.faculty = B.id " +
                "JOIN rating C " +
                "   ON A.id = C.course " +
                "WHERE A.id = '" + mCourseCode + "'";

        Database.sendQuery(this, query, listener);
    }

    private void queryReviewList() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    reviewList.clear();

                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
                        JSONObject entry = data.getJSONObject(i);
                        reviewList.add(new Review(
                                entry.getInt("0"),
                                entry.getString("1")
                        ));
                    }

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
            }
        };

        String query = "SELECT rating, comment FROM review WHERE course = '" + mCourseCode + "'";

        Database.sendQuery(this, query, listener);
    }

    private void queryUserCourseRegistered() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    if (resp.getInt("rowCount") == 0) {
                        button.setVisibility(View.GONE);
                    } else {
                        button.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
            }
        };

        String query = "SELECT semester " +
                "FROM registration A " +
                "JOIN user B " +
                "   ON A.user = B.id " +
                "WHERE A.course = '" + mCourseCode + "' AND B.email = '" + Preferences.getLogin(this) + "'";

        Database.sendQuery(this, query, listener);
    }
}
