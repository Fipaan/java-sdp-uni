package lfs.decorators;
import lfs.courses.*;
import lfs.utils.*;

public class GamificationDecorator extends CourseDecorator {
    public GamificationDecorator(Course course) { super(course); }

    @Override
    public String deliverContent() {
        return StringUtils.list(course.deliverContent(), "games");
    }
}
