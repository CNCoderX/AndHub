package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.cncoderx.github.AppContext;
import com.cncoderx.github.R;
import com.cncoderx.github.accounts.GitAccountManager;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Auth;
import com.cncoderx.github.sdk.service.IAuthService;
import com.cncoderx.github.ui.view.LinearGradientTextView;
import com.cncoderx.github.utils.AuthUtils;
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @author cncoderx
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.tv_splash_text)
    LinearGradientTextView mText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
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
        mText.setTypeface(Typeface.createFromAsset(
                getResources().getAssets(), "icon.ttf"));
        mText.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mText.stop();
    }

    private void checkAuthorization(final String token) {
        String authorization = AuthUtils.createBasicAuth(Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        IAuthService service = ServiceGenerator.create(IAuthService.class);
        Call<Auth> call = service.check(authorization, Constants.CLIENT_ID, token);
        call.enqueue(new CallbackFinal<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                startMainActivity();
            }

            @Override
            public void onFailure(ResponseBody body) {
                Toast.makeText(getApplicationContext(), R.string.token_invalid, Toast.LENGTH_SHORT).show();
                GitAccountManager.invalidateAuthToken(token);
                startLoginActivity();
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
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
