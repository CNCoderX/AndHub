package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cncoderx.github.MainActivity;
import com.cncoderx.github.R;
import com.cncoderx.github.form.LoginForm;
import com.cncoderx.github.netservice.ILoginService;
import com.cncoderx.github.preference.AccountPreference;
import com.cncoderx.github.utils.URLUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_login_username)
    EditText etUsername;

    @BindView(R.id.et_login_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        etUsername.setText("CNCoderX");
        etPassword.setText("wujie196741");
    }

    @OnClick(R.id.btn_login_login)
    public void onLogin(View view) {
        final String username = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "请输入用户名或邮箱", Toast.LENGTH_SHORT).show();
            etUsername.requestFocus();
            return;
        }
        final String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        final String authorization = getBasicAuth(username, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLUtils.BASE_URL)
                .build();

        ILoginService service = retrofit.create(ILoginService.class);

        LoginForm form = new LoginForm();
        form.setClientId("a589a33e95411aa2fd71");
        form.setClientSecret("6891fb6414508f8c4f8d02458520e954683f81e4");
        String jsonBody = new GsonBuilder().create().toJson(form);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        Call<ResponseBody> call = service.login(authorization, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("LoginActivity", "onResponse");
                    ResponseBody body = response.body();
                    if (body != null) {
                        try {
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(body.string());
                            JsonObject object = element.getAsJsonObject();
                            String token = object.get("token").getAsString();

                            AccountPreference preference = new AccountPreference(LoginActivity.this);
                            preference.setUsername(username);
                            preference.setPassword(password);
                            preference.setToken(token);
                            preference.apply();

                            startActivity(new Intent(LoginActivity.this, SearchActivity.class));
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(errorBody.string());
                            JsonObject object = element.getAsJsonObject();
                            String message = object.get("message").getAsString();
                            Toast.makeText(getApplicationContext(), getString(R.string.http_error_message,
                                    response.code(), message), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("LoginActivity", "onFailure");
            }
        });
    }

    private static String getBasicAuth(String username, String password) {
        String credentials = username + ":" + password;
        String basic;
        try {
            basic = Base64.encodeToString(credentials.getBytes("US-ASCII"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        return "Basic " + basic;
    }
}
