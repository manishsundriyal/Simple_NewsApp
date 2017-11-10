package com.example.sundriyal.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by Sundriyal on 11/9/2017.
 */

public class DisplayNewsActivity extends AppCompatActivity{

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_news);
        webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String UrlToDisplay = intent.getStringExtra("URL");
        if (UrlToDisplay != "") {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(UrlToDisplay);
        }
    }
}
