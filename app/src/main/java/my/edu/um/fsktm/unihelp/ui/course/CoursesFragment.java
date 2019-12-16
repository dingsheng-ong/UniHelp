package my.edu.um.fsktm.unihelp.ui.course;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Course;
import my.edu.um.fsktm.unihelp.models.Faculty;
import my.edu.um.fsktm.unihelp.models.Group;
import my.edu.um.fsktm.unihelp.models.Instructor;
import my.edu.um.fsktm.unihelp.models.Location;
import my.edu.um.fsktm.unihelp.models.Review;
import my.edu.um.fsktm.unihelp.models.Slot;
import my.edu.um.fsktm.unihelp.ui.course.adapters.CourseAdapter;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.RandomIconGenerator;

public class CoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<Course> courseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragment, container, false);
        courseList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.courseList);
        renderRecyclerView();
        queryListOfCourses();
        return view;
    }

    private void renderRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CourseAdapter(courseList);
        recyclerView.setAdapter(adapter);
    }

    private void queryListOfCourses() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    courseList.clear();
                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
                        JSONObject entry = data.getJSONObject(i);
                        courseList.add(new Course(
                                entry.getString("0"),
                                entry.getString("1"),
                                entry.getString("2"),
                                entry.getString("3"),
                                entry.getInt("4"),
                                entry.getInt("5"),
                                entry.getInt("6"),
                                entry.getDouble("7")
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
            }
        };

        String query = "WITH lead_instructor AS (" +
                "   SELECT A.course, B.name " +
                "   FROM course_instructor A " +
                "   JOIN instructor B " +
                "       ON A.instructor = B.id " +
                "   JOIN faculty C " +
                "       ON B.faculty = C.id " +
                "   GROUP BY A.course), " +
                "rating AS (" +
                "   SELECT course, AVG(rating) AS rating, COUNT(rating) AS count " +
                "   FROM review " +
                "   GROUP BY course) " +
                "SELECT " +
                "   A.id, " +       // 0
                "   A.name, " +     // 1
                "   B.name, " +     // 2
                "   C.name, " +     // 3
                "   A.capacity, " + // 4
                "   A.credit, " +   // 5
                "   D.count, " +    // 6
                "   D.rating " +    // 7
                "FROM course A " +
                "JOIN faculty B " +
                "   ON A.faculty = B.id " +
                "JOIN lead_instructor C " +
                "   ON A.id = C.course " +
                "JOIN rating D " +
                "   ON A.id = D.course " +
                "ORDER BY A.id ASC";

        Database.sendQuery(getActivity(), query, listener);
    }

}
