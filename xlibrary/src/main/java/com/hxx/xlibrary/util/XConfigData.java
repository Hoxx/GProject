package com.hxx.xlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hxx.xlibrary.exception.XInitializeException;


/**
 * Created by Android on 2017/5/25.
 */

public class XConfigData {

    private static XConfigData instance = new XConfigData();
    private SharedPreferences preferences;

    public synchronized static XConfigData getInstance() {
        if (instance == null) {
            instance = new XConfigData();
        }
        return instance;
    }

    public void init(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private XConfigData() {
    }

    public void setStringData(String key, String value) {
        checkNotInit();
        preferences.edit().putString(key, value).apply();
    }

    public String getStringData(String key, String defaultValue) {
        checkNotInit();
        return preferences.getString(key, defaultValue);
    }

    public void setBooleanData(String key, boolean value) {
        checkNotInit();
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBooleanData(String key, boolean defaultValue) {
        checkNotInit();
        return preferences.getBoolean(key, defaultValue);
    }

    public void setLongData(String key, Long value) {
        checkNotInit();
        preferences.edit().putLong(key, value).apply();
    }

    public Long getLongData(String key, Long defaultValue) {
        checkNotInit();
        return preferences.getLong(key, defaultValue);
    }

    public void setIntData(String key, Integer value) {
        checkNotInit();
        preferences.edit().putInt(key, value).apply();
    }

    public int getIntData(String key, Integer defaultValue) {
        checkNotInit();
        return preferences.getInt(key, defaultValue);
    }

    public void setFloatData(String key, float value) {
        checkNotInit();
        preferences.edit().putFloat(key, value).apply();
    }

    public float getFloatData(String key, float defaultValue) {
        checkNotInit();
        return preferences.getFloat(key, defaultValue);
    }

    public void remove(String key) {
        checkNotInit();
        preferences.edit().remove(key).apply();
    }

    public void clear() {
        checkNotInit();
        preferences.edit().clear().apply();
    }

    private void checkNotInit() {
        if (preferences == null)
            throw new XInitializeException("XConfigData Not Initialize");
    }

}
