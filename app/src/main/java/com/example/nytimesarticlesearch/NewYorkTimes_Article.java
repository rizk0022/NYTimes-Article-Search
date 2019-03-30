package com.example.nytimesarticlesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The NewYorkTimes_Article class is the activity forming NYTimes website link to read the article.
 * This class implements Serializable.
 */
public class NewYorkTimes_Article implements Serializable {
    /**
     * webUrl variable is String used to get the url from JsonObject
     * headline variable is String of the headline we get from JsonObject
     * thubnail variable is String of the link address of the article
     */
    String webUrl;
    public static String id = "article";

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    String headline;
    String thumbnail;
    /**
     * this method is to open the article link from nytimes website using JSONObject to get the link
     */
    public NewYorkTimes_Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbnail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbnail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * this method is to add the article to JSONArray
     */
    public static ArrayList<NewYorkTimes_Article> fromJSONArray(JSONArray array) {
        ArrayList<NewYorkTimes_Article> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new NewYorkTimes_Article(array.getJSONObject(x)));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        return results;
    }
}
