package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fara.inkamapp.R;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class CrispWebView extends AppCompatActivity {
    private WebView webView;
    private String url = "http://www.income-app.ir/crisp.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crisp_web_view);
        webView = findViewById(R.id.web_view);

        try {
            webView.setWebViewClient(new CustomWebView());

            String url = addLocationToUrl("http://income-app.ir/crisp.cshtml");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("http://income-app.ir/crisp.cshtml"); // load a web page in a web view


        }
        catch (Exception e) {
            Log.e("error : ", e.toString());
        }
    }

    private class CustomWebView extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {


            if (url.matches("yahoo.com")) {

                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();

            }

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();

            super.onReceivedError(view, request, error);
        }
    }

    String addLocationToUrl(String _url) throws NoSuchPaddingException, NoSuchAlgorithmException {
        if (!_url.endsWith("?"))
            _url += "?";
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return _url;
    }

}