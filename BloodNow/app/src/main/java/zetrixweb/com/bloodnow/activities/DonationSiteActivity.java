package zetrixweb.com.bloodnow.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import zetrixweb.com.bloodnow.R;

public class DonationSiteActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_site);

        webView = (WebView) findViewById(R.id.donation_web);

        webView.loadUrl("https://www.google.com/maps/search/blood+donation+center");

    }
}
