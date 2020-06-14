package binay.inshortapp.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import binay.inshortapp.R;

/**
 * The type Article details activity.
 */
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

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (errorCode == 404 || errorCode == 500) {
                Toast.makeText(ArticleDetailsActivity.this, "There seems problem with article URL", Toast.LENGTH_LONG).show();
            }

        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
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
