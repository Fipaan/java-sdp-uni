package lfs.decorators;
import lfs.courses.*;
import lfs.utils.*;

public class MentorSupportDecorator extends CourseDecorator {
    public MentorSupportDecorator(Course course) { super(course); }

    @Override
    public String deliverContent() {
        return StringUtils.list(course.deliverContent(), "Mentor support");
    }
}
