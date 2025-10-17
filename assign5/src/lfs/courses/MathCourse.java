package lfs.courses;

public class MathCourse implements Course {
    private boolean startedLearning = false;
    public MathCourse() {}
    
    public void startLearning() { startedLearning = true; }
    public boolean isStartedLearning() { return startedLearning; }

    public String deliverContent() {
        return "Math course";
    }
}
