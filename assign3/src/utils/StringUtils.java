import java.nio.*;
import java.nio.charset.*;

public class StringUtils {
    public static boolean isValidCharset(byte[] bytes, int offset, int length, Charset charset) {
        CharsetDecoder decoder = charset.newDecoder()
                                        .onMalformedInput(CodingErrorAction.REPORT)
                                        .onUnmappableCharacter(CodingErrorAction.REPORT);

        ByteBuffer bb = ByteBuffer.wrap(bytes, offset, length);
        CharBuffer cb = CharBuffer.allocate(length);

        if (decoder.decode(bb, cb, true).isError()) return false;
        if (decoder.flush(cb).isError()) return false;

        return true;
    }
    
    public static int charLength(char ch) { return String.valueOf(ch).getBytes(StandardCharsets.UTF_8).length; }
    public record IndexResult(int index, boolean isFullChar) {}
    public static IndexResult indexOf(byte[] array, char target, int array_length) {
        byte[] targetBytes = String.valueOf(target).getBytes(StandardCharsets.UTF_8);
        int lastIndex = array_length - targetBytes.length;
        for (int offset = 0; offset <= lastIndex; ++offset) {
            if (GenericUtils.startsWith(array, offset, targetBytes)) return new IndexResult(offset, true);
        }
        for (int offset = lastIndex + 1; offset < array_length; ++offset) {
            if (GenericUtils.startsWith(array, offset, targetBytes, array_length - (offset + 1))) {
                return new IndexResult(offset, false);
            }
        }
        return new IndexResult(-1, false);
    }
    public static IndexResult indexOf(byte[] array, char target) {
        return indexOf(array, target, array.length);
    }
}
