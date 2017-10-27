package com.cncoderx.github.preference;

import android.content.Context;

/**
 * @author cncoderx
 */
public class SearchOptionsPreference extends BasePreference {
    private static final String PREF_NAME = "search_options.pref";
    private static final String KEY_SORT_OPTION = "option_sort";
    private static final String KEY_LANG_OPTION = "option_lang";

    public SearchOptionsPreference(Context context) {
        super(context, PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getSortOption() {
        return getInt(KEY_SORT_OPTION);
    }

    public SearchOptionsPreference setSortOption(int sort) {
        putInt(KEY_SORT_OPTION, sort);
        return this;
    }

    public int getLangOption() {
        return getInt(KEY_LANG_OPTION);
    }

    public SearchOptionsPreference setLangOption(int lang) {
        putInt(KEY_LANG_OPTION, lang);
        return this;
    }
}
