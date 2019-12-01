package my.edu.um.fsktm.unihelp.ui.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> reviewList;

    public ReviewAdapter(ArrayList<Review> reviewList) { this.reviewList = reviewList; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rating, comment;

        public ViewHolder(View view) {
            super(view);
            rating = view.findViewById(R.id.rating);
            comment = view.findViewById(R.id.comment);
        }
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View reviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_review_item, parent, false);
        return new ReviewAdapter.ViewHolder(reviewView );
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.rating.setText(Integer.toString(review.getRating()));
        holder.comment.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
