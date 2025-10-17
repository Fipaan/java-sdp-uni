public class Main {
    public static void main(String[] args) {
        StudentPortalFacade student = new StudentPortalFacade();
        
        Course mathCourse = new MathCourse();
               mathCourse = new MentorSupportDecorator(mathCourse);
               mathCourse = new CertificateDecorator(mathCourse);
        student.enrollInCourse(mathCourse);
        
        Course programmingCourse = new ProgrammingCourse();
               programmingCourse = new GamificationDecorator(programmingCourse);
        student.enrollInCourse(programmingCourse);
        
        student.startLearning(mathCourse);
        student.startLearning(programmingCourse);
        student.completeCourse(mathCourse);
        student.completeCourse(programmingCourse);
    }
}
