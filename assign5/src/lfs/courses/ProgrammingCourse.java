package lfs.courses;

public class ProgrammingCourse implements Course {
    private boolean startedLearning = false;
    public ProgrammingCourse() {}
    
    public void startLearning() { startedLearning = true; }
    public boolean isStartedLearning() { return startedLearning; }

    public String deliverContent() {
        return "Programming course";
    }
}
