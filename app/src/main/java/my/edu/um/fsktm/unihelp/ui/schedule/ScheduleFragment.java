package my.edu.um.fsktm.unihelp.ui.schedule;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.util.RandomIconGenerator;

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

        List<Item> database = queryListOfLocation();
        adapter = new ItemAdapter(getContext(), database);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Item> queryListOfLocation() {
        // TO-DO: query db
        List<Item> database = new ArrayList<>();
        database.add(new Item(
                "class",
                "WIX3004 Mobile Application Development - Lecture",
                "Class!",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "DK1, FSKTM",
                "Dr. Ong Sim Ying"
        ));
        database.add(new Item(
                "class",
                "WIX3004 Mobile Application Development - Lab",
                "Class!",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "DK1, FSKTM",
                "Dr. Ong Sim Ying"
        ));
        database.add(new Item(
                "event",
                "Industrial Training Talk 2019/2020",
                "Class!",
                new Timestamp(new GregorianCalendar(2019,12,1,16,0).getTimeInMillis()),
                new Timestamp(new GregorianCalendar(2019,12,1,17,0).getTimeInMillis()),
                "The Cube, FSKTM",
                null
        ));
        return database;
    }

}
