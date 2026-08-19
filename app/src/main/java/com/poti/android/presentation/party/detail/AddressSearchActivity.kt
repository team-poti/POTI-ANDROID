package com.poti.android.presentation.party.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class AddressSearchActivity : ComponentActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest,
                ): Boolean = handleAddressResult(request.url)
            }

            loadDataWithBaseURL(
                POSTCODE_BASE_URL,
                POSTCODE_HTML,
                HTML_MIME_TYPE,
                Charsets.UTF_8.name(),
                null,
            )
        }

        val rootView = FrameLayout(this).apply {
            addView(
                webView,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                ),
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.displayCutout(),
            )

            view.updatePadding(
                left = systemBars.left,
                top = systemBars.top,
                right = systemBars.right,
                bottom = systemBars.bottom,
            )

            insets
        }

        setContentView(rootView)
        ViewCompat.requestApplyInsets(rootView)
    }

    override fun onDestroy() {
        webView.stopLoading()
        webView.webViewClient = WebViewClient()
        webView.destroy()
        super.onDestroy()
    }

    private fun handleAddressResult(uri: Uri): Boolean {
        if (uri.scheme != RESULT_SCHEME || uri.host != RESULT_HOST) return false

        val postalCode = uri.getQueryParameter(QUERY_POSTAL_CODE).orEmpty()
        val address = uri.getQueryParameter(QUERY_ADDRESS).orEmpty()
        if (postalCode.isBlank() || address.isBlank()) return true

        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(EXTRA_POSTAL_CODE, postalCode)
                putExtra(EXTRA_ADDRESS, address)
            },
        )
        finish()
        return true
    }

    companion object {
        const val EXTRA_POSTAL_CODE = "extra_postal_code"
        const val EXTRA_ADDRESS = "extra_address"

        private const val POSTCODE_BASE_URL = "https://postcode.map.kakao.com/"
        private const val HTML_MIME_TYPE = "text/html"
        private const val RESULT_SCHEME = "poti"
        private const val RESULT_HOST = "postcode"
        private const val QUERY_POSTAL_CODE = "zonecode"
        private const val QUERY_ADDRESS = "address"

        private val POSTCODE_HTML =
            """
            <!doctype html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <script src="https://t1.kakaocdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
            </head>
            <body style="margin:0">
                <div id="postcode" style="width:100%; height:100vh;"></div>
                <script>
                    new kakao.Postcode({
                        oncomplete: function(data) {
                            var address = data.userSelectedType === 'R'
                                ? data.roadAddress
                                : data.jibunAddress;
                            location.href = 'poti://postcode?zonecode=' +
                                encodeURIComponent(data.zonecode) +
                                '&address=' + encodeURIComponent(address);
                        },
                        width: '100%',
                        height: '100%'
                    }).embed(document.getElementById('postcode'));
                </script>
            </body>
            </html>
            """.trimIndent()
    }
}
