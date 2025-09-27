import java.util.concurrent.locks.LockSupport;

public class SystemUtils {
    public static void sleep(double secs) {
        long lsecs = (long) (secs * 1_000_000_000);
        LockSupport.parkNanos(lsecs);
    }
}
