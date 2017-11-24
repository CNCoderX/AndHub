package com.cncoderx.github.ui.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cncoderx.github.AppContext;
import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.LoginForm;
import com.cncoderx.github.sdk.service.IAuthService;
import com.cncoderx.github.sdk.model.Auth;
import com.cncoderx.github.ui.dialog.LoadingDialog;
import com.cncoderx.github.utils.AuthUtils;
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.utils.Constants;
import com.google.gson.GsonBuilder;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class LoginActivity extends AccountAuthenticatorActivity {
    @BindView(R.id.et_login_username)
    EditText etUsername;

    @BindView(R.id.et_login_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Account account = accounts[0];
            String username = account.name;
            String password = am.getPassword(account);
            if (!TextUtils.isEmpty(username))
                etUsername.setText(username);
            if (!TextUtils.isEmpty(password))
                etPassword.setText(password);
        }
    }

    @OnClick(R.id.btn_login_login)
    public void onLogin(View view) {
        final String username = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), R.string.input_username_tips, Toast.LENGTH_SHORT).show();
            etUsername.requestFocus();
            return;
        }
        final String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), R.string.input_password_tips, Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        final String authorization = AuthUtils.createBasicAuth(username, password);
        IAuthService service = ServiceGenerator.create(IAuthService.class);

        LoginForm form = new LoginForm();
        form.clientId = Constants.CLIENT_ID;
        form.clientSecret = Constants.CLIENT_SECRET;
        Collections.addAll(form.scopes, Constants.DEFAULT_SCOPE.split(","));
        String jsonBody = new GsonBuilder().create().toJson(form);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        Call<Auth> call = service.create(authorization, body);
        call.enqueue(new CallbackFinal<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                String token = auth.token;
                AppContext.getInstance().setToken(token);
                AppContext.getInstance().setLoginName(username);

                loginSuccess(username, password, token);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(ResponseBody body) {
                Toast.makeText(getApplicationContext(), R.string.login_failure, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPostResponse() {
                LoadingDialog.close();
            }
        });
        LoadingDialog.show(this);
    }

    private void loginSuccess(String username, String password, String token) {
        AccountManager am = AccountManager.get(this);
        Account account = new Account(username, Constants.ACCOUNT_TYPE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            boolean isNewAccount = intent.getBooleanExtra(AccountManager.KEY_BOOLEAN_RESULT, false);
            if (isNewAccount) {
                am.addAccountExplicitly(account, password, null);
                am.setAuthToken(account, Constants.TOKEN_TYPE, token);

                final Intent data = new Intent();
                data.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
                data.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
                data.putExtra(AccountManager.KEY_PASSWORD, password);
                setAccountAuthenticatorResult(intent.getExtras());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                am.setPassword(account, password);
                am.setAuthToken(account, Constants.TOKEN_TYPE, token);
            }
        } else {
            Account[] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);
            if (accounts.length > 0) {
                account = accounts[0];
                am.setPassword(account, password);
                am.setAuthToken(account, Constants.TOKEN_TYPE, token);
            } else {
                am.addAccountExplicitly(account, password, null);
                am.setAuthToken(account, Constants.TOKEN_TYPE, token);
            }
        }
    }

}
