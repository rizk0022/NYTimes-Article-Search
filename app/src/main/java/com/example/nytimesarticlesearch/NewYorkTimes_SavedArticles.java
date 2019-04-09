package com.example.nytimesarticlesearch;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * This class NewYorkTimes_SavedArticles is the activity to load the saved articles into Listview.
 * it will load the articles images with titles in the listview layout
 */
public class NewYorkTimes_SavedArticles extends AppCompatActivity {
    /**
     * isTablet boolean variable is used to implement tablet fragment
     * list1 variable is ListView of the saved article activity
     * messageFragment variable is instant of NewYorkTimes_ArticleFragment class
     */
    protected boolean isTablet;
    ListView list1;
    NewYorkTimes_ArticleFragment messageFragment;
    NewYorkTimes_MyDatabaseOpenHelper dbOpener;
    public static SQLiteDatabase db;
    NewYorkTimes_Article article;

    /**
     * This initiate the fields and implement the setOnItemClickListener when the article is clicked it open a fragment
     * in both tablet and mobile.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newyorktimes_saved_articles);
        list1 = (ListView) findViewById(R.id.list1);

        NewYorktimes_ArticleActivity.createAdapter(NewYorkTimes_SavedArticles.this);
        list1.setAdapter(NewYorktimes_ArticleActivity.saved_Adapter);
        dbOpener = new NewYorkTimes_MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();
        isTablet = (findViewById(R.id.fragmentLocation) != null);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle newBundle = new Bundle();
                newBundle.putString("headline", NewYorktimes_ArticleActivity.saved_Adapter.getItem(position).getHeadline());
                newBundle.putString("url", NewYorktimes_ArticleActivity.saved_Adapter.getItem(position).getWebUrl());
                newBundle.putString("pic_url", NewYorktimes_ArticleActivity.saved_Adapter.getItem(position).getThumbnail());
                newBundle.putInt("id", NewYorktimes_ArticleActivity.saved_Adapter.getPosition(NewYorktimes_ArticleActivity.saved_Adapter.getItem(position)));

                if (isTablet) {
                    messageFragment = new NewYorkTimes_ArticleFragment(NewYorkTimes_SavedArticles.this);
                    messageFragment.setArguments(newBundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentLocation, messageFragment);
                    ft.commit();
                } else {
                    Intent msgDetailsIntent = new Intent(getApplicationContext(), NewYorkTimes_ArticleDetails.class);
                    msgDetailsIntent.putExtras(newBundle);
                    startActivityForResult(msgDetailsIntent, CONTEXT_INCLUDE_CODE);
                }
            }
        });

    }

    /**
     * This method is called when deleting an article from the saved list's fragment
     */
    public void deleteItem(int id) {
        db.delete(NewYorkTimes_MyDatabaseOpenHelper.TABLE_NAME, NewYorkTimes_MyDatabaseOpenHelper.COL_ID + "=" + id, null);
        NewYorkTimes_Article position = NewYorktimes_ArticleActivity.saved_Adapter.getItem(id);
        NewYorktimes_ArticleActivity.saved_Articles.remove(position);

        NewYorktimes_ArticleActivity.saved_Adapter.notifyDataSetChanged();
    }

    /**
     * This method is called as a result of setResult() in the fragment class.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CONTEXT_INCLUDE_CODE) {
            int msgID = data.getIntExtra("msgID", -1);
            deleteItem(msgID);
        }
    }

}
