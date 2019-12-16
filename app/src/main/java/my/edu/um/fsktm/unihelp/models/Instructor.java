package my.edu.um.fsktm.unihelp.models;

import java.io.Serializable;

import my.edu.um.fsktm.unihelp.models.Faculty;

public class Instructor implements Serializable {
    private String name, email, department, image, faculty;

    public Instructor(String email, String name, String department, String faculty) {
        this.email = email;
        this.name = name;
        this.department = department;
        this.faculty = faculty;
    }

    public Instructor(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getName() {
        return name;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
