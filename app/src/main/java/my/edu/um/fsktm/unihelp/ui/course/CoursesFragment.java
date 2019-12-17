package my.edu.um.fsktm.unihelp.ui.course;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

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
import my.edu.um.fsktm.unihelp.util.LoadingScreen;
import my.edu.um.fsktm.unihelp.util.RandomIconGenerator;

public class CoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CourseAdapter adapter;
    private ArrayList<Course> courseList;
    private AlertDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragment, container, false);
//        ((Toolbar) getActivity().findViewById(R.id.main_toolbar)).getMenu().clear();
        Toolbar actionBar = getActivity().findViewById(R.id.main_toolbar);
        setupSearchActionBar(actionBar);
        // setup loading screen
        loading = LoadingScreen.build(getActivity());
        courseList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.courseList);
        renderRecyclerView();
        queryListOfCourses();
        return view;
    }

    private void setupSearchActionBar(Toolbar actionBar) {
        actionBar.getMenu().clear();
        actionBar.inflateMenu(R.menu.reserve_search);
        SearchView searchView = (SearchView) actionBar.getMenu().findItem(R.id.search_reservation).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void renderRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CourseAdapter(courseList);
        recyclerView.setAdapter(adapter);
    }

    private void queryListOfCourses() {
        loading.show();
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
                loading.dismiss();
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
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

        Database.sendQuery(getActivity(), query, listener, error);
    }

}
