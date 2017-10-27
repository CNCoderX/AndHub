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

    protected SharedPreferences.Editor putString(String key, String value) {
        return getEditor().putString(key, value);
    }

    protected SharedPreferences.Editor putStringSet(String key, Set<String> values) {
        return getEditor().putStringSet(key, values);
    }

    protected SharedPreferences.Editor putInt(String key, int value) {
        return getEditor().putInt(key, value);
    }

    protected SharedPreferences.Editor putLong(String key, long value) {
        return getEditor().putLong(key, value);
    }

    protected SharedPreferences.Editor putFloat(String key, float value) {
        return getEditor().putFloat(key, value);
    }

    protected SharedPreferences.Editor putBoolean(String key, boolean value) {
        return getEditor().putBoolean(key, value);
    }

    protected SharedPreferences.Editor remove(String key) {
        return getEditor().remove(key);
    }

    protected SharedPreferences.Editor clear() {
        return getEditor().clear();
    }

    private SharedPreferences.Editor getEditor() {
        if (mEditor == null)
            mEditor = mSharedPreferences.edit();
        return mEditor;
    }

    protected Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    protected String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    protected Set<String> getStringSet(String key) {
        return mSharedPreferences.getStringSet(key, null);
    }

    protected int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    protected long getLong(String key) {
        return mSharedPreferences.getLong(key, 0L);
    }

    protected float getFloat(String key) {
        return mSharedPreferences.getFloat(key, 0f);
    }

    protected boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    protected boolean contains(String key) {
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
