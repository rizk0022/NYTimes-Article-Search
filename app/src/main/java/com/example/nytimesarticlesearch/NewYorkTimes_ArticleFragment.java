package com.example.nytimesarticlesearch;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * The NewYorkTimes_ArticleFragment class is the activity opening a fragment when item is clicked on Saved List articles.
 * This class extends Fragment.
 */
public class NewYorkTimes_ArticleFragment extends Fragment {
    /**
     * image_article variable is for the image of the article ImageView
     * title_article variable is for the title of the article TextView
     * url_article variable is for the url TextView
     * deleteButton variable is for the button delete
     * intent is an Intent variable to use when delete button is pressed
     * urlTextToLoad variable is a url String for the article to load
     * articleID variable is the ID of the article in the fragment
     * urlText variable is used with the Bundle object for the url text.
     */
    protected ImageView image_article;
    protected TextView title_article;
    protected TextView url_article;
    protected Button deleteButton;
    Intent intent;
    public static String urlTextToLoad;
    int articleID;
    String urlText;
    protected NewYorkTimes_SavedArticles article_window;

    public NewYorkTimes_ArticleFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public NewYorkTimes_ArticleFragment(NewYorkTimes_SavedArticles article_window) {
        super();
        this.article_window = article_window;
    }

    /**
     * This method initializes class fields and setup the view of the fragment.
     * also it setup the delete button.
     */

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.newyorktimes_article_fragment, container, false);
        final Bundle args = getArguments();
        image_article = (ImageView) view.findViewById(R.id.ivImage_fragment);
        title_article = (TextView) view.findViewById(R.id.article_title_fragment);
        url_article = (TextView) view.findViewById(R.id.article_url_fragment);
        deleteButton = (Button) view.findViewById(R.id.btn_delete);
        String titleText = "Article Title:" + " " + args.getString("headline");
        urlText = "URL: " + " " + args.getString("url");
        articleID = args.getInt("id");
        String thumbnail = args.getString("pic_url");
        Picasso.with(getContext()).load(thumbnail).into(image_article);
        title_article.setText(titleText);
        url_article.setText(urlText);
        urlTextToLoad = args.getString("url");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (article_window == null) {
                    Intent msgDetailsIntent = new Intent(getActivity(), NewYorkTimes_ArticleFragment.class);
                    msgDetailsIntent.putExtra("msgID", articleID);

                    getActivity().setResult(NewYorkTimes_SavedArticles.CONTEXT_INCLUDE_CODE, msgDetailsIntent);
                    getActivity().finish();
                } else {
                    article_window.deleteItem(articleID);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.remove(NewYorkTimes_ArticleFragment.this);
                    ft.commit();
                }
            }
        });

        url_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), NewYorkTimes_SavedArticleWebView.class);
                startActivity(intent);
            }
        });
        return view;
    }
}


