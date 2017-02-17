package com.natarajan.easycommunication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class HelpActivity extends AppCompatActivity {
    WebView webView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_act);
        this.webView = (WebView) findViewById(R.id.webView1);
        this.webView.loadUrl("file:///android_asset/launch.html");
    }
}
