package com.cncoderx.github.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cncoderx.github.R;
import com.cncoderx.github.preference.SettingPreference;
import com.cncoderx.github.ui.view.AppBar;
import com.cncoderx.github.utils.IntentExtra;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeReviewActivity extends BaseActivity {
    @BindView(R.id.app_bar)
    AppBar mAppbar;

    @BindView(R.id.wv_code_review_content)
    WebView mWebView;

    private ICodeReview mCodeReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_review);
        ButterKnife.bind(this);

        WebSettings settings = mWebView.getSettings();
        mCodeReview = new ICodeReview(this);
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(mCodeReview, "ICodeReview");
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setBuiltInZoomControls(true);
            settings.setUseWideViewPort(true);
        }
        String name = getIntent().getStringExtra(IntentExtra.KEY_NAME);
        String url = getIntent().getStringExtra(IntentExtra.KEY_PATH);
        mAppbar.setTitle(name);
        mCodeReview.file = name;
        new LoadHtmlTask(this).execute(url);
    }

    static class LoadHtmlTask extends AsyncTask<String, Integer, Object> {
        WeakReference<CodeReviewActivity> mActivity;

        public LoadHtmlTask(CodeReviewActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        protected Object doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();

                String contentType = connection.getContentType();
                if (contentType.startsWith("text")) {
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line;
                    StringBuilder text = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        text.append(line).append('\n');
                    }
                    return text.toString();
                }
                return url;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result == null) return;
            CodeReviewActivity activity = mActivity.get();
            if (activity != null) {
                if (result instanceof URL) {
                    activity.loadUrl(result.toString());
                } else {
                    activity.loadSource(result.toString());
                }
            }
        }
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void loadSource(String content) {
        mCodeReview.content = content;
        mWebView.loadUrl("file:///android_asset/code_review.html");
    }

    @OnClick(R.id.iv_code_review_setting)
    public void onMenuClick(View v) {
        Intent intent = new Intent(this, CodeStyleSettingActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadSource(mCodeReview.content);
    }

    public static class ICodeReview {
        String file;
        String content;
        String[] themes;

        SettingPreference mPreference;

        public ICodeReview(Context context) {
            mPreference = new SettingPreference(context);
            Resources resources = context.getResources();
            themes = resources.getStringArray(R.array.code_themes);
        }

        @JavascriptInterface
        public String getFile() {
            return file;
        }

        @JavascriptInterface
        public String getContent() {
            return content;
        }

        @JavascriptInterface
        public boolean lineWrapping() {
            return mPreference.isLineWrapping();
        }

        @JavascriptInterface
        public boolean lineNumbers() {
            return mPreference.isLineNumbers();
        }

        @JavascriptInterface
        public String getTheme() {
            int i = mPreference.getCodeThemes();
            return themes[i];
        }

        @JavascriptInterface
        public int getFontSize() {
            int fontSize = mPreference.getCodeFontSizeValue();
            return fontSize;
        }
    }
}
