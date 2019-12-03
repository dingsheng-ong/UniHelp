package my.edu.um.fsktm.unihelp.ui.reservation;


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
import my.edu.um.fsktm.unihelp.models.Location;
import my.edu.um.fsktm.unihelp.ui.reservation.adapters.LocationAdapter;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.RandomIconGenerator;

public class ReservationFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Location> database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reserve_fragment, container, false);

        recyclerView = view.findViewById(R.id.reservation_list);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        database = new ArrayList<>();
        queryListOfLocation();
        adapter = new LocationAdapter(getContext(), database);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void queryListOfLocation() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    JSONArray data = resp.getJSONArray("data");
                    database.clear();
                    for (int i = 0; i < resp.getInt("rowCount"); i++) {
                        JSONObject entry = data.getJSONObject(i);
                        database.add(new Location(
                            entry.getString("0"),
                            entry.getString("1"),
                            entry.getString("2"),
                            entry.getInt("3"),
                            RandomIconGenerator.getResId(entry.getString("0"))
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("RF 73", e.toString());
                }
            }
        };
        String query = "WITH reservationCount AS (\n" +
                       "    SELECT A.locationId,\n" +
                       "           COUNT(A.locationId) AS count\n" +
                       "      FROM location A\n" +
                       "      JOIN reservation B\n" +
                       "        ON A.locationId = B.locationId\n" +
                       "     GROUP BY A.locationId\n" +
                       ") SELECT A.locationId,\n" +
                       "         A.name,\n" +
                       "         C.name,\n" +
                       "         count\n" +
                       "    FROM location A\n" +
                       "    JOIN reservationCount B\n" +
                       "      ON A.locationId = B.locationId\n" +
                       "    JOIN faculty C\n" +
                       "      ON A.facultyId = C.facultyId\n" +
                       "   ORDER BY count DESC";
        Database.sendQuery(getActivity(), query, listener);
    }

}
