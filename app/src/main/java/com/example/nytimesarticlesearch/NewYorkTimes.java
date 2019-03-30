package com.example.nytimesarticlesearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * The NewYorkTimes class is the main activity of the application. This class extends AppCompatActivity.
 *
 * @author Youssef Rizk.
 * @version 1.
 */

public class NewYorkTimes extends AppCompatActivity {
    /**
     * progressBar variable is for the progress bar that is displayed when searching for articles and when clicking on an article from the list
     * etQuery variable is an EditText variable of the search query user input
     * gvResults variable is the ListView variable for the list of articles retrieved upon the search
     * articles variable is the ArrayList of articles retrieved upon the search
     * adapter variable is NewYorkTimes_ArticleArrayAdapter object to load that activity once an item clicked.
     * url variable is static finalString variable of nytimes.com website for article search
     * API_KEY variable is static final String variable of the API KEY belong to me.
     * sb variable is the Snackbar variable that display a Welcome snackbar message upon lunching the application
     * intent is an Intent variable to load NewYorktimes_ArticleActivity activity when an article title clicked.
     * article variable is NewYorkTimes_Article object that will be used when an article title clicked to load the article
     */
    ProgressBar progressBar;
    EditText etQuery;
    ListView gvResults;
    ArrayList<NewYorkTimes_Article> articles;
    NewYorkTimes_ArticleArrayAdapter adapter;
    private static final String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String API_KEY = "nGhORsp4W6LhNZnA1DtcYdeVv2Kp0l8r";
    Snackbar sb;
    Intent intent;
    NewYorkTimes_Article article;

    /**
     * This method initializes the NewYorkTimes activity.
     * fields are initialized, and a setOnItemClickListener is defined for the article item clicked.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newyorktimes_activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_activity);
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (ListView) findViewById(R.id.gvResults);
        progressBar = findViewById(R.id.progressBar);
        articles = new ArrayList<>();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        adapter = new NewYorkTimes_ArticleArrayAdapter(this, articles);

        gvResults.setAdapter(adapter);
        sb = Snackbar.make(toolbar,"Welcome to NYTimes Article Search",Snackbar.LENGTH_LONG);
        sb.show();
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), NewYorktimes_ArticleActivity.class);
                article = articles.get(position);
                alertDialog();
            }
        });
}

    /**
     * This method define the search of articles and retrieving them from the API using AsyncHttpClient and JSON object.
     * with exception handling of the results
     */

    private void loadMoreData(int offset) {
        String query = etQuery.getText().toString();
        Toast.makeText(this, "Searching for Articles", Toast.LENGTH_LONG).show();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("q", query);
        params.put("page", offset);
        client.get(url, params, new JsonHttpResponseHandler()  {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    progressBar.setVisibility(View.INVISIBLE);
                    adapter.addAll(NewYorkTimes_Article.fromJSONArray(articleJsonResults));

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        } );

        progressBar.setVisibility(View.VISIBLE);
    }
    /**
     * This method is used when Search button is clicked to call loadMoreData() method and make the search and get the articles from the API
     *
     */
    public void onArticleSearch(View view) {
        adapter.clear();
        loadMoreData(0);
    }

    /**
     * This method is for the dialog window used when the user click on the article item to inform that it will be redirected to NYTimes website
     * if user press on OK then it will redirect to NYTimes website and open the article
     * if user press on Cancel then it will go back to articles list.
     */
    public void alertDialog()
    {
        View middle = getLayoutInflater().inflate(R.layout.newyorktimes_custom_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                                intent.putExtra("article", article);
                                startActivity(intent);
                                progressBar.setVisibility(View.VISIBLE);
                    }

                }).setView(middle)

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }

    /**
     * This method is for onResume when the user go back to this activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }
}

