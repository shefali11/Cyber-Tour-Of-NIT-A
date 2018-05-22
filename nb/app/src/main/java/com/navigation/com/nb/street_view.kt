package com.navigation.com.nb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView

class street_view : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_street_view)
        val webView: WebView
        webView = findViewById<View>(R.id.help_webView) as WebView
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("http://192.168.43.128/projects/indexpopup.html")
    }
}
