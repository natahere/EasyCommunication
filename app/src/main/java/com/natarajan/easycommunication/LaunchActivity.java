package com.natarajan.easycommunication;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

public class LaunchActivity extends AppCompatActivity {
    WebView webView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_act);
        if (Boolean.valueOf(getSharedPreferences("hasRunBefore", 0).getBoolean("hasRun", false)).booleanValue()) {
            startActivity(new Intent("android.intent.action.APPACTIVITY"));
            finish();
            return;
        }
        Editor edit = getSharedPreferences("hasRunBefore", 0).edit();
        edit.putBoolean("hasRun", true);
        edit.commit();
        setContentView(R.layout.launch_act);
        this.webView = (WebView) findViewById(R.id.webView1);
        this.webView.loadUrl("file:///android_asset/launch.html");
    }

    public void onLaunch(View view) {
        startActivity(new Intent("android.intent.action.APPACTIVITY"));
        finish();
    }
}
