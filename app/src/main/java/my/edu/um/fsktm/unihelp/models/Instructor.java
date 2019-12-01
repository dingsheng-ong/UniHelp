package my.edu.um.fsktm.unihelp.models;

import java.io.Serializable;

import my.edu.um.fsktm.unihelp.models.Faculty;

public class Instructor implements Serializable {
    private String name, email, department, image;
    private Faculty faculty;

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

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
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
