package com.cncoderx.andhub.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.view.View;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.SettingActivityBinding;
import com.cncoderx.andhub.okhttp.TokenStore;
import com.cncoderx.andhub.preference.ProfilePreference;
import com.cncoderx.andhub.preference.SettingPreference;
import com.cncoderx.andhub.ui.dialog.LanguageSettingDialog;
import com.cncoderx.andhub.ui.dialog.MessageBaseDialog;
import com.cncoderx.andhub.utils.ToolUtils;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author cncoderx
 */
public class SettingActivity extends BaseActivity {
    private LanguageSettingDialog mLanguageSettingDialog;
    private MessageBaseDialog mClearCacheDialog;
    private MessageBaseDialog mLogoutDialog;
    private SettingActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_activity);

        mLanguageSettingDialog = new LanguageSettingDialog();
        mClearCacheDialog = MessageBaseDialog.newInstance(
                getString(R.string.clear_cache_confirm_again),
                getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearCacheSync();
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

        SettingPreference preference = new SettingPreference(this);
        mBinding.setSetting(preference);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getCacheSync();
    }

    public void onLanguage(View v) {
        mLanguageSettingDialog.show(getSupportFragmentManager(), "");
    }

    public void onCodeStyle(View v) {
        Intent intent = new Intent(this, CodeStyleSettingActivity.class);
        startActivity(intent);
    }

    public void onClearCache(View v) {
        mClearCacheDialog.show(getSupportFragmentManager(), "");
    }

    public void onAbout(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void onVersion(View v) {
        showToast(R.string.latest_version_toast);
    }

    public void onLogout(View v) {
        mLogoutDialog.show(getSupportFragmentManager(), "");
    }

    private void logout() {
        new ProfilePreference(this).clear().apply();
        TokenStore.getInstance(getApplicationContext()).removeToken();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void invalidateAll() {
        mBinding.invalidateAll();
    }

    private void getCacheSync() {
        Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(SingleEmitter<Long> emitter) throws Exception {
                String cachePath = ToolUtils.getCacheDirPath(getApplicationContext());
                String filesPath = ToolUtils.getFilesDirPath(getApplicationContext());
                long l = ToolUtils.getDirectorySize(new File(cachePath))
                        + ToolUtils.getDirectorySize(new File(filesPath));
                emitter.onSuccess(l);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long l) throws Exception {
                mBinding.setCacheSize(l);
            }
        });
    }

    private void clearCacheSync() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                String cachePath = ToolUtils.getCacheDirPath(getApplicationContext());
                String filesPath = ToolUtils.getFilesDirPath(getApplicationContext());
                ToolUtils.clearDir(cachePath);
                ToolUtils.clearDir(filesPath);
                emitter.onComplete();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action() {
            @Override
            public void run() throws Exception {
                showToast(R.string.clear_cache_finished);
                mBinding.setCacheSize(0);
            }
        });
    }
}
