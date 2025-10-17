package lfs.courses;

public class ProgrammingCourse implements Course {
    private String name;
    public String getName() { return name; }
    public Course setName(String name) { this.name = name; return this; }

    public ProgrammingCourse() { name = "Programming"; }
    public ProgrammingCourse(String name) { this.name = name; }

    public String deliverContent() {
        return "Programming course";
    }
}
