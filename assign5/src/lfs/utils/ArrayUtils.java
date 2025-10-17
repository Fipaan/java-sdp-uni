package lfs.utils;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ArrayUtils {
    public static <T> T findElement(ArrayList<T> arr, Predicate<T> check) {
        for (T elem : arr) {
            if (check.test(elem)) return elem;
        }
        return null;
    }
}
