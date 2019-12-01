package my.edu.um.fsktm.unihelp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {
    private String courseCode, courseName, description, learningOutcome;
    private int capacity, seats, taken, credits;
    private Faculty faculty;
    private ArrayList<Instructor> instructors;
    private ArrayList<Group> groups;
    private ArrayList<Review> reviews;
    private ArrayList<Course> prerequisite;

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setInstructors(ArrayList<Instructor> instructors) {
        this.instructors = instructors;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public void setLearningOutcome(String learningOutcome) {
        this.learningOutcome = learningOutcome;
    }

    public void setPrerequisite(ArrayList<Course> prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public String getLearningOutcome() {
        return learningOutcome;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSeats() {
        return seats;
    }

    public int getTaken() {
        return taken;
    }

    public int getCredits() {
        return credits;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public ArrayList<Instructor> getInstructors() {
        return instructors;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public ArrayList<Course> getPrerequisite() {
        return prerequisite;
    }

    public float getAverageRating() {
        float avg = 0;
        for (int i = 0; i < reviews.size(); i++) {
            avg += reviews.get(i).getRating();
        }
        avg /= (float) reviews.size();
        return avg;
    }

    public int[] getRatingPercentage() {
        float[] percentages = new float[5];
        for (int i = 0; i < 5; i++) percentages[i] = 0;
        for (int i = 0; i < reviews.size(); i++) percentages[reviews.get(i).getRating() - 1]++;
        for (int i = 0; i < 5; i++) percentages[i] /= reviews.size();
        int[] _p = new int[5];
        for (int i = 0; i < 5; i++) _p[i] = (int) (percentages[i] * 100);
        return _p;
    }
}
