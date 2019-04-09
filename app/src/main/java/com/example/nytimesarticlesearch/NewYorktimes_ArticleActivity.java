package com.example.nytimesarticlesearch;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The NewYorktimes_ArticleActivity class is the activity opening NYTimes website link to read the article.
 * This class extends AppCompatActivity.
 */
public class NewYorktimes_ArticleActivity extends AppCompatActivity {
    /**
     * dbOpener variable is an instance of NewYorkTimes_MyDatabaseOpenHelper to access the database
     * db is an instance of SQLiteDatabase to execute the database
     * webView variable is the WebView variable for the article display
     * toolbar is Toolbar variable
     * results_saved variable is a Cursor object to execute the database
     * inflater variable is MenuInflater variable is used when menu icon's is clicked
     * url_string String variable for the URL of the article
     * header_title String variable for the article title
     * url_picture String variable for the article picture url
     * newId long is the id of the article
     * newRowValues variable is an instance of ContentValues used to add to the database and get the new ID
     * article is an instance of NewYorkTimes_Article class
     * saved_Articles variable is an ArrayList of articles NewYorkTimes_Article.
     * saved_Adapter variable is an instance of the adapter NewYorkTimes_SavedArticleArrayAdapter
     */
    NewYorkTimes_MyDatabaseOpenHelper dbOpener;
    public static SQLiteDatabase db;
    WebView webView;
    Toolbar toolbar;
    Cursor results_saved;
    MenuInflater inflater;
    String url_string;
    String header_title;
    public static String url_picture;
    long newId;
    ContentValues newRowValues;
    NewYorkTimes_Article article;
    public static ArrayList<NewYorkTimes_Article> saved_Articles = new ArrayList<NewYorkTimes_Article>();
    public static NewYorkTimes_SavedArticleArrayAdapter saved_Adapter;

    /**
     * This method initializes class fields and getting the URL string from NewYorkTimes_Article class.
     * setWebViewClient is used to open the link.
     */
    public static void createAdapter(Context cnt) {
        saved_Adapter = new NewYorkTimes_SavedArticleArrayAdapter(cnt, saved_Articles);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newyorktimes_activity_article);
        toolbar = findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        saved_Adapter = new NewYorkTimes_SavedArticleArrayAdapter(this, saved_Articles);
        article = (NewYorkTimes_Article) getIntent().getSerializableExtra("article");
        webView = findViewById(R.id.wvArticle);
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
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        inflater = getMenuInflater();
        inflater.inflate(R.menu.newyorktimes_menu_search, menu);

        return true;
    }

    /**
     * This method is used when About menu icon is clicked to open a dialog with the Application information and how to use it.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.info_details:
                infoDialog();
                break;
            case R.id.save_icon:
                onSaveIcon();
                Toast.makeText(this, getResources().getString(R.string.article_saving), Toast.LENGTH_LONG).show();
                break;
        }
        return false;

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void onSaveIcon() {
        //get a database:
        dbOpener = new NewYorkTimes_MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();
        //query all the results from the database:
        String[] columns = {NewYorkTimes_MyDatabaseOpenHelper.COL_ID, NewYorkTimes_MyDatabaseOpenHelper.COL_HEADER, NewYorkTimes_MyDatabaseOpenHelper.COL_URL, NewYorkTimes_MyDatabaseOpenHelper.COL_PIC_URL};
        results_saved = db.query(false, NewYorkTimes_MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        header_title = article.getHeadline();
        url_string = article.getWebUrl();
        url_picture = article.getThumbnail();

        //add to the database and get the new ID
        newRowValues = new ContentValues();
        //put strings in columns:
        newRowValues.put(NewYorkTimes_MyDatabaseOpenHelper.COL_HEADER, header_title);
        newRowValues.put(NewYorkTimes_MyDatabaseOpenHelper.COL_URL, url_string);
        newRowValues.put(NewYorkTimes_MyDatabaseOpenHelper.COL_PIC_URL, url_picture);
        newId = db.insert(NewYorkTimes_MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
        //add the new Contact to the array list:
        saved_Articles.add(article);
        saved_Adapter.notifyDataSetChanged();
    }

    /**
     * This method to form the dialog that will open when About menu icon is clicked.
     */
    public void infoDialog() {
        View middle_info = getLayoutInflater().inflate(R.layout.newyorktimes_custom_dialog_info, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onPause();
            }
        }).setView(middle_info);
        builder.create().show();
    }


}