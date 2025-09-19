public class StringTools {
    public static<T> String prefixLines(String prefix, T text_obj) {
        if (text_obj == null || prefix == null) {
            throw new IllegalArgumentException("Text and prefix cannot be null");
        }
        String text = text_obj.toString();
        
        String[] lines = text.split("\\R"); // \R matches any line-break
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            result.append(prefix).append(lines[i]);
            if (i < lines.length - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }
}
