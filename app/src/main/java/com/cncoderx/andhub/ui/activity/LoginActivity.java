package com.cncoderx.andhub.ui.activity;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.cncoderx.andhub.AppContext;
import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.account.GitAccount;
import com.cncoderx.andhub.account.GitAccountManager;
import com.cncoderx.andhub.databinding.LoginActivityBinding;
import com.cncoderx.andhub.model.Auth;
import com.cncoderx.andhub.model.LoginForm;
import com.cncoderx.andhub.model.Profile;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IAuthService;
import com.cncoderx.andhub.okhttp.service.IUserService;
import com.cncoderx.andhub.preference.ProfilePreference;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.AuthUtils;
import com.cncoderx.andhub.utils.Constants;
import com.cncoderx.andhub.utils.DensityUtils;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.google.gson.GsonBuilder;

import java.util.Collections;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava2.HttpException;

public class LoginActivity extends BaseActivity {
    private BindingAdapter<GitAccount> mAccountAdapter;

    private LoginActivityBinding mBinding;
    private String mUsername, mPassword;

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;
    private Bundle mResultBundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.login_activity);

        GitAccount[] accounts = GitAccountManager.getGitAccounts();
        mAccountAdapter = new BindingAdapter<>(R.layout.item_account_drop_list, BR.account, accounts);
        if (accounts.length > 0) {
            GitAccount account = accounts[0];
            mBinding.setUsername(account.getName());
            mBinding.setPassword(account.getPassword());
            mBinding.setAvatarUrl(account.getAvatar());
        }
    }

    public void onLogin(View v) {
        if (checkNameAndPwd()) {
            login();
        }
    }

    private boolean checkNameAndPwd() {
        mUsername = mBinding.getUsername();
        if (TextUtils.isEmpty(mUsername)) {
            showToast(R.string.input_username_tips);
            return false;
        }
        mPassword = mBinding.getPassword();
        if (TextUtils.isEmpty(mPassword)) {
            showToast(R.string.input_password_tips);
            return false;
        }
        return true;
    }

    private void login() {
        showProgress(getString(R.string.creating_authorization));

        final String authorization = AuthUtils.createBasicAuth(mUsername, mPassword);

        LoginForm form = new LoginForm();
        form.clientId = Constants.CLIENT_ID;
        form.clientSecret = Constants.CLIENT_SECRET;
        Collections.addAll(form.scopes, Constants.DEFAULT_SCOPE.split(","));
        String jsonBody = new GsonBuilder().create().toJson(form);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        IAuthService service = ServiceGenerator.create(IAuthService.class);
        service.createAuth(authorization, body)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(new Consumer<Auth>() {
                    @Override
                    public void accept(Auth auth) throws Exception {
                        final String token = auth.token;
                        AppContext.getInstance().setToken(token);
                        createGitAccount(mUsername, mPassword, token);
                        showProgress(getString(R.string.requesting_profile));
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Auth, Single<Profile>>() {
                    @Override
                    public Single<Profile> apply(Auth auth) throws Exception {
                        IUserService service = ServiceGenerator.create(IUserService.class);
                        return service.getUser();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Profile>bindToLifecycle())
                .subscribe(new Consumer<Profile>() {
                    @Override
                    public void accept(Profile profile) throws Exception {
                        showProgress(getString(R.string.logging_in));
                        GitAccountManager.updateGitAccountAvatar(mUsername, profile.avatarUrl);
                        new ProfilePreference(LoginActivity.this).apply(profile);
                        startMainActivity();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        HttpException exception = (HttpException) throwable;
                        showToast(exception.message());
                        hideProgress();
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
                mResultBundle = intent.getExtras();
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

    public void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showProgress(String message) {
        mBinding.setProgress(message);
    }

    private void hideProgress() {
        mBinding.setProgress("");
    }

    public void onSearch(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
    }

    public void onUnfold(View v) {
        final PopupWindow window = new PopupWindow(mBinding.etLoginUsername.getWidth(), -2);
        RecyclerView view = new RecyclerView(this);
        RecyclerViewHelper.setLinearLayout(view);
        RecyclerViewHelper.setAdapter(view, mAccountAdapter);
        RecyclerViewHelper.setDivider(view, getResources().getDrawable(R.drawable.list_divider), DensityUtils.dp2px(this, .5f));
        RecyclerViewHelper.setOnItemClickListener(view, new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
                GitAccount account = mAccountAdapter.get(position);
                mBinding.setUsername(account.getName());
                mBinding.setPassword(account.getPassword());
                mBinding.setAvatarUrl(account.getAvatar());
                window.dismiss();
            }
        });
        window.setContentView(view);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));
        window.setAnimationStyle(R.style.expandable_list_anim);
        PopupWindowCompat.showAsDropDown(window, mBinding.etLoginUsername, 0, 0, Gravity.BOTTOM);
    }

    /**
     * Sends the result or a Constants.ERROR_CODE_CANCELED error if a result isn't present.
     */
    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }

}
