package com.miiicasa.casa.utils;

import android.util.Log;

import com.miiicasa.Config;

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

    public static boolean checkOpen() {
        return (debug == false && Config.IS_DEBUG)
            ? Log.isLoggable(Config.DEBUG_KEY, Log.DEBUG)
            : debug;
    }

    public static String getProjectName() {
        return projectName;
    }

    private static String mergeMessages(String TAG, String format, Object... args) {
        String msg = (args == null) ? format : String.format(format, args);
        return "[" + TAG + "] " + msg;
    }

    public static void d(String TAG, String format, Object... args) {
        if (checkOpen()) {
            Log.d(projectName, mergeMessages(TAG, format, args));
        }
    }

    public static void i(String TAG, String format, Object... args) {
        if (checkOpen()) {
            Log.i(projectName, mergeMessages(TAG, format, args));
        }
    }

    public static void e(String TAG, String format, Object... args) {
        if (checkOpen()) {
            Log.e(projectName, mergeMessages(TAG, format, args));
        }
    }

    public static void w(String TAG, String format, Object... args) {
        if (checkOpen()) {
            Log.w(projectName, mergeMessages(TAG, format, args));
        }
    }

    public static void v(String TAG, String format, Object... args) {
        if (checkOpen()) {
            Log.v(projectName, mergeMessages(TAG, format, args));
        }
    }
}
