package com.navigation.com.nb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class shivam_linked : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shivam_linked)
        val webView: WebView
        setContentView(R.layout.activity_shivam_linked)
        webView = findViewById<View>(R.id.webView1) as WebView
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl("https://www.linkedin.com/in/shivam-singh-rajawat-80b7a5112/")
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                super.shouldOverrideUrlLoading(view, url)
                view.loadUrl(url)
                return true
            }
        }

    }
}
