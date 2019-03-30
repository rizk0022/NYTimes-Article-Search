package com.example.nytimesarticlesearch;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * The NewYorktimes_ArticleActivity class is the activity opening NYTimes website link to read the article.
 * This class extends AppCompatActivity.
 */
public class NewYorktimes_ArticleActivity extends AppCompatActivity {
    /**
     * This method initializes class fields and getting the URL string from NewYorkTimes_Article class.
     * setWebViewClient is used to open the link.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newyorktimes_activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        NewYorkTimes_Article article = (NewYorkTimes_Article) getIntent().getSerializableExtra("article");
        WebView webView = (WebView) findViewById(R.id.wvArticle);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(article.getWebUrl());
    }
    /**
     * This method inflate the menu with About icon.
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newyorktimes_menu_search, menu);

        return true;
    }
    /**
     * This method is used when About menu icon is clicked to open a dialog with the Application information and how to use it.
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.info_details:
                infoDialog();

                break;

        }
        return true;
    }
    /**
     * This method to form the dialog that will open when About menu icon is clicked.
     *
     */
    public void infoDialog()
    {
        View middle_info = getLayoutInflater().inflate(R.layout.newyorktimes_custom_dialog_info, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
            }).setView(middle_info);
        builder.create().show();
    }
}