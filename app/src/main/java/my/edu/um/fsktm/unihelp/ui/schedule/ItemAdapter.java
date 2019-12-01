package my.edu.um.fsktm.unihelp.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import my.edu.um.fsktm.unihelp.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> database;
    private Context context;

    //  View Holder
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        ItemViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    ItemAdapter(Context context, List<Item> database) {
        this.context = context;
        this.database = database;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.schedule_item,
            parent,
            false
        );
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = database.get(position);
        View root = holder.view;
        LinearLayout itemLayout = root.findViewById(R.id.schedule_item);
        TextView title = root.findViewById(R.id.schedule_item_title);
        TextView description = root.findViewById(R.id.schedule_item_description);

        // Formatting
        String descriptionText;
        switch(item.getType()) {
            case "class":
                itemLayout.setBackgroundColor(Color.parseColor("#3f51b5"));
                itemLayout.setPadding(30,30,30,30);
                title.setTextColor(Color.WHITE);
                description.setTextColor(Color.WHITE);
                descriptionText =
                    new SimpleDateFormat("h:mm a").format(item.getTimeStart()) + " - " +
                    new SimpleDateFormat("h:mm a").format(item.getTimeEnd()) + "\n" +
                    item.getVenue() + "\n" +
                    item.getLecturer();
                title.setText(item.getTitle());
                description.setText(descriptionText);
                break;
            case "event":
                itemLayout.setBackgroundColor(Color.parseColor("#00cc66"));
                itemLayout.setPadding(30,30,30,30);
                title.setTextColor(Color.WHITE);
                description.setTextColor(Color.WHITE);
                descriptionText =
                    new SimpleDateFormat("h:mm a").format(item.getTimeStart()) + " - " +
                    new SimpleDateFormat("h:mm a").format(item.getTimeEnd()) + "\n" +
                    item.getVenue() + "\n" +
                    item.getDescription();
                title.setText(item.getTitle());
                description.setText(descriptionText);
                break;
            case "booking":
                itemLayout.setBackgroundColor(Color.parseColor("#008fb3"));
                itemLayout.setPadding(30,30,30,30);
                title.setTextColor(Color.WHITE);
                description.setTextColor(Color.WHITE);
                descriptionText =
                    new SimpleDateFormat("h:mm a").format(item.getTimeStart()) + " - " +
                    new SimpleDateFormat("h:mm a").format(item.getTimeEnd());
                title.setText(item.getVenue());
                description.setText(descriptionText);
                break;
            case "divider":
                title.setTextColor(Color.GRAY);
                title.setTextSize(25);
                title.setText(item.getTitle());
                description.setVisibility(TextView.GONE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return database.size();
    }
}
