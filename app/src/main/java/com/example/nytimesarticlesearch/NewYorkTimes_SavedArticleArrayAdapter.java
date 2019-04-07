package com.example.nytimesarticlesearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This class NewYorkTimes_ArticleArrayAdapter is the activity to load the search results of articles into Listview.
 * it will load the articles images with titles in the listview layout
 */
public class NewYorkTimes_SavedArticleArrayAdapter extends ArrayAdapter<NewYorkTimes_Article> {
    /**
     * This is the constructor of NewYorkTimes_ArticleArrayAdapter class
     */
    public NewYorkTimes_SavedArticleArrayAdapter(Context context, List<NewYorkTimes_Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    /**
     * This getView method will inflate the layout newyorktimes_item_article_result with the results
     * this method is using Picasso package to show the thumbnail results.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewYorkTimes_Article article = this.getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.newyorktimes_item_saved_article, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage1);

        imageView.setImageResource(0);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle1);
        tvTitle.setText(article.getHeadline());
        String thumbnail = article.getThumbnail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView;

    }
}
