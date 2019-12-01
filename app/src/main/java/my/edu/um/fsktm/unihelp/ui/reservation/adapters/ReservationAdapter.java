package my.edu.um.fsktm.unihelp.ui.reservation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Reservation;

public class ReservationAdapter extends ArrayAdapter<Reservation> {

    private Reservation[] database;
    Context context;

    public ReservationAdapter(Context context, Reservation[] database) {
        super(context, R.layout.reserve_timeline_item, database);
        this.database = database;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reservation reservation = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                R.layout.reserve_timeline_item,
                parent,
                false
            );
        }
        convertView.setTag(reservation.getStatus());
        TextView tag = convertView.findViewById(R.id.detail_timeline_tag);
        View block = convertView.findViewById(R.id.detail_timeline_block);

        tag.setText(reservation.getTime());
        switch (reservation.getStatus()) {
            case NOT_RESERVE:
                break;
            case USER_RESERVE:
                block.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                break;
            case NON_USER_RESERVE:
                block.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
                break;
        }
        return convertView;
    }
}
