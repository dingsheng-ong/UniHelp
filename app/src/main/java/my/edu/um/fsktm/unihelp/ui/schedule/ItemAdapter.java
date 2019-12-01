package my.edu.um.fsktm.unihelp.ui.schedule;

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

//    private View.OnClickListener openLocation(final Item location) {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, RoomDetailActivity.class);
//                intent.putExtra("location", location);
//                context.startActivity(intent);
//            }
//        };
//    }

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

//        root.setOnClickListener(openLocation(item));

        TextView title = root.findViewById(R.id.schedule_item_title);
        TextView description = root.findViewById(R.id.schedule_item_description);

        title.setText(item.getTitle());
        description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return database.size();
    }
}
