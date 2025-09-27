public class GenericUtils {
    public static <T> int indexOf(T[] array, T target, int arrayLength) {
        for (int i = 0; i < arrayLength; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }
    public static <T> int indexOf(T[] array, T target) {
        return indexOf(array, target, array.length);
    }
    public static int indexOf(byte[] array, byte target, int arrayLength) {
        for (int i = 0; i < arrayLength; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    public static int indexOf(byte[] array, byte target) {
        return indexOf(array, target, array.length);
    }
    public static <T> boolean startsWith(T[] array, int offset, T[] prefix, int prefix_length) {
        if (offset + prefix_length > array.length) return false;
        for (int i = 0; i < prefix_length; i++) {
            if (!array[offset + i].equals(prefix[i])) {
                return false;
            }
        }
        return true;
    }
    public static boolean startsWith(byte[] array, int offset, byte[] prefix, int prefix_length) {
        if (offset + prefix_length > array.length) return false;
        for (int i = 0; i < prefix_length; i++) {
            if (array[offset + i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }
    public static <T> boolean startsWith(T[] array, int offset, T[] prefix) {
        return startsWith(array, offset, prefix, prefix.length);
    }
    public static boolean startsWith(byte[] array, int offset, byte[] prefix) {
        return startsWith(array, offset, prefix, prefix.length);
    }
}
