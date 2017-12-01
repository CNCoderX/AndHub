package com.cncoderx.github.preference;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * @author cncoderx
 */
public class SettingPreference extends BasePreference {
    private static final String PREF_NAME = "setting.pref";
    private static final String KEY_LANG = "language";
    private static final String KEY_CODE_THEMES = "code_themes";
    private static final String KEY_CODE_WRAP = "code_wrapped";

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

    public int getCodeThemes() {
        return getInt(KEY_CODE_THEMES);
    }

    public SettingPreference setCodeThemes(int theme) {
        putInt(KEY_CODE_THEMES, theme);
        return this;
    }

    public boolean isCodeWrapped() {
        return getBoolean(KEY_CODE_WRAP);
    }

    public SettingPreference setCodeWrapped(boolean wrapped) {
        putBoolean(KEY_CODE_WRAP, wrapped);
        return this;
    }

    @NonNull
    public Locale getLocale() {
        Locale locale;
        int lang = getLanguage();
        switch (lang) {
            case 1:
                locale = Locale.ENGLISH;
                break;
            case 2:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 3:
                locale = Locale.TRADITIONAL_CHINESE;
                break;
            default:
                locale = Locale.getDefault();
                break;
        }
        return locale;
    }
}
