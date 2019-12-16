package my.edu.um.fsktm.unihelp.ui.course.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.Preferences;

public class CourseRatingFormActivity extends AppCompatActivity {
    private TextView courseCode, courseName, faculty;
    private RatingBar ratingBar;
    private EditText review;
    private String mCourseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_rating_form_layout);

        renderActionBar();

        mCourseCode = getIntent().getStringExtra("courseCode");

        initViews();
        queryCourseDescription();
        queryUserReview();
    }


    private void initViews() {
        courseCode = findViewById(R.id.courseCode);
        courseName = findViewById(R.id.courseName);
        faculty = findViewById(R.id.faculty);

        ratingBar = findViewById(R.id.ratingBar);
        review = findViewById(R.id.review);

        Button submitButton = findViewById(R.id.button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReview();
            }
        });
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
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
            }
        };

        String query = "SELECT " +
                "   A.id, " +   // 0
                "   A.name, " + // 1
                "   B.name " +  // 2
                "FROM course A " +
                "JOIN faculty B " +
                "   ON A.faculty = B.id " +
                "WHERE A.id = '" + mCourseCode + "'";

        Database.sendQuery(this, query, listener);
    }

    private void queryUserReview() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    if (resp.getInt("rowCount") > 0) {
                        JSONArray data = resp.getJSONArray("data");
                        JSONObject reviewObject = data.getJSONObject(0);
                        ratingBar.setRating((float) reviewObject.getInt("0"));
                        review.setText(reviewObject.getString("1"));
                    }
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
            }
        };

        String query = "SELECT " +
                "   A.rating, " +   // 0
                "   A.comment " +   // 1
                "FROM review A " +
                "JOIN user B " +
                "   ON A.user = B.id " +
                "WHERE A.course = '" + mCourseCode + "' AND B.email = '" + Preferences.getLogin(this) + "'";

        Database.sendQuery(this, query, listener);
    }

    private void createReview() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                finish();
            }
        };
        int ratingVal = (int) ratingBar.getRating();
        String commentVal = review.getText().toString();
        String query = String.format("INSERT OR REPLACE INTO review (course, user, rating, comment) VALUES ('%s', (SELECT id FROM user WHERE email = '%s'), %d, '%s')",
                mCourseCode,
                Preferences.getLogin(this),
                ratingVal,
                commentVal);

        Database.sendQuery(this, query, listener);
    }
}
