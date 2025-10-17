package lfs.decorators;
import lfs.courses.*;
import lfs.utils.*;

public class CertificateDecorator extends CourseDecorator {
    public CertificateDecorator(Course course) { super(course); }

    @Override
    public String deliverContent() {
        return StringUtils.list(course.deliverContent(), "certificate");
    }
}
