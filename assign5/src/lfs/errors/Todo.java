package lfs.errors;

import java.lang.RuntimeException;
import java.lang.Thread;

public class Todo {
    public static RuntimeException New(String str) { return new RuntimeException("NOT IMPLEMENTED: " + str); }
    public static RuntimeException New() {
        String callerName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return Todo.New(callerName);
    }
}
