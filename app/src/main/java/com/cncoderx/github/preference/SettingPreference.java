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
    private static final String KEY_CODE_FONT_SIZE = "code_font_size";
    private static final String KEY_CODE_LINE_WRAPPING = "code_line_wrapping";
    private static final String KEY_CODE_LINE_NUMBERS = "code_line_numbers";

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

    public int getCodeFontSize() {
        return getInt(KEY_CODE_FONT_SIZE);
    }

    public SettingPreference setCodeFontSize(int fontSize) {
        putInt(KEY_CODE_FONT_SIZE, fontSize);
        return this;
    }

    public boolean isLineWrapping() {
        return getBoolean(KEY_CODE_LINE_WRAPPING);
    }

    public SettingPreference setLineWrapping(boolean wrapping) {
        putBoolean(KEY_CODE_LINE_WRAPPING, wrapping);
        return this;
    }

    public boolean isLineNumbers() {
        return getBoolean(KEY_CODE_LINE_NUMBERS, true);
    }

    public SettingPreference setLineNumbers(boolean lineNumbers) {
        putBoolean(KEY_CODE_LINE_NUMBERS, lineNumbers);
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

    public int getCodeFontSizeValue() {
        int value;
        switch (getCodeFontSize()) {
            case 1:
                value = 14;
                break;
            case 2:
                value = 16;
                break;
            default:
                value = 12;
                break;
        }
        return value;
    }
}
