package fipaan.com.utils;

import fipaan.com.errors.FThrow;

public class ProcessUtils {
    public static String caller() { return Thread.currentThread().getStackTrace()[3].getMethodName(); }

    public static void sleep(double secs) {
        double millis_full = secs * 1_000;
        long millis = (long)Math.floor(millis_full);
        int nanos   = (int)Math.floor((millis_full - millis) * 1_000);
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) { FThrow.New(e, "sleep failed"); }
    }
}
