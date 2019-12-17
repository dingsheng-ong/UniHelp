package my.edu.um.fsktm.unihelp.ui.reservation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Location;
import my.edu.um.fsktm.unihelp.ui.reservation.activities.RoomDetailActivity;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder>
    implements Filterable {

    private List<Location> database;
    private List<Location> filtered;
    private Context context;

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        View view;
        LocationViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    public LocationAdapter(Context context, List<Location> database) {
        this.context = context;
        this.database = database;
        this.filtered = database;
    }

    private View.OnClickListener openLocation(final Location location) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RoomDetailActivity.class);
                intent.putExtra("location", location);
                context.startActivity(intent);
            }
        };
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.reserve_item,
            parent,
            false
        );
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        Location item = filtered.get(position);
        View root = holder.view;

        root.setOnClickListener(openLocation(item));

        ImageView icon = root.findViewById(R.id.location_icon);
        TextView name = root.findViewById(R.id.location_name);
        TextView faculty = root.findViewById(R.id.location_fac);
        TextView count = root.findViewById(R.id.location_reserve_count);

        icon.setImageResource(item.getIconResID());
        name.setText(item.getName());
        faculty.setText(item.getFaculty());
        count.setText(String.format(Locale.US, "%d reservations", item.getReservations()));
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString();
                if (query.isEmpty()) {
                    filtered = database;
                } else {
                    List<Location> _filtered = new ArrayList<>();
                    for (Location location: database) {
                        boolean matchFaculty = location.getFaculty().toLowerCase().contains(query.toLowerCase());
                        boolean matchName = location.getName().toLowerCase().contains(query.toLowerCase());
                        if (matchFaculty || matchName) {
                            _filtered.add(location);
                        }
                    }
                    filtered = _filtered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (List<Location>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
