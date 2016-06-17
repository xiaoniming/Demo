package com.seu.ni.demo.Web;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.seu.ni.demo.R;

public class WebViewActivity extends AppCompatActivity {

    private final String address = "http://192.168.0.113:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        usingHttpURLConnection();

    }


    public void usingWebView() {
        WebView webView = (WebView) findViewById(R.id.wv_demo);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.baidu.com");
    }

    public void usingHttpURLConnection() {
        final TextView textView = (TextView) findViewById(R.id.tv_web);
        textView.setVisibility(View.VISIBLE);
        HttpUtil.SendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                textView.setText(response);
            }

            @Override
            public void onErr(Exception e) {
                textView.setText(e.toString());
            }
        });
    }
}
