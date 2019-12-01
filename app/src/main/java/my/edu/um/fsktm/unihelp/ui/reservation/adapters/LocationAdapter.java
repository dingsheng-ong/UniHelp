package my.edu.um.fsktm.unihelp.ui.reservation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import my.edu.um.fsktm.unihelp.R;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<Location> database;
    private Context context;

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        View view;
        LocationViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    LocationAdapter(Context context, List<Location> database) {
        this.context = context;
        this.database = database;
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
        Location item = database.get(position);
        View root = holder.view;

        root.setOnClickListener(openLocation(item));

        ImageView icon = root.findViewById(R.id.location_icon);
        TextView name = root.findViewById(R.id.location_name);
        TextView faculty = root.findViewById(R.id.location_fac);
        TextView count = root.findViewById(R.id.location_reserve_count);
        TextView rating = root.findViewById(R.id.location_rating);

        icon.setImageResource(item.getIconResID());
        name.setText(item.getName());
        faculty.setText(item.getFaculty());
        count.setText(String.format(Locale.US, "%d reservations", item.getReservations()));
        rating.setText(String.format(Locale.US, "%.1f ★", item.getRating()));
    }

    @Override
    public int getItemCount() {
        return database.size();
    }
}
