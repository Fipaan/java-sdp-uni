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
    public static <T> T[] extractArr(ArrayList<T> list) {
        return TypeUtils.<Object[], T[]>cast(list.toArray());
    }
    public static int[][] extractArray2D(ArrayList<ArrayList<Integer>> list) {
        int sizeI = list.size();
        int[][] arr = new int[sizeI][];
        for (int i = 0; i < sizeI; i++) {
            ArrayList<Integer> row = list.get(i);
            int sizeJ = row.size();
            arr[i] = new int[sizeJ];
            for (int j = 0; j < sizeJ; j++) {
                arr[i][j] = row.get(j);
            }
        }
        return arr;
    }
}
