package fipaan.com.utils;

import fipaan.com.console.*;
import fipaan.com.printf.*;
import fipaan.com.array.*;
import fipaan.com.generic.*;
import fipaan.com.is.*;
import fipaan.com.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.util.function.Predicate;

public class ArrayUtils {
    public static <T> T findElement(ArrayList<T> arr, Predicate<T> check) {
        for (T elem : arr) {
            if (check.test(elem)) return elem;
        }
        return null;
    }
    public static <Self> Self[] New(Class<Self> self, int size) {
        return TypeUtils.<Object, Self[]>cast(Array.newInstance(self, size));
    }
    public static <Self> Self[] extractArr(Class<Self> cls, ArrayList<Self> list) {
        Self[] arr = New(cls, list.size());
        return TypeUtils.<Object, Self[]>cast(list.toArray(arr));
    }
    public static <In, Out> Out[] castArr(Class<Out> cls, In[] arr) {
        Out[] out = New(cls, arr.length);
        for (int i = 0; i < arr.length; i++) {
            out[i] = TypeUtils.<In, Out>cast(arr[i]);
        }
        return out;
    }
    /*
    public static <U, V> V[] castArr(U[] arr) {
        return TypeUtils.<Object, V[]>cast(TypeUtils.<U[], Object>cast(arr));
    }
    public static <T> T[] extractArr(ArrayList<T> list) {
        return ArrayUtils.<Object, T>castArr(list.toArray());
    }
    public static <T> T[] New(int size) {
        return ArrayUtils.<Object, T>castArr(new Object[size]);
    }
    */
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

    public static <T> boolean onArraysDo(T[][] in, T[][] out, ArrayOpData data,
                                         InOutPosFunc<T[][]> func) {
        if (data.apply(in, out) == null) return false;
        if (!data.verify()) return false;
        
        for (int j = 0; j < data.height; ++j) {
            for (int i = 0; i < data.width; ++i) {
                func.accept(in, out, data.in.x + i, data.in.y + j, data.out.x + i, data.out.y + j);
            }
        }

        return true;
    }

    public static <T> boolean onArraysDo(Array2D<T> in, Array2D<T> out, ArrayOpData data,
                                         InOutPosFunc<Array2D<T>> func) {
        if (!data.apply(in, out).verify()) return false;
        
        for (int j = 0; j < data.height; ++j) {
            for (int i = 0; i < data.width; ++i) {
                func.accept(in, out, data.in.x + i, data.in.y + j, data.out.x + i, data.out.y + j);
            }
        }

        return true;
    }

    public static <T> boolean onArraysDo(Array2DInt in, Array2DInt out, ArrayOpData data, 
                                         InOutPosFunc<Array2DInt> func) {
        if (!data.apply(in, out).verify()) return false;

        for (int j = 0; j < data.height; ++j) {
            for (int i = 0; i < data.width; ++i) {
                func.accept(in, out, data.in.x + i, data.in.y + j, data.out.x + i, data.out.y + j);
            }
        }

        return true;
    }
    
    public static <T> boolean moveArr(T[][] in, T[][] out, ArrayOpData data) {
        if (data.apply(in, out) == null) return false;
        if (!data.verify()) return false;

        for (int j = 0; j < data.height; ++j) {
            System.arraycopy(in[data.in.y + j], data.in.x, out[data.out.y + j], data.out.x, data.width);
        }

        return true;
    }
    public static <T> boolean moveArr(Array2D<T> in, Array2D<T> out, ArrayOpData data) {
        if (!data.apply(in, out).verify()) return false;
        
        T[] inBuf  =  in.getRaw();
        T[] outBuf = out.getRaw();
        int  inCursor = data.in.width  * data.in.y  + data.in.x;
        int outCursor = data.out.width * data.out.y + data.out.x;
        for (int j = 0; j < data.height; ++j) {
            System.arraycopy(inBuf, inCursor, outBuf, outCursor, data.width);
            inCursor  += data.in.width;
            outCursor += data.out.width;
        }
        return true;
    }
    public static boolean moveArr(Array2DInt in, Array2DInt out, ArrayOpData data) {
        if (!data.apply(in, out).verify()) return false;
        
        int[] inBuf  =  in.getRaw();
        int[] outBuf = out.getRaw();
        int  inCursor = data.in.width  * data.in.y;
        int outCursor = data.out.width * data.out.y;
        for (int j = 0; j < data.height; ++j) {
            System.arraycopy(inBuf, inCursor, outBuf, outCursor, data.width);
            inCursor  += data.in.width;
            outCursor += data.out.width;
        }
        return true;
    }
}
