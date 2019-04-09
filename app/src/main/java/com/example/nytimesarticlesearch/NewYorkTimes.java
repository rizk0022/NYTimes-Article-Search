package com.example.nytimesarticlesearch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
    Button saved_list;
    NewYorkTimes_ArticleArrayAdapter adapter;
    private static final String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String API_KEY = "nGhORsp4W6LhNZnA1DtcYdeVv2Kp0l8r";
    Snackbar sb;
    Intent intent;
    SharedPreferences sp;
    Intent saved_List_Intent;
    AsyncHttpClient client;
    String query;
    RequestParams params;
    NewYorkTimes_MyDatabaseOpenHelper dbOpener;
    public static SQLiteDatabase db;
    NewYorkTimes_Article article;


    /**
     * This method initializes the NewYorkTimes activity.
     * fields are initialized, and a setOnItemClickListener is defined for the article item clicked.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newyorktimes_activity_search);

        //get a database:
        dbOpener = new NewYorkTimes_MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();
        //query all the results from the database:
        String[] columns = {NewYorkTimes_MyDatabaseOpenHelper.COL_ID, NewYorkTimes_MyDatabaseOpenHelper.COL_HEADER, NewYorkTimes_MyDatabaseOpenHelper.COL_URL, NewYorkTimes_MyDatabaseOpenHelper.COL_PIC_URL};
        Cursor results_saved = db.query(false, NewYorkTimes_MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        while(results_saved.moveToNext())
        {
            int headerindex = results_saved.getColumnIndex(NewYorkTimes_MyDatabaseOpenHelper.COL_HEADER);
            String header = results_saved.getString(headerindex);
            int urlindex = results_saved.getColumnIndex(NewYorkTimes_MyDatabaseOpenHelper.COL_URL);
            String url = results_saved.getString(urlindex);
            int urlpictureindex = results_saved.getColumnIndex(NewYorkTimes_MyDatabaseOpenHelper.COL_PIC_URL);
            String urlpicture = results_saved.getString(urlpictureindex);
            //add the new Contact to the array list:
            article = new NewYorkTimes_Article(  url,header, urlpicture );
            NewYorktimes_ArticleActivity.saved_Articles.add(article);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_activity);
        etQuery = (EditText) findViewById(R.id.etQuery);
        saved_list = (Button) findViewById(R.id.savedArticles);
        gvResults = (ListView) findViewById(R.id.gvResults);
        sp = getSharedPreferences("searchQuery", Context.MODE_PRIVATE);
        String searchText = sp.getString("searchQuery", "");
        etQuery.setText(searchText);

        progressBar = findViewById(R.id.progressBar);
        articles = new ArrayList<>();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ForecastQuery networkThreadOne = new ForecastQuery();
        adapter = new NewYorkTimes_ArticleArrayAdapter(this, articles);
        saved_List_Intent = new Intent(NewYorkTimes.this, NewYorkTimes_SavedArticles.class);
        gvResults.setAdapter(adapter);
        sb = Snackbar.make(toolbar, getResources().getString(R.string.nytimes_welcome), Snackbar.LENGTH_LONG);
        sb.show();
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), NewYorktimes_ArticleActivity.class);
                article = articles.get(position);
                alertDialog();
            }
        });

        gvResults.setOnScrollListener(new NewYorkTimes_EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadMoreData(page);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        saved_list.setOnClickListener(btn -> {
            startActivity(saved_List_Intent);
        });
    }

    /**
     * This method define the search of articles and retrieving them from the API using AsyncHttpClient and JSON object.
     * with exception handling of the results
     */

    private void loadMoreData(int offset) {
        query = etQuery.getText().toString();
        try {
            URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, getResources().getString(R.string.article_searching), Toast.LENGTH_LONG).show();
        ForecastQuery networkThreadOne = new ForecastQuery();
        client = new AsyncHttpClient();
        params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("q", query);
        params.put("page", offset);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    progressBar.setVisibility(View.INVISIBLE);
                    adapter.addAll(NewYorkTimes_Article.fromJSONArray(articleJsonResults));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        progressBar.setVisibility(View.VISIBLE);

    }

    /**
     * This method is used when Search button is clicked to call loadMoreData() method and make the search and get the articles from the API
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
    public void alertDialog() {
        View middle = getLayoutInflater().inflate(R.layout.newyorktimes_custom_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("")
                .setPositiveButton(getResources().getString(R.string.article_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                        intent.putExtra("article", article);
                        startActivity(intent);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                }).setView(middle)

                .setNegativeButton(getResources().getString(R.string.article_cancel), new DialogInterface.OnClickListener() {
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

    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("searchQuery", etQuery.getText().toString());
        edit.commit();
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        String min, max, current, iconName;
        Bitmap icon;
        String API_KEY = "nGhORsp4W6LhNZnA1DtcYdeVv2Kp0l8r";
        String query = etQuery.getText().toString();
        int offset;
        JSONObject jObject;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {

                    //Start of JSON reading of UV factor:

                    //create the network connection:
                    URL UVurl = new URL("http://api.nytimes.com/svc/search/v2/articlesearch.json");
                    HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                    InputStream inStream = UVConnection.getInputStream();


                    //create a JSON object from the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString();
                    query = etQuery.getText().toString();
                    //now a JSON table:
                    jObject = new JSONObject(result);
                    API_KEY = jObject.getString("api-key");
                    query = jObject.getString("q");
                    offset = jObject.getInt("page");
                    JSONArray articleJsonResults = null;
                    try {
                        articleJsonResults = jObject.getJSONObject("response").getJSONArray("docs");
                        progressBar.setVisibility(View.INVISIBLE);
                        adapter.addAll(NewYorkTimes_Article.fromJSONArray(articleJsonResults));
                        progressBar.setVisibility(View.INVISIBLE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    publishProgress(125);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("Crash!!", ex.getMessage());
            }
            return null;


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
        }

    }}