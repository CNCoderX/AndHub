package com.cncoderx.github.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * @author cncoderx
 */

public class BasePreference {
    final SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public BasePreference(Context context, String name, int mode) {
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(name, mode);
    }

    public SharedPreferences.Editor putString(String key, String value) {
        return getEditor().putString(key, value);
    }

    public SharedPreferences.Editor putStringSet(String key, Set<String> values) {
        return getEditor().putStringSet(key, values);
    }

    public SharedPreferences.Editor putInt(String key, int value) {
        return getEditor().putInt(key, value);
    }

    public SharedPreferences.Editor putLong(String key, long value) {
        return getEditor().putLong(key, value);
    }

    public SharedPreferences.Editor putFloat(String key, float value) {
        return getEditor().putFloat(key, value);
    }

    public SharedPreferences.Editor putBoolean(String key, boolean value) {
        return getEditor().putBoolean(key, value);
    }

    public SharedPreferences.Editor remove(String key) {
        return getEditor().remove(key);
    }

    public SharedPreferences.Editor clear() {
        return getEditor().clear();
    }

    private SharedPreferences.Editor getEditor() {
        if (mEditor == null)
            mEditor = mSharedPreferences.edit();
        return mEditor;
    }

    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public Set<String> getStringSet(String key) {
        return mSharedPreferences.getStringSet(key, null);
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0L);
    }

    public float getFloat(String key) {
        return mSharedPreferences.getFloat(key, 0f);
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    public boolean commit() {
        if (mEditor != null) {
            try {
                return mEditor.commit();
            } finally {
                mEditor = null;
            }
        }
        return false;
    }

    public void apply() {
        if (mEditor != null) {
            try {
                mEditor.apply();
            } finally {
                mEditor = null;
            }
        }
    }
}
