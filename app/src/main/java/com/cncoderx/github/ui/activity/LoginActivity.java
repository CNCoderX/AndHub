package com.cncoderx.github.ui.activity;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoderx.github.AppContext;
import com.cncoderx.github.R;
import com.cncoderx.github.accounts.GitAccount;
import com.cncoderx.github.accounts.GitAccountManager;
import com.cncoderx.github.preference.ProfilePreference;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Auth;
import com.cncoderx.github.sdk.model.LoginForm;
import com.cncoderx.github.sdk.model.Profile;
import com.cncoderx.github.sdk.service.IAuthService;
import com.cncoderx.github.sdk.service.IUserService;
import com.cncoderx.github.ui.adapter.AccountDropListAdapter;
import com.cncoderx.github.utils.AuthUtils;
import com.cncoderx.github.utils.BaseHandlerReference;
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.utils.Constants;
import com.cncoderx.github.utils.DensityUtils;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class LoginActivity extends AccountAuthenticatorActivity {
    @BindView(R.id.iv_login_avatar)
    ImageView ivAvatar;

    @BindView(R.id.et_login_username)
    EditText etUsername;

    @BindView(R.id.et_login_password)
    EditText etPassword;

    @BindView(R.id.tv_login_progress)
    TextView tvProgress;

    private String mUsername, mPassword;
    private AccountDropListAdapter mAccountAdapter;
    private LoginHandler mHandler = new LoginHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        GitAccount[] accounts = GitAccountManager.getGitAccounts();
        mAccountAdapter = new AccountDropListAdapter(accounts);
        if (accounts.length > 0) {
            GitAccount account = accounts[0];
            etUsername.setText(account.getName());
            etPassword.setText(account.getPassword());
            etUsername.setSelection(etUsername.length());
            etPassword.setSelection(etPassword.length());
            Glide.with(this).load(account.getAvatar()).into(ivAvatar);
        }
    }

    @OnClick(R.id.btn_login_login)
    public void onLogin(View view) {
        if (checkNameAndPwd()) {
            login();
        }
    }

    private boolean checkNameAndPwd() {
        mUsername = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(mUsername)) {
            Toast.makeText(getApplicationContext(), R.string.input_username_tips, Toast.LENGTH_SHORT).show();
            etUsername.requestFocus();
            return false;
        }
        mPassword = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(getApplicationContext(), R.string.input_password_tips, Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void login() {
        showProgress(getString(R.string.creating_authorization));
        mHandler.sendEmptyMessage(MSG_CREATE_AUTH);
    }

    public void createAuthorization() {
        final String authorization = AuthUtils.createBasicAuth(mUsername, mPassword);

        LoginForm form = new LoginForm();
        form.clientId = Constants.CLIENT_ID;
        form.clientSecret = Constants.CLIENT_SECRET;
        Collections.addAll(form.scopes, Constants.DEFAULT_SCOPE.split(","));
        String jsonBody = new GsonBuilder().create().toJson(form);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        IAuthService service = ServiceGenerator.create(IAuthService.class);
        Call<Auth> call = service.create(authorization, body);
        call.enqueue(new CallbackFinal<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                final String token = auth.token;

                AppContext.getInstance().setToken(token);
                createGitAccount(mUsername, mPassword, token);

                showProgress(getString(R.string.requesting_profile));
                mHandler.sendEmptyMessage(MSG_GET_PROFILE);
            }

            @Override
            public void onFailure(ResponseBody body) {
                Gson gson = new GsonBuilder().create();
                try {
                    JsonObject jsonObj = gson.fromJson(body.string(), JsonObject.class);
                    String message = jsonObj.get("message").getAsString();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                }
            }

            @Override
            public void onPostResponse(boolean successful) {
                if (!successful) {
                    hideProgress();
                }
            }
        });
    }

    private void createGitAccount(String username, String password, String token) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            boolean isNewAccount = intent.getBooleanExtra(AccountManager.KEY_BOOLEAN_RESULT, false);
            if (isNewAccount) {
                GitAccountManager.addGitAccount(username, password, token);

                final Intent data = new Intent();
                data.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
                data.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
                data.putExtra(AccountManager.KEY_PASSWORD, password);
                setAccountAuthenticatorResult(intent.getExtras());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                GitAccountManager.updateGitAccount(username, password, token);
            }
        } else {
            if (!GitAccountManager.addGitAccount(username, password, token)) {
                GitAccountManager.updateGitAccount(username, password, token);
            }
        }
    }

    public void getAccountProfile() {
        IUserService service = ServiceGenerator.create(IUserService.class);
        Call<Profile> call = service.getProfile();
        call.enqueue(new CallbackFinal<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                if (profile == null) return;

                GitAccountManager.updateGitAccountAvatar(mUsername, profile.avatarUrl);

                new ProfilePreference(LoginActivity.this)
                        .setAvatar(profile.avatarUrl)
                        .setLogin(profile.login)
                        .setName(profile.name)
                        .setBio(profile.bio)
                        .setCompany(profile.company)
                        .setBlog(profile.blog)
                        .setEmail(profile.email)
                        .setLocation(profile.location)
                        .setFollowers(profile.followers)
                        .setFollowing(profile.following)
                        .apply();

                showProgress(getString(R.string.logging_in));
                mHandler.sendEmptyMessage(MSG_START_MAIN);
            }

            @Override
            public void onPostResponse(boolean successful) {
                if (!successful) {
                    hideProgress();
                }
            }
        });
    }

    public void startMainActivity() {
//        hideProgress();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showProgress(String message) {
        ((ViewGroup) tvProgress.getParent()).setVisibility(View.VISIBLE);
        tvProgress.setText(message);
    }

    private void hideProgress() {
        ((ViewGroup) tvProgress.getParent()).setVisibility(View.GONE);
    }

    @OnClick(R.id.iv_login_search)
    public void onSearch(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.iv_login_username_unfold)
    public void onUnfold(View v) {
        showExpandableAccountList();
    }

    public void showExpandableAccountList() {
        final PopupWindow window = new PopupWindow(etUsername.getWidth(), -2);
        RecyclerView view = new RecyclerView(this);
        RecyclerViewHelper.setLinearLayout(view);
        RecyclerViewHelper.setAdapter(view, mAccountAdapter);
        RecyclerViewHelper.setDivider(view, getResources().getDrawable(R.drawable.list_divider), DensityUtils.dp2px(this, .5f));
        RecyclerViewHelper.setOnItemClickListener(view, new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
                GitAccount account = mAccountAdapter.get(position);
                etUsername.setText(account.getName());
                etPassword.setText(account.getPassword());
                etUsername.setSelection(etUsername.length());
                etPassword.setSelection(etPassword.length());
                Glide.with(LoginActivity.this).load(account.getAvatar()).into(ivAvatar);
                window.dismiss();
            }
        });
        window.setContentView(view);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));
        window.setAnimationStyle(R.style.expandable_list_anim);
        PopupWindowCompat.showAsDropDown(window, etUsername, 0, 0, Gravity.BOTTOM);
    }

    static final int MSG_CREATE_AUTH = 1;
    static final int MSG_GET_PROFILE = 2;
    static final int MSG_START_MAIN = 3;

    static class LoginHandler extends BaseHandlerReference<LoginActivity> {

        public LoginHandler(@NonNull LoginActivity reference) {
            super(reference);
        }

        @Override
        public void handleMessage(LoginActivity reference, Message msg) {
            switch (msg.what) {
                case MSG_CREATE_AUTH:
                    reference.createAuthorization();
                    break;
                case MSG_GET_PROFILE:
                    reference.getAccountProfile();
                    break;
                case MSG_START_MAIN:
                    reference.startMainActivity();
                    break;
            }
        }
    }
}
