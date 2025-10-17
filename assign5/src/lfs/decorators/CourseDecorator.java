package lfs.decorators;
import lfs.courses.*;

public abstract class CourseDecorator implements Course {
    protected Course course;
    public CourseDecorator(Course course) { this.course = course; }
    
    public String getName() { return course.getName(); }
    public Course setName(String name) { course.setName(name); return this; }
    public abstract String deliverContent();
}
