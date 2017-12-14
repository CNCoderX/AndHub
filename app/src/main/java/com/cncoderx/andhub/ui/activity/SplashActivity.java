package com.cncoderx.andhub.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cncoderx.andhub.AppContext;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.account.GitAccountManager;
import com.cncoderx.andhub.databinding.SplashActivityBinding;
import com.cncoderx.andhub.model.Auth;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IAuthService;
import com.cncoderx.andhub.utils.AuthUtils;
import com.cncoderx.andhub.utils.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author cncoderx
 */
public class SplashActivity extends BaseActivity {
    private SplashActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.splash_activity);
        final String token = AppContext.getInstance().getToken();
        if (TextUtils.isEmpty(token)) {
            startLoginActivity();
        } else {
            checkAuthorization(token);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.tvSplashText.setTypeface(Typeface.createFromAsset(
                getResources().getAssets(), "icon.ttf"));
        mBinding.tvSplashText.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.tvSplashText.stop();
    }

    private void checkAuthorization(final String token) {
        String authorization = AuthUtils.createBasicAuth(
                Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        ServiceGenerator.create(IAuthService.class)
                .checkAuth(authorization, Constants.CLIENT_ID, token)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Auth>bindToLifecycle())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        startMainActivity();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showToast(R.string.token_invalid);
                        GitAccountManager.invalidateAuthToken(token);
                        startLoginActivity();
                    }
                });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
