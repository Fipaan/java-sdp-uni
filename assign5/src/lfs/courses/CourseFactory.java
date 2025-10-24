package lfs.courses;

import lfs.errors.FError;

public class CourseFactory {
    public CourseFactory() {}

    public Course buildMath() { return new MathCourse(); }
    public Course buildProgramming() { return new ProgrammingCourse(); }

    public Course build(String type) {
        switch (type) {
            case "Math":        return buildMath();
            case "Programming": return buildProgramming();
            default: throw FError.New("Unknown type: %s", type);
        }
    }
}
