package app.utils;

public final class OsUtils {
    public static boolean isWindows()
    {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
