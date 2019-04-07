package com.example.nytimesarticlesearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * This class NewYorkTimes_SavedArticleWebView is the activity to display the article when url is clicked
 * in the fragment details
 */
public class NewYorkTimes_SavedArticleWebView extends AppCompatActivity {
    /**
     * This initiate the fields and implement the WebView of the article to display
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newyorktimes__saved_article_web_view);

        WebView view = (WebView) findViewById(R.id.wvArticlesaved);
        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        view.loadUrl(NewYorkTimes_ArticleFragment.urlTextToLoad);
    }

}
