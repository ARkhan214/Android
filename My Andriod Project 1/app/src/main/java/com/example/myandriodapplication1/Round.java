package com.example.myandriodapplication1;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Round extends AppCompatActivity {

    private WebView webRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_round);

        webRound = findViewById(R.id.webRound);

        WebSettings webSettings = webRound.getSettings();
        webRound.setWebViewClient(new SameVieww());
        webSettings.setJavaScriptEnabled(true);
        webRound.loadUrl("https://isdb-bisew.org/it-scholarship-programme/it-scholarship-unique-features");

    }

    public class SameVieww extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}