package lfs.courses;

public class MathCourse implements Course {
    private String name;
    public String getName() { return name; }
    public Course setName(String name) { this.name = name; return this; }

    public MathCourse() { name = "Math"; }
    public MathCourse(String name) { this.name = name; }

    public String deliverContent() {
        return "Math course";
    }
}
