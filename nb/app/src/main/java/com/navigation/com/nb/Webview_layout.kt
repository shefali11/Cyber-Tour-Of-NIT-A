package com.navigation.com.nb

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView

class Webview_layout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_layout)
                val webView: WebView
                setContentView(R.layout.activity_webview_layout)
                webView = findViewById<View>(R.id.help_webView1) as WebView
                webView.settings.javaScriptEnabled = true
                webView.loadUrl("http://www.nita.ac.in/")


    }
}