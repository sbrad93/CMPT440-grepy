package edu.marist.brady;

/**
 * App utility class for constants.
 */
public final class CONSTANTS {
    /** Application name. */
    private static String appName = "grepy";
    /** Application version. */
    private static String version = "0.0.2";

    /**
     * CONSTANTS constructor.
     */
    private CONSTANTS() {
    }

    /**
     * App version getter.
     * {@link CONSTANTS#version}
     * @return Version string of app
     */
    public static String getVersion() {
        return version;
    }

    /**
     * App name getter.
     * @return App name string
     */
    public static String getAppName() {
        return appName;
    }
}
