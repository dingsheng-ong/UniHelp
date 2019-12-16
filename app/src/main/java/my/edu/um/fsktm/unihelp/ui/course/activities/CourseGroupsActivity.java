package my.edu.um.fsktm.unihelp.ui.course.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Group;
import my.edu.um.fsktm.unihelp.models.Slot;
import my.edu.um.fsktm.unihelp.models.TimetableCell;
import my.edu.um.fsktm.unihelp.ui.course.adapters.SlotAdapter;
import my.edu.um.fsktm.unihelp.ui.course.adapters.TimetableRowAdapter;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.LoadingScreen;
import my.edu.um.fsktm.unihelp.util.Preferences;

public class CourseGroupsActivity extends AppCompatActivity {
    private TextView courseCode, courseName, faculty;
    private ArrayAdapter<String> spinnerAdapter;
    private RecyclerView.Adapter timetableAdapter, slotAdapter;
    private Spinner spinner;
    private Button button;
    private ArrayList<Group> groupList;
    private ArrayList<Slot> slotList;
    private TimetableCell[][] timetable;
    private String mCourseCode;
    private Group selectedGroup;
    private AlertDialog loading;
    private int queryCounter = 0;
    private Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(CourseGroupsActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
            queryCounter--;
            if (queryCounter == 0)
                loading.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_groups_layout);

        mCourseCode = getIntent().getStringExtra("courseCode");
        groupList = new ArrayList<>();
        slotList = new ArrayList<>();
        timetable = new TimetableCell[5][14];
        loading = LoadingScreen.build(CourseGroupsActivity.this);

        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                timetable[i][j] = new TimetableCell(j + 8);
            }
        }

        renderActionBar();
        initViews();
        queryCourseDescription();
        queryGroupList();
        queryUserCourseRegistered();
    }

    private void initViews() {
        courseCode = findViewById(R.id.courseCode);
        courseName = findViewById(R.id.courseName);
        faculty = findViewById(R.id.faculty);
        spinner = findViewById(R.id.spinnerCourseGroups);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserToCourse();
            }
        });

        initSpinner();
        initTimetable();
        initSlotSummary();
    }

    private void initSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroup = groupList.get(position);
                if (selectedGroup != null) {
                    slotList.clear();
                    for (Slot slot : selectedGroup.getSlots()) {
                        slotList.add(slot);
                    }
                    slotAdapter.notifyDataSetChanged();
                    updateTimetable();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initTimetable() {
        RecyclerView recyclerView = findViewById(R.id.timetable);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        timetableAdapter = new TimetableRowAdapter(timetable);
        recyclerView.setAdapter(timetableAdapter);
    }

    private void initSlotSummary() {
        RecyclerView recyclerView = findViewById(R.id.slotSummary);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        slotAdapter = new SlotAdapter(slotList);
        recyclerView.setAdapter(slotAdapter);
    }


    private void renderActionBar() {
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Course Groups");
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

    private void updateTimetable() {
        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                timetable[i][j].clear();
            }
        }
        for (Slot slot : selectedGroup.getSlots()) {
            for (int i = slot.getTimeStart(); i < slot.getTimeEnd(); i++) {
                timetable[slot.getDay() - 1][i - 8].addSlot(slot);
                timetable[slot.getDay() - 1][i - 8].setPrimary(true);
            }
        }
        timetableAdapter.notifyDataSetChanged();
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
                "   A.id, " +   // 0
                "   A.name, " + // 1
                "   B.name " +  // 2
                "FROM course A " +
                "JOIN faculty B " +
                "   ON A.faculty = B.id " +
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
                        slot.setCourseCode(mCourseCode);
                        groupMap.get(groupId).add(slot);
                    }

                    groupList.clear();
                    spinnerAdapter.clear();
                    for (Object key : groupMap.keySet().toArray()) {
                        String id = (String) key;
                        groupList.add(new Group(id, groupMap.get(id)));
                        spinnerAdapter.add(String.format("Group %s", id));
                    }

                    spinnerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "SELECT " +
                "   group_id, " +   // 0
                "   type, " +       // 1
                "   time_start, " + // 2
                "   time_end, " +   // 3
                "   day " +         // 4
                "FROM course_group A " +
                "JOIN slot B " +
                "   ON A.slot = B.id " +
                "WHERE A.course = '" + mCourseCode + "'";

        Database.sendQuery(this, query, listener, error);
    }

    private void queryUserCourseRegistered() {
        loading.show();
        queryCounter++;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    if (resp.getInt("rowCount") > 0) {
                        button.setVisibility(View.INVISIBLE);
                    }  else {
                        button.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "SELECT semester " +
                "FROM registration A " +
                "JOIN user B " +
                "   ON A.user = B.id " +
                "WHERE A.course = '" + mCourseCode + "' AND B.email = '" + Preferences.getLogin(this) + "'";

        Database.sendQuery(this, query, listener, error);
    }

    private void registerUserToCourse() {
        loading.show();
        queryCounter++;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    finish();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        String query = "INSERT INTO registration (course, user, semester) " +
                "VALUES (" +
                "   '"+ mCourseCode+"', " +
                "   (SELECT id FROM user WHERE email = '" + Preferences.getLogin(this) + "'), " +
                "   '2019/2020')";

        Database.sendQuery(this, query, listener, error);
    }

}
