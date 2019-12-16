package my.edu.um.fsktm.unihelp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {
    private String courseCode, courseName, description, learningOutcome;
    private int capacity, seats, taken, credits;
    private String faculty;
    private String leadInstructor;
    private double rating;
    private int reviews;

    public Course(String courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public Course(String courseCode, String courseName, String faculty, String leadInstructor, int capacity, int credits, int reviews, double rating) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.faculty = faculty;
        this.leadInstructor = leadInstructor;
        this.capacity = capacity;
        this.credits = credits;
        this.rating = rating;
        this.reviews = reviews;
    }

    public void setLearningOutcome(String learningOutcome) {
        this.learningOutcome = learningOutcome;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public void setLeadInstructor(String leadInstructor) {
        this.leadInstructor = leadInstructor;
    }

    public String getLearningOutcome() {
        return learningOutcome;
    }

    public int getTaken() {
        return taken;
    }

    public int getSeats() {
        return seats;
    }

    public int getCredits() {
        return credits;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public double getRating() {
        return rating;
    }

    public int getReviews() {
        return reviews;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getLeadInstructor() {
        return leadInstructor;
    }

}
