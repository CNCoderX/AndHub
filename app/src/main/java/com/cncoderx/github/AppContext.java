package com.cncoderx.github;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.cncoderx.github.preference.SettingPreference;
import com.cncoderx.github.sdk.AuthorizationInterceptor;
import com.cncoderx.github.sdk.HttpConfig;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.TokenStore;
import com.cncoderx.github.utils.Constants;
import com.cncoderx.github.utils.Typefaces;

import java.util.Locale;

/**
 * @author cncoderx
 */
public class AppContext extends Application {
    private static AppContext instance;

    public static AppContext getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        AuthorizationInterceptor.addAccessURLPath("/applications");
        AuthorizationInterceptor.addAccessURLPath("/authorizations");

        HttpConfig config = new HttpConfig();
        config.setBaseUrl(Constants.BASE_URL);
        config.setConnectTimeout(Constants.DEFAULT_CONNECT_TIMEOUT);
        config.setReadTimeout(Constants.DEFAULT_READ_TIMEOUT);
        ServiceGenerator.initialize(this, config);

        Typefaces.replaceSystemDefaultFont(this, "Arial.ttf");

        int lang = new SettingPreference(this).getLanguage();
        switch (lang) {
            case 0:
                setLocale(Locale.getDefault());
                break;
            case 1:
                setLocale(Locale.ENGLISH);
                break;
            case 2:
                setLocale(Locale.SIMPLIFIED_CHINESE);
                break;
            case 3:
                setLocale(Locale.TRADITIONAL_CHINESE);
                break;
        }
    }

    public String getLoginName() {
        return TokenStore.getInstance(this).getName();
    }

    public void setLoginName(String loginName) {
        TokenStore.getInstance(this).setName(loginName);
    }

    public void setToken(String token) {
        TokenStore.getInstance(this).setToken(token);
    }

    @Nullable
    public String getToken() {
        return TokenStore.getInstance(this).getToken();
    }

    public void setLocale(Locale locale) {
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(config, dm);
    }

    public Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        return config.locale;
    }
}
