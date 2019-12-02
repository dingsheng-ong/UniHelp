package my.edu.um.fsktm.unihelp.ui.schedule;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.ScheduleItem;
import my.edu.um.fsktm.unihelp.ui.schedule.adapters.ScheduleAdapter;

public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);

        recyclerView = view.findViewById(R.id.schedule_item_list);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<ScheduleItem> database = queryListOfItems();
        adapter = new ScheduleAdapter(getContext(), database);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<ScheduleItem> queryListOfItems() {
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
