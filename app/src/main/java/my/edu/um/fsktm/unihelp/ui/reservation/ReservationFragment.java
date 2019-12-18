package my.edu.um.fsktm.unihelp.ui.reservation;


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
import my.edu.um.fsktm.unihelp.models.Location;
import my.edu.um.fsktm.unihelp.ui.reservation.adapters.LocationAdapter;
import my.edu.um.fsktm.unihelp.util.Database;
import my.edu.um.fsktm.unihelp.util.LoadingScreen;
import my.edu.um.fsktm.unihelp.util.RandomIconGenerator;

public class ReservationFragment extends Fragment {

    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Location> database;
    private AlertDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reserve_fragment, container, false);
        // Setup action bar
        Toolbar actionBar = getActivity().findViewById(R.id.main_toolbar);
        setupSearchActionBar(actionBar);

        loading = LoadingScreen.build(getActivity());

        recyclerView = view.findViewById(R.id.reservation_list);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        database = new ArrayList<>();
        queryListOfLocation();
        adapter = new LocationAdapter(getContext(), database);
        recyclerView.setAdapter(adapter);

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

    @Override
    public void onResume() {
        super.onResume();
        queryListOfLocation();
    }

    private void queryListOfLocation() {
        loading.show();
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
                loading.dismiss();
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                    getActivity(),
                    "Please try again!",
                    Toast.LENGTH_SHORT
                ).show();
                loading.dismiss();
            }
        };
        Database.sendQuery(getActivity(), QueryBuilder.reservationCount, listener, error);
    }

}
