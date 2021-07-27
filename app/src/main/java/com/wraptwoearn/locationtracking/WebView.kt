package com.wraptwoearn.locationtracking

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wraptwoearn.appcontroller.AppController
import com.wraptwoearn.util.PrefrenceHelper
import kotlinx.android.synthetic.main.activity_web_view.*

@SuppressLint("SetJavaScriptEnabled")
class WebView : AppCompatActivity() {

    private var isLoaded: Boolean = false
    private var doubleBackToExitPressedOnce = false
    private var webURL =
        "https://wrap2earn.com/in/driver-rank.html?id=" + PrefrenceHelper(AppController.instance.applicationContext).scratch_card_id.toString() // Change it with your URL


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webview.settings.javaScriptEnabled = true
        loadWebView()
    }

    override fun onResume() {
        loadWebView()
        super.onResume()
    }

    private fun loadWebView() {
        webview.loadUrl(webURL)
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: android.webkit.WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(
                view: android.webkit.WebView?,
                url: String?,
                favicon: Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                isLoaded = true
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                isLoaded = false
            }
        }
    }

}