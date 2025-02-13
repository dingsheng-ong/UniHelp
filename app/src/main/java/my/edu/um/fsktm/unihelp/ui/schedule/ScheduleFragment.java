package my.edu.um.fsktm.unihelp.ui.schedule;


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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.ScheduleItem;
import my.edu.um.fsktm.unihelp.ui.schedule.adapters.ScheduleAdapter;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.LoadingScreen;
import my.edu.um.fsktm.unihelp.util.Preferences;
import my.edu.um.fsktm.unihelp.util.RandomIconGenerator;

public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    List<ScheduleItem> database;
    private int queryCount;
    private AlertDialog loading;
    private int queryCounter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);
        ((Toolbar) getActivity().findViewById(R.id.main_toolbar)).getMenu().clear();
        loading = LoadingScreen.build(getActivity());
        recyclerView = view.findViewById(R.id.schedule_item_list);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // Dummy
//        database = queryListOfDummyItems();
        database = new ArrayList<>();
        queryListOfItems();
        adapter = new ScheduleAdapter(getContext(), database);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void queryListOfItems() {
        loading.show();
        queryCounter += 3;

        Response.Listener<JSONObject> eventListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
//                    database.clear();
                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
                        JSONObject entry = data.getJSONObject(i);

                        database.add(new ScheduleItem(
                                "event",
                                entry.getString("0"), //title
                                entry.getString("1"), //desc
                                Timestamp.valueOf(entry.getString("2")), //start
                                Timestamp.valueOf(entry.getString("3")), //end
                                entry.getString("4"), //venue
                                ""  //lect
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                } finally {
                    queryDone();
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };


        Response.Listener<JSONObject> bookingListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
//                    database.clear();
                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
                        JSONObject entry = data.getJSONObject(i);

                        database.add(new ScheduleItem(
                                "booking",
                                "", //title
                                "", //desc
                                Timestamp.valueOf(entry.getString("0")), //start
                                Timestamp.valueOf(entry.getString("1")), //end
                                entry.getString("2"), //venue
                                ""  //lect
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                } finally {
                    queryDone();
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };

        Response.Listener<JSONObject> classListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
//                    database.clear();
                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
//                        A.id, A.name, D.type, D.day, D.time_start, D.time_end, D.location, F.name
                        JSONObject entry = data.getJSONObject(i);
                        Calendar startDate = Calendar.getInstance();
                        Calendar endDate = Calendar.getInstance();

                        int dayDiff = startDate.DAY_OF_WEEK - Integer.parseInt(entry.getString("3"));
                        dayDiff = (dayDiff < 0) ? (7-dayDiff) : (dayDiff);

                        startDate.add(Calendar.DAY_OF_YEAR, dayDiff);
                        startDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(entry.getString("4")));
                        startDate.set(Calendar.MINUTE,0);
                        startDate.set(Calendar.SECOND,0);
                        startDate.set(Calendar.MILLISECOND,0);

                        endDate.add(Calendar.DAY_OF_YEAR, dayDiff);
                        endDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(entry.getString("5")));
                        endDate.set(Calendar.MINUTE,0);
                        endDate.set(Calendar.SECOND,0);
                        endDate.set(Calendar.MILLISECOND,0);
                        database.add(new ScheduleItem(
                                "class",
                                entry.getString("0") + " " + entry.getString("1")
                                        + " - " + entry.getString("2"), //title
                                "", //desc
                                new Timestamp(startDate.getTimeInMillis()), //start
                                new Timestamp(endDate.getTimeInMillis()), //end
                                entry.getString("6"), //venue
                                entry.getString("7")  //lect
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                } finally {
                    queryDone();
                }
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_SHORT).show();
                queryCounter--;
                if (queryCounter == 0)
                    loading.dismiss();
            }
        };
        String user = Preferences.getLogin(getActivity()).split("/")[0];
        String eventQuery = "SELECT name,description,time_start,time_end,location FROM event WHERE time_start >= date('now')";
        String bookingQuery = String.format(
            Locale.US,
            "SELECT time_start, time_end, location FROM reservation WHERE user = '%s' AND time_start >= date('now')",
            user
        );
        String classQuery = "WITH user_courses AS (SELECT course, group_id FROM registration WHERE user = '%s')\n" +
                "SELECT A.id, A.name, D.type, D.day, D.time_start, D.time_end, D.location, F.name FROM course A \n" +
                "  INNER JOIN user_courses B ON B.course = A.id\n" +
                "  INNER JOIN course_group C ON A.id = C.course AND B.group_id = C.group_id\n" +
                "  INNER JOIN slot D ON C.slot = D.id\n" +
//                "  INNER JOIN course_instructor E ON A.id = E.course WHERE E.lead=1\n" +
                "  INNER JOIN instructor F ON D.instructor = F.id\n"
//                "  WHERE D.time_start >= date('now')"
                ;
        classQuery = String.format(Locale.US, classQuery, user);
        database.clear();
        queryCount = 0;
        Database.sendQuery(getActivity(), eventQuery, eventListener, error);
        Database.sendQuery(getActivity(), bookingQuery, bookingListener, error);
        Database.sendQuery(getActivity(), classQuery, classListener, error);

    }

    private void queryDone() {
        queryCount ++;
        if (queryCount < 3) {
            return;
        }

        // sort
        Collections.sort(database, new Comparator<ScheduleItem>() {
            @Override
            public int compare(ScheduleItem scheduleItem, ScheduleItem t1) {
                return scheduleItem.getTimeStart().compareTo(t1.getTimeStart());
            }
        });

        // add date divider
        String curDate = "";
        for (int i=0; i < database.size(); i++) {
            Date date = database.get(i).getTimeStart();
            String dateStr = new SimpleDateFormat("E, dd MMM").format(date);
            if (!curDate.equals(dateStr)) {
                database.add(i, new ScheduleItem(
                        "divider",
                        dateStr,
                        "",
                        new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                        new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                        "",
                        ""

                ));
                curDate = dateStr;
            }
        }

        // add final padding
        database.add(new ScheduleItem(
                "divider",
                "",
                "",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "",
                ""
        ));

    }

    private List<ScheduleItem> queryListOfDummyItems() {
        // TO-DO: query db
        List<ScheduleItem> database = new ArrayList<>();
        database.add(new ScheduleItem(
                "divider",
                "25th",
                "",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "",
                ""
        ));
        database.add(new ScheduleItem(
                "class",
                "WIX3004 Mobile Application Development - Lecture",
                "",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "DK1, FSKTM",
                "Dr. Ong Sim Ying"
        ));
        database.add(new ScheduleItem(
                "class",
                "WIX3004 Mobile Application Development - Lab",
                "",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "DK1, FSKTM",
                "Dr. Ong Sim Ying"
        ));
        database.add(new ScheduleItem(
                "divider",
                "26th",
                "",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "",
                ""
        ));
        database.add(new ScheduleItem(
                "event",
                "Industrial Training Talk 2019/2020",
                "EVERY SINGLE STUDENT MUST JOIN or cannot graduate!!!!!!",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "The Cube, FSKTM",
                null
        ));
        database.add(new ScheduleItem(
                "booking",
                "",
                "",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "Student Center's Discussion Room",
                null
        ));
        // ADD EMPTY SPACE AT END
        database.add(new ScheduleItem(
                "divider",
                "",
                "",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "",
                ""
        ));
        return database;
    }

}
