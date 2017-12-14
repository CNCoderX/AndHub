package com.cncoderx.andhub.preference;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.cncoderx.andhub.R;

import java.util.Locale;

/**
 * @author cncoderx
 */
public class SettingPreference extends BasePreference {
    public static final String PREF_NAME = "setting.pref";
    public static final String KEY_LANG = "language";
    public static final String KEY_CODE_THEMES = "code_themes";
    public static final String KEY_CODE_FONT_SIZE = "code_font_size";
    public static final String KEY_CODE_LINE_WRAPPING = "code_line_wrapping";
    public static final String KEY_CODE_LINE_NUMBERS = "code_line_numbers";

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

    @BindingAdapter({"setting", "key"})
    public static void setSetting(TextView view, SettingPreference preference, String key) {
        switch (key) {
            case KEY_LANG:
                setSettingArray(view, R.array.languages, preference.getLanguage());
                break;
            case KEY_CODE_THEMES:
                setSettingArray(view, R.array.code_themes, preference.getCodeThemes());
                break;
            case KEY_CODE_FONT_SIZE:
                setSettingArray(view, R.array.code_font_sizes, preference.getCodeFontSize());
                break;
            case KEY_CODE_LINE_WRAPPING:
                setSettingBool(view, preference.isLineWrapping());
                break;
            case KEY_CODE_LINE_NUMBERS:
                setSettingBool(view, preference.isLineNumbers());
                break;
        }
    }

    private static void setSettingBool(TextView view, boolean bool) {
        if (view instanceof CheckBox) {
            ((CheckBox) view).setChecked(bool);
        } else if (view instanceof Switch) {
            ((Switch) view).setChecked(bool);
        } else {
            view.setText(Boolean.toString(bool));
        }
    }

    private static void setSettingArray(TextView view, @ArrayRes int id, int index) {
        Resources resources = view.getResources();
        String[] array = resources.getStringArray(id);
        view.setText(array[index]);
    }
}
