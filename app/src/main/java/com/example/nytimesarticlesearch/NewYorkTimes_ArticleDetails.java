package com.example.nytimesarticlesearch;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

/**
 * This class NewYorkTimes_ArticleDetails is for the fragment implementation in case not a tablet.
 */

public class NewYorkTimes_ArticleDetails extends AppCompatActivity {
    protected FrameLayout fl;
    protected NewYorkTimes_ArticleFragment messageFragment;

    /**
     * This method initiate the fields and commit the fragment transaction.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fl = new FrameLayout(this);
        fl.setId(R.id.fragmentLocation);
        setContentView(fl);

        messageFragment = new NewYorkTimes_ArticleFragment();
        messageFragment.setArguments(getIntent().getExtras());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLocation, messageFragment);
        ft.commit();
    }
}
