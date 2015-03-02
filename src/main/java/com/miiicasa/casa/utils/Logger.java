package com.miiicasa.casa.utils;

import android.util.Log;

/**
 * Created by showsky on 15/3/2.
 */
public class Logger {

    private final static String TAG = Logger.class.getSimpleName();
    private static boolean debug = false;
    private static String projectName = "miiicasa";

    public static void setProject(String projectName, boolean debug) {
        Logger.projectName = projectName;
        Logger.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static String getProjectName() {
        return projectName;
    }

    private static String mergeMessages(String TAG, String format, Object... args) {
        String msg = (args == null) ? format : String.format(format, args);
        return "[" + TAG + "] " + msg;
    }

    public static void d(String TAG, String format, Object... args) {
        if (debug) {
            Log.d(projectName, mergeMessages(TAG, format, args));
        }
    }

    public static void i(String TAG, String format, Object... args) {
        if (debug) {
            Log.i(projectName, mergeMessages(TAG, format, args));
        }
    }

    public static void e(String TAG, String format, Object... args) {
        if (debug) {
            Log.e(projectName, mergeMessages(TAG, format, args));
        }
    }

    public static void w(String TAG, String format, Object... args) {
        if (debug) {
            Log.w(projectName, mergeMessages(TAG, format, args));
        }
    }

    public static void v(String TAG, String format, Object... args) {
        if (debug) {
            Log.v(projectName, mergeMessages(TAG, format, args));
        }
    }
}
