package com.cncoderx.github.preference;

import android.content.Context;

import com.cncoderx.github.utils.AESUtils;
import com.cncoderx.github.utils.Constants;

/**
 * @author cncoderx
 */
public class AccountPreference extends BasePreference {
    private static final String PREF_NAME = "account.pref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN = "token";

    private boolean isEncrypted;

    public AccountPreference(Context context) {
        this(context, true);
    }

    public AccountPreference(Context context, boolean encrypted) {
        super(context, PREF_NAME, Context.MODE_PRIVATE);
        isEncrypted = encrypted;
    }

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public String getPassword() {
        String password = getString(KEY_PASSWORD);
        return isEncrypted ? AESUtils.decrypt(Constants.AES_KEY, password) : password;
    }

    public String getToken() {
        return getString(KEY_TOKEN);
    }

    public void setUsername(String username) {
        putString(KEY_USERNAME, username);
    }

    public void setPassword(String password) {
        if (isEncrypted) {
            putString(KEY_PASSWORD, AESUtils.encrypt(Constants.AES_KEY, password));
        } else {
            putString(KEY_PASSWORD, password);
        }
    }

    public void setToken(String token) {
        putString(KEY_TOKEN, token);
    }
}
