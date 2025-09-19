import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Engine {
    static private int width  = 1920;
    static private int height = 1080;
    static private long counter = 0;
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static boolean MouseInRect(Vector2 pos, Vector2 size) {
        throw new Error("TODO: MouseInRect(Vector2 pos, Vector2 size)");
    }
    static void DrawRectangle(Vector2 pos, Vector2 size, Color color) {
        throw new Error("TODO: DrawRectangle(Vector2 pos, Vector2 size, Color color)");
    }
    static void DrawText(Vector2 pos, Vector2 size, Color color, String text) {
        throw new Error("TODO: DrawText(Vector2 pos, Vector2 size, Color color, String text)");
    }
    static String GetInputAsString() {
        throw new Error("TODO: GetInputAsString()");
    }
    static void ScissorsBegin(Vector2 pos, Vector2 size) {
        throw new Error("TODO: ScissorsBegin(Vector2 pos, Vector2 size)");
    }
    static void ScissorsEnd() {
        throw new Error("TODO: ScissorsEnd()");
    }
    static int GetWidth() {
        throw new Error("TODO: GetWidth()");
    }
    static int GetHeight() {
        throw new Error("TODO: GetHeight()");
    }
    static Vector2 GetSize() {
        return new Vector2(width, height);
    }
    static long GetUniqueId() {
        return counter++;
    }
}
