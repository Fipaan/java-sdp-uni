package lfs.decorators;
import lfs.courses.*;

public abstract class CourseDecorator implements Course {
    protected Course course;
    public CourseDecorator(Course course) { this.course = course; }
 
    public void startLearning() { course.startLearning(); }
    public boolean isStartedLearning() { return course.isStartedLearning(); }   
    public abstract String deliverContent();
}
