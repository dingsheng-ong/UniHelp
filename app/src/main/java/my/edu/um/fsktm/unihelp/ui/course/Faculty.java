package my.edu.um.fsktm.unihelp.ui.course;

import java.io.Serializable;

public class Faculty implements Serializable {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
