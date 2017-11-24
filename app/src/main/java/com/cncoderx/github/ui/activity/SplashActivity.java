package com.cncoderx.github.ui.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cncoderx.github.AppContext;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Auth;
import com.cncoderx.github.sdk.model.User;
import com.cncoderx.github.sdk.service.IAuthService;
import com.cncoderx.github.utils.AuthUtils;
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.utils.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @author cncoderx
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final String token = getAuthToken();
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            String authorization = AuthUtils.createBasicAuth(Constants.CLIENT_ID, Constants.CLIENT_SECRET);
            IAuthService service = ServiceGenerator.create(IAuthService.class);
            Call<Auth> call = service.check(authorization, Constants.CLIENT_ID, token);
            call.enqueue(new CallbackFinal<Auth>() {
                @Override
                public void onSuccess(Auth auth) {
                    User user = auth.user;
                    if (user != null) {
                        AppContext.getInstance().setLoginName(user.login);
                    }
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(ResponseBody body) {
                    AccountManager am = AccountManager.get(SplashActivity.this);
                    am.invalidateAuthToken(Constants.ACCOUNT_TYPE, token);

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private String getAuthToken() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);
        String token = null;
        if (accounts.length > 0) {
            Account account = accounts[0];
            token = am.peekAuthToken(account, Constants.TOKEN_TYPE);
        }
        return token;
    }

}
