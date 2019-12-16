package my.edu.um.fsktm.unihelp.models;

import java.io.Serializable;

public class Review implements Serializable {
    private int rating;
    private String comment;

    public Review(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
