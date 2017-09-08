package binay.inshortapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import binay.inshortapp.R;

public class ArticleDetailsActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getIntent().getStringExtra("HOST") + "</font>"));
        //Load article URL
        WebView articleView = (WebView) findViewById(R.id.articleView);
        articleView.setWebViewClient(new MyBrowser());
        articleView.getSettings().setLoadsImagesAutomatically(true);
        articleView.getSettings().setJavaScriptEnabled(true);
        articleView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (getIntent().getStringExtra("URL") != null) {
            articleView.loadUrl(getIntent().getStringExtra("URL"));
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 3000);
    }

    private void init() {
        progressDialog = new ProgressDialog(ArticleDetailsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;

    }
}
