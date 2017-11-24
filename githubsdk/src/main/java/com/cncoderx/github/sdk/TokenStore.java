package com.cncoderx.github.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author cncoderx
 */
public class TokenStore {
    private SharedPreferences mPreference;

    private static TokenStore sTokenStore;

    public static final String PREF_NAME = "access_token";
    public static final String KEY_NAME = "name";
    public static final String KEY_TOKEN = "token";

    @NonNull
    public static TokenStore getInstance(Context context) {
        if (sTokenStore == null)
            sTokenStore = new TokenStore(context);
        return sTokenStore;
    }

    public TokenStore(Context context) {
        mPreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getToken() {
        return mPreference.getString(KEY_TOKEN, null);
    }

    public void setToken(String token) {
        mPreference.edit().putString(KEY_TOKEN, token).apply();
    }

    public void removeToken() {
        mPreference.edit().remove(KEY_TOKEN).apply();
    }

    public String getName() {
        return mPreference.getString(KEY_NAME, null);
    }

    public void setName(String name) {
        mPreference.edit().putString(KEY_NAME, name).apply();
    }

    public void removeName() {
        mPreference.edit().remove(KEY_NAME).apply();
    }
}
