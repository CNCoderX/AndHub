package com.cncoderx.andhub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.preference.SettingPreference;
import com.cncoderx.andhub.ui.view.AppBar;
import com.cncoderx.andhub.utils.IntentExtra;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CodeReviewActivity extends BaseActivity {
    private AppBar mAppbar;
    private WebView mWebView;
    private ICodeReview mCodeReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_review_activity);
        mAppbar = (AppBar) findViewById(R.id.app_bar);
        mWebView = (WebView) findViewById(R.id.wv_code_review_content);

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
        buildConnection(url);
    }

    private void buildConnection(String url) {
        Single.just(url)
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String _url) throws Exception {
                        URL url = new URL(_url);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        try {
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
                        } finally {
                            connection.disconnect();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object result) throws Exception {
                        if (result instanceof URL) {
                            loadUrl(result.toString());
                        } else {
                            loadSource(result.toString());
                        }
                    }
                });
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void loadSource(String content) {
        mCodeReview.content = content;
        mWebView.loadUrl("file:///android_asset/code_review.html");
    }

    public void onMenu(View v) {
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
