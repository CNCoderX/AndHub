package com.cncoderx.github.preference;

import android.content.Context;

/**
 * @author cncoderx
 */
public class SettingPreference extends BasePreference {
    private static final String PREF_NAME = "setting.pref";
    private static final String KEY_LANG = "language";

    public SettingPreference(Context context) {
        super(context, PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getLanguage() {
        return getInt(KEY_LANG);
    }

    public SettingPreference setLanguage(int language) {
        putInt(KEY_LANG, language);
        return this;
    }
}
