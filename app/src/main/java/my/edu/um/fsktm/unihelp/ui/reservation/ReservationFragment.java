package my.edu.um.fsktm.unihelp.ui.reservation;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.util.RandomIconGenerator;

public class ReservationFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reserve_fragment, container, false);

        recyclerView = view.findViewById(R.id.reservation_list);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<Location> database = queryListOfLocation();
        adapter = new LocationAdapter(getContext(), database);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Location> queryListOfLocation() {
        // TO-DO: query db
        List<Location> database = new ArrayList<>();
        database.add(new Location(
                "MM1",
                "Makmal Mikro 1",
                "Faculty of Computer Science and Information Technology",
                4.2,
                104,
                RandomIconGenerator.getResId("MM1")
        ));
        database.add(new Location(
                "MM2",
                "Makmal Mikro 2",
                "Faculty of Computer Science and Information Technology",
                4.4,
                120,
                RandomIconGenerator.getResId("MM2")
        ));
        database.add(new Location(
                "DK1",
                "Lecture Hall 1",
                "Faculty of Computer Science and Information Technology",
                2.4,
                25,
                RandomIconGenerator.getResId("DK1")
        ));
        database.add(new Location(
                "MM1",
                "Makmal Mikro 1",
                "Faculty of Computer Science and Information Technology",
                4.2,
                104,
                RandomIconGenerator.getResId("MM1")
        ));
        database.add(new Location(
                "MM2",
                "Makmal Mikro 2",
                "Faculty of Computer Science and Information Technology",
                4.4,
                120,
                RandomIconGenerator.getResId("MM2")
        ));
        database.add(new Location(
                "DK1",
                "Lecture Hall 1",
                "Faculty of Computer Science and Information Technology",
                2.4,
                25,
                RandomIconGenerator.getResId("DK1")
        ));
        database.add(new Location(
                "MM1",
                "Makmal Mikro 1",
                "Faculty of Computer Science and Information Technology",
                4.2,
                104,
                RandomIconGenerator.getResId("MM1")
        ));
        database.add(new Location(
                "MM2",
                "Makmal Mikro 2",
                "Faculty of Computer Science and Information Technology",
                4.4,
                120,
                RandomIconGenerator.getResId("MM2")
        ));
        database.add(new Location(
                "DK1",
                "Lecture Hall 1",
                "Faculty of Computer Science and Information Technology",
                2.4,
                25,
                RandomIconGenerator.getResId("DK1")
        ));
        return database;
    }

}
