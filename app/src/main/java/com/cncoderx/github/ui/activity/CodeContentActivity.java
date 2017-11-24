package com.cncoderx.github.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cncoderx.github.R;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CodeContentActivity extends BaseActivity {
    @BindView(R.id.content)
    WebView mContent;

    private static final String JS_PATH = "file:///android_asset/code_prettify/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_content);
        ButterKnife.bind(this);
        WebSettings settings = mContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
        String url = getIntent().getDataString();
        new LoadHtmlTask(mContent).execute(url);
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

        public LoadHtmlTask(WebView webView) {
            wrefWebView = new WeakReference<>(webView);
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
                        /*.append(JS_PATH)*/.append("prettify.css").append("\"/>\n")
                        .append("<script type=\"text/javascript\" src=\"")
                        /*.append(JS_PATH)*/.append("prettify.js").append("\"></script>\n")
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

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mContent != null) {
//            mContent.pauseTimers();
//            mContent.onHide();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mContent != null) {
//            mContent.resumeTimers();
//            mContent.onShow();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mContent != null) {
//            mContent.onDestroy();
//        }
//    }
}
