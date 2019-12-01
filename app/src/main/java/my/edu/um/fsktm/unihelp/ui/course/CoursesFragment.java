package my.edu.um.fsktm.unihelp.ui.course;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import my.edu.um.fsktm.unihelp.R;
import my.edu.um.fsktm.unihelp.models.Course;
import my.edu.um.fsktm.unihelp.models.Faculty;
import my.edu.um.fsktm.unihelp.models.Group;
import my.edu.um.fsktm.unihelp.models.Instructor;
import my.edu.um.fsktm.unihelp.models.Review;
import my.edu.um.fsktm.unihelp.models.Slot;
import my.edu.um.fsktm.unihelp.ui.course.adapters.CourseAdapter;

public class CoursesFragment extends Fragment {

    private ArrayList<Course> courseList;

    public CoursesFragment() {
        courseList = new ArrayList<>();

        Course course = new Course();
        Course c1 = new Course();
        c1.setCourseCode("WIX1001");
        c1.setCourseName("Something Useless");
        Course c2 = new Course();
        c2.setCourseCode("WIX2002");
        c2.setCourseName("Probably Not Useful As Well");

        Faculty fsktm = new Faculty();
        fsktm.setName("Faculty of Computer Science and Information Technology");

        Faculty fs = new Faculty();
        fs.setName("Faculty of Science");

        ArrayList<Instructor> instructors = new ArrayList<>();
        Instructor ong = new Instructor();
        ong.setDepartment("Department of Software Engineering");
        ong.setEmail("ong@um.edu.my");
        ong.setFaculty(fsktm);
        ong.setName("Dr Ong Sim Ying");
        instructors.add(ong);

        Instructor einstein = new Instructor();
        einstein.setDepartment("Department of Physics");
        einstein.setEmail("einstein@um.edu.my");
        einstein.setFaculty(fs);
        einstein.setName("Albert Einstein");
        instructors.add(einstein);

        Slot lecture = new Slot();
        lecture.setType("lecture");
        lecture.setDay(1);
        lecture.setTimeStart(16, 0);
        lecture.setTimeEnd(18, 0);

        Slot tut1 = new Slot();
        tut1.setType("tutorial");
        tut1.setDay(4);
        tut1.setTimeStart(12, 0);
        tut1.setTimeEnd(13, 0);

        Slot tut2 = new Slot();
        tut2.setType("tutorial");
        tut2.setDay(4);
        tut2.setTimeStart(13, 0);
        tut2.setTimeEnd(14, 0);

        ArrayList<Slot> s1 = new ArrayList<>();
        ArrayList<Slot> s2 = new ArrayList<>();

        s1.add(lecture);
        s1.add(tut1);

        s2.add(lecture);
        s2.add(tut2);

        ArrayList<Group> groups = new ArrayList<>();

        Group g1 = new Group();
        g1.setSlots(s1);

        Group g2 = new Group();
        g2.setSlots(s2);

        groups.add(g1);
        groups.add(g2);

        course.setCapacity(60);
        course.setCourseCode("WIX3004");
        course.setCourseName("Mobile Application Development");
        course.setCredits(3);
        course.setDescription("Develop mobile application");
        course.setFaculty(fsktm);
        course.setGroups(groups);
        course.setInstructors(instructors);
        course.setTaken(3521);
        course.setSeats(12);

        course.setLearningOutcome("You will learn something hopefully.");

        ArrayList<Course> preq = new ArrayList<>();
        preq.add(c1);
        preq.add(c2);
        course.setPrerequisite(preq);

        ArrayList<Review> reviews = new ArrayList<>();
        Review r1 = new Review();
        r1.setComment("Good");
        r1.setRating(5);
        Review r2 = new Review();
        r2.setComment("Not Bad");
        r2.setRating(3);
        Review r3 = new Review();
        r3.setComment("Could be better");
        r3.setRating(2);
        Review r4 = new Review();
        r4.setComment("This sucks");
        r4.setRating(1);
        reviews.add(r1);
        reviews.add(r2);
        reviews.add(r3);
        reviews.add(r4);
        course.setReviews(reviews);

        courseList.add(course);
        courseList.add(course);
        courseList.add(course);
        courseList.add(course);
        courseList.add(course);
        courseList.add(course);
        courseList.add(course);
        courseList.add(course);
        courseList.add(course);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragment, container, false);

        RecyclerView courseListView = view.findViewById(R.id.courseList);
        courseListView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        courseListView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new CourseAdapter(courseList);
        courseListView.setAdapter(adapter);

        return view;
    }

}
