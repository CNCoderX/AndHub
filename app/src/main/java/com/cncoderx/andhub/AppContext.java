package com.cncoderx.andhub;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.cncoderx.andhub.account.GitAccountManager;
import com.cncoderx.andhub.okhttp.HttpConfig;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.TokenStore;
import com.cncoderx.andhub.okhttp.interceptor.AuthorizationInterceptor;
import com.cncoderx.andhub.preference.SettingPreference;
import com.cncoderx.andhub.utils.Constants;

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

        GitAccountManager.initialize(this);

        AuthorizationInterceptor.addAccessURLPath("/applications");
        AuthorizationInterceptor.addAccessURLPath("/authorizations");

        HttpConfig config = new HttpConfig();
        config.setBaseUrl(Constants.BASE_URL);
        config.setConnectTimeout(Constants.DEFAULT_CONNECT_TIMEOUT);
        config.setReadTimeout(Constants.DEFAULT_READ_TIMEOUT);
        ServiceGenerator.initialize(this, config);

        Locale locale = new SettingPreference(this).getLocale();
        setLocale(locale);
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
