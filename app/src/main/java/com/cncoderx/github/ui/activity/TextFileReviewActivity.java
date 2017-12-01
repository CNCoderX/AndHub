package com.cncoderx.github.ui.activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.preference.SettingPreference;
import com.cncoderx.github.ui.view.AppBar;
import com.cncoderx.github.utils.IntentExtra;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TextFileReviewActivity extends BaseActivity {
    @BindView(R.id.app_bar)
    AppBar mAppbar;

    @BindView(R.id.content)
    WebView mWebView;

    private boolean wrap;
    private SettingPreference mPreference;

    private static final String JS_PATH = "file:///android_asset/code_prettify/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_file_review);
        ButterKnife.bind(this);
        mPreference = new SettingPreference(this);
        wrap = mPreference.isCodeWrapped();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "TextFileReview");
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setBuiltInZoomControls(true);
            settings.setUseWideViewPort(true);
        }
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
        String name = getIntent().getStringExtra(IntentExtra.KEY_NAME);
        String url = getIntent().getStringExtra(IntentExtra.KEY_PATH);
        mAppbar.setTitle(name);
        new LoadHtmlTask(mWebView, getCodeThemeCss(), "prettify.js").execute(url);
    }

    static class TaskResult {
        String url;
        String text;

        public TaskResult(String url, String text) {
            this.url = url;
            this.text = text;
        }
    }

    static class LoadHtmlTask extends AsyncTask<String, Integer, TaskResult> {
        WeakReference<WebView> wrefWebView;
        String css;
        String js;

        public LoadHtmlTask(WebView webView, String css, String js) {
            wrefWebView = new WeakReference<>(webView);
            this.css = css;
            this.js = js;
        }

        @Override
        protected TaskResult doInBackground(String... params) {
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
                    StringBuffer text = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        text.append(line).append('\n');
                    }
                    return new TaskResult(url.toString(), text.toString());
                }
                return new TaskResult(url.toString(), null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(TaskResult result) {
            if (result == null) return;

            WebView webView = wrefWebView.get();
            if (webView == null) return;
            if (result.text == null) {
                if (result.url != null) {
                    webView.loadUrl(result.url);
                }
            } else {
                StringBuilder html = new StringBuilder();
                html.append("<!DOCTYPE html>").append('\n')
                        .append("<html>").append('\n')
                        .append("<head>").append('\n')
                        .append("<link rel=\"stylesheet\" href=\"")
                        /*.append(JS_PATH)*/.append(css).append("\"/>\n")
                        .append("<script type=\"text/javascript\" src=\"")
                        /*.append(JS_PATH)*/.append(js).append("\"></script>\n")
                        .append("</head>").append('\n')
                        .append("<body onload=\"prettyPrint()\">").append('\n')
                        .append("<pre class=\"prettyprint\">").append('\n')
                        .append(StringEscapeUtils.escapeHtml(result.text)).append('\n')
                        .append("</pre>").append('\n')
                        .append("</body>").append('\n')
                        .append("</html>");
                webView.loadDataWithBaseURL(JS_PATH, html.toString(), "text/html", "utf-8", null);
            }
        }
    }

    public String getCodeThemeCss() {
        String css;
        int theme = new SettingPreference(this).getCodeThemes();
        switch (theme) {
            case 1:
                css = "desert.css";
                break;
            case 2:
                css = "doxy.css";
                break;
            case 3:
                css = "sons-of-obsidian.css";
                break;
            case 4:
                css = "sunburst.css";
                break;
            default:
                css = "prettify.css";
                break;
        }
        return css;
    }

    @OnClick(R.id.iv_content_menu)
    public void onMenuClick(View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.review_menu_layout, null);
        TextView tvItem = (TextView) view.findViewById(R.id.tv_review_menu_wrapped);
        if (wrap) {
            tvItem.setText(R.string.disable_wrapping);
        } else {
            tvItem.setText(R.string.enable_wrapping);
        }
        final PopupWindow window = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));
        window.setAnimationStyle(R.style.popup_menu_anim);
        PopupWindowCompat.showAsDropDown(window, mAppbar, 0, 0, Gravity.RIGHT);
        tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrap = !wrap;
                mPreference.setCodeWrapped(wrap).apply();
                window.dismiss();
            }
        });
    }

    @JavascriptInterface
    public boolean getWrap() {
        return wrap;
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mWebView != null) {
//            mWebView.pauseTimers();
//            mWebView.onHide();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mWebView != null) {
//            mWebView.resumeTimers();
//            mWebView.onShow();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mWebView != null) {
//            mWebView.onDestroy();
//        }
//    }
}
