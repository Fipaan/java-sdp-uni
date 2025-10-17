package lfs;

import lfs.gui.FGUI;
import lfs.courses.*;
import lfs.errors.*;
import lfs.print.*;
import java.util.ArrayList;

public class StudentPortalFacade {
    private String studentName;
    public String getName() { return studentName; }
    public StudentPortalFacade setName(String name) { studentName = name; return this; }
    private ArrayList<Course> courses = new ArrayList<>();

    public StudentPortalFacade(String name) { studentName = name; }

    public void expectCourseEnrolledToggle(Course course, String action, boolean enrolled) {
        for (Course _course: courses) {
            if (_course == course) {
                if (enrolled) return;
                else {
                    FGUI.notifyError("Cannot %s. Course `%s` is already enrolled!", action, course.deliverContent());
                    throw FError.New("Course already enrolled!");
                }
            }
        }
        if (enrolled) {
            FGUI.notifyError("Cannot %s. Course `%s` is not enrolled!", action, course.deliverContent());
            throw FError.New("Course is not enrolled!");
        }
    }
    public void expectCourseEnrolled(Course course, String action) {
        expectCourseEnrolledToggle(course, action, true);
    }
    public void expectCourseNotEnrolled(Course course, String action) {
        expectCourseEnrolledToggle(course, action, false);
    }
    public void enrollInCourse(Course course) {
        Info.printfn("Enrolling: `%s`", course.deliverContent());
        expectCourseNotEnrolled(course, "enroll");
        courses.add(course);

        FGUI.notifyInfo("Succesfully enrolled `%s`", course.deliverContent());
    }
    public void startLearning(Course course) {
        Info.printfn("Starting learning: `%s`", course.deliverContent());
        expectCourseEnrolled(course, "start");
        FGUI.notifyInfo("Keep up with learning `%s`", course.deliverContent());
    }
    public void completeCourse(Course course) {
        Info.printfn("Completed learning: `%s`", course.deliverContent());
        expectCourseEnrolled(course, "complete");
        FGUI.notifyInfo("Succesfully completed `%s`. Congratulations!", course.deliverContent());
        courses.remove(course);
    }
}
