package com.soloask.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Lebron on 2016/6/24.
 */
public class SharedPreferencesHelper {
    public static final String SHAREPREFERENCE_NAME = "com.soloask.android.preference";

    public static long getPreferenceLong(Context context, String name, long def) {
        SharedPreferences prefs = getSettingsSharedPreferences(context);
        return prefs.getLong(name, def);
    }

    public static SharedPreferences getSettingsSharedPreferences(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp;
    }

    public static void setPreferenceString(Context context, String name, String value) {
        if (context == null)
            return;
        SharedPreferences.Editor editPrefs = getSettingsEditor(context);
        editPrefs.putString(name, value);
        editPrefs.commit();
    }

    public static SharedPreferences.Editor getSettingsEditor(Context context) {
        SharedPreferences sp = getSettingsSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        return editor;
    }

    public static void setPreferenceLong(Context context, String name, long value) {
        SharedPreferences.Editor editPrefs = getSettingsEditor(context);
        editPrefs.putLong(name, value);
        editPrefs.commit();
    }

    public static void setPreferenceBoolean(Context context, String name, boolean value) {
        SharedPreferences.Editor editPrefs = getSettingsEditor(context);
        editPrefs.putBoolean(name, value);
        editPrefs.commit();
    }

    public static String getPreferenceString(Context context, String name, String def) {
        SharedPreferences prefs = getSettingsSharedPreferences(context);
        return prefs.getString(name, def);
    }

    public static boolean getPreferenceBoolean(Context context, String name, boolean def) {
        SharedPreferences prefs = getSettingsSharedPreferences(context);
        return prefs.getBoolean(name, def);
    }

    public static int getPreferenceInt(Context context, String name, int def) {
        SharedPreferences prefs = getSettingsSharedPreferences(context);
        return prefs.getInt(name, def);
    }

    public static void setPreferenceInt(Context context, String name, int value) {
        SharedPreferences.Editor editPrefs = getSettingsEditor(context);
        editPrefs.putInt(name, value);
        editPrefs.commit();
    }

    public static void setPreferenceStringSet(Context context, String name, Set<String> value) {
        SharedPreferences.Editor editor = getSettingsEditor(context);
        editor.putStringSet(name, value);
        editor.commit();
    }

    public static Set<String> getPreferenceStringSet(Context context, String name, Set<String> def) {
        return getSettingsSharedPreferences(context).getStringSet(name, def);
    }
}
