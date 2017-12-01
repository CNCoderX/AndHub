package com.cncoderx.github.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoderx.github.BuildConfig;
import com.cncoderx.github.R;
import com.cncoderx.github.preference.ProfilePreference;
import com.cncoderx.github.preference.SettingPreference;
import com.cncoderx.github.sdk.TokenStore;
import com.cncoderx.github.ui.dialog.CodeThemeSettingDialog;
import com.cncoderx.github.ui.dialog.LanguageSettingDialog;
import com.cncoderx.github.ui.dialog.MessageBaseDialog;
import com.cncoderx.github.utils.ToolUtils;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author cncoderx
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_setting_language)
    TextView tvLanguage;

    @BindView(R.id.tv_setting_code_themes)
    TextView tvCodeThemes;

    @BindView(R.id.tv_setting_version)
    TextView tvVersion;

    @BindView(R.id.tv_setting_clear_cache)
    TextView tvCache;

    private LanguageSettingDialog mLanguageSettingDialog;
    private CodeThemeSettingDialog mCodeThemeSettingDialog;
    private MessageBaseDialog mClearCacheDialog;
    private MessageBaseDialog mLogoutDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mLanguageSettingDialog = new LanguageSettingDialog();
        mLanguageSettingDialog.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                restartApplication();
            }
        });
        mCodeThemeSettingDialog = new CodeThemeSettingDialog();
        mCodeThemeSettingDialog.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String[] themes = getResources().getStringArray(R.array.code_themes);
                tvCodeThemes.setText(themes[which]);
            }
        });
        mClearCacheDialog = MessageBaseDialog.newInstance(getString(R.string.clear_cache_confirm_again),
                getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new ClearCacheTask(SettingActivity.this, tvCache).execute();
                    }
                });
        mLogoutDialog = MessageBaseDialog.newInstance(
        getString(R.string.logout_confirm_again),
        getString(R.string.yes),
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        init();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new GetCacheSizeTask(this, tvCache).execute();
    }

    private void init() {
        SettingPreference preference = new SettingPreference(this);
        int lang = preference.getLanguage();
        int theme = preference.getCodeThemes();
        Resources resources = getResources();
        String language = resources.getStringArray(R.array.languages)[lang];
        String codeTheme = resources.getStringArray(R.array.code_themes)[theme];
        tvLanguage.setText(language);
        tvCodeThemes.setText(codeTheme);
        tvVersion.setText(BuildConfig.VERSION_NAME);
    }

    @OnClick(R.id.ll_setting_accounts)
    public void onAccountsClick(View v) {
        Intent intent = new Intent(this, AccountListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_setting_language)
    public void onLanguageClick(View v) {
        mLanguageSettingDialog.show(getSupportFragmentManager(), getString(R.string.language));
    }

    @OnClick(R.id.ll_setting_code_themes)
    public void onCodeThemesClick(View v) {
        mCodeThemeSettingDialog.show(getSupportFragmentManager(), getString(R.string.code_themes));
    }

    @OnClick(R.id.ll_setting_clear_cache)
    public void onClearCacheClick(View v) {
        mClearCacheDialog.show(getSupportFragmentManager(), getString(R.string.clear_cache));
    }

    @OnClick(R.id.ll_setting_about)
    public void onAboutClick(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_setting_version)
    public void onVersionClick(View v) {
        Toast.makeText(getApplicationContext(), R.string.latest_version_toast, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.ll_setting_logout)
    public void onLogoutClick(View v) {
        mLogoutDialog.show(getSupportFragmentManager(), getString(R.string.logout));
    }

    private void restartApplication() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
    }

    private void logout() {
        new ProfilePreference(this).clear().apply();
        TokenStore.getInstance(getApplicationContext()).removeToken();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    static class GetCacheSizeTask extends AsyncTask<Void, Void, Long> {
        final WeakReference<Context> mContext;
        final WeakReference<TextView> mCacheText;

        GetCacheSizeTask(Context context, TextView cacheText) {
            mContext = new WeakReference<>(context);
            mCacheText = new WeakReference<>(cacheText);
        }


        @Override
        protected Long doInBackground(Void... params) {
            Context c = mContext.get();
            if (c == null) return 0L;

            String cachePath = ToolUtils.getCacheDirPath(c);
            String filesPath = ToolUtils.getFilesDirPath(c);
            return getCacheSize(cachePath) + getCacheSize(filesPath);
        }

        public long getCacheSize(String dir) {
            try {
                return ToolUtils.getDirectorySize(new File(dir));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0L;
        }

        @Override
        protected void onPostExecute(Long size) {
            Context c = mContext.get();
            if (c == null) return;

            TextView cacheText = mCacheText.get();
            if (cacheText == null) return;

            String text = Formatter.formatShortFileSize(c, size);
            cacheText.setText(text);
        }
    }

    static class ClearCacheTask extends AsyncTask<Void, Void, Void> {
        final WeakReference<Context> mContext;
        final WeakReference<TextView> mCacheText;

        ClearCacheTask(Context context, TextView cacheText) {
            mContext = new WeakReference<>(context);
            mCacheText = new WeakReference<>(cacheText);
        }

        @Override
        protected Void doInBackground(Void... paths) {
            Context c = mContext.get();
            if (c == null) return null;

            String cachePath = ToolUtils.getCacheDirPath(c);
            String filesPath = ToolUtils.getFilesDirPath(c);
            clearCache(cachePath);
            clearCache(filesPath);
            return null;
        }

        public void clearCache(String dir) {
            try {
                ToolUtils.clearDir(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            Context c = mContext.get();
            if (c != null) {
                Toast.makeText(c, R.string.clear_cache_finished, Toast.LENGTH_SHORT).show();
            }
            TextView cacheText = mCacheText.get();
            if (cacheText != null) {
                String text = Formatter.formatShortFileSize(c, 0);
                cacheText.setText(text);
            }
        }
    }
}
