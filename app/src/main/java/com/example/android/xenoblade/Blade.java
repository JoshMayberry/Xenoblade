package com.example.android.xenoblade;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Must be public to use databinding
public class Blade extends BaseObservable {
    private String LOG_TAG = Blade.class.getSimpleName();

    private int pageIndex = -1;
    private String title = "";
    private String urlPage = "";
    private String urlPortrait = "";
    private String urlElement = "";
    private String urlRarity = "";

    private Bitmap bmpPortrait = null;
    private Bitmap bmpElement = null;
    private Bitmap bmpRarity = null;

    private static final Pattern regexRemoveLink = Pattern.compile("<[^<]*>");

    //https://xenoblade.fandom.com/api/v1/Articles/List?category=Blades&limit=1000
    //https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=72205&prop=pageprops
    //https://xenoblade.fandom.com/api/v1/Articles/Details?ids=72205

    @Override
    public String toString() {
        return "Blade{" +
                "title='" + title + '\'' +
                "pageIndex='" + pageIndex + '\'' +
                ", urlPage='" + urlPage + '\'' +
                ", urlPortrait='" + urlPortrait + '\'' +
                ", urlElement='" + urlElement + '\'' +
                ", urlRarity='" + urlRarity + '\'' +
                '}';
    }

    public Blade() {
    }

    //Getters
    //See: https://codelabs.developers.google.com/codelabs/android-databinding/index.html?index=..%2F..index#6
    //See: Android Data Binding Library - Update UI using Observable objects: https://www.youtube.com/watch?v=gP_zj-CIBvM
    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public Bitmap getPortrait() {
        return bmpPortrait;
    }

    @Bindable
    public Bitmap getElement() {
        return bmpElement;
    }

    @Bindable
    public Bitmap getRarity() {
        return bmpRarity;
    }

    int getPageIndex() {
        return pageIndex;
    }

    Uri getPage() {
        return Uri.parse(urlPage);
    }

    //Setters
    void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    Blade setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
        return this;
    }

    Blade setUrlPage(String urlPage) {
        this.urlPage = urlPage;
        return this;
    }

    Blade setPortrait(String urlPortrait) {
        this.urlPortrait = urlPortrait;
        this.bmpPortrait = QueryUtilities.getImageFromURL(urlPortrait);
        return this;
    }

    Blade setElement(String urlElement) {
        this.urlElement = urlElement;
        this.bmpElement = QueryUtilities.getImageFromURL(urlElement);
        return this;
    }

    Blade setRarity(String urlRarity) {
        this.urlRarity = urlRarity;
        this.bmpRarity = QueryUtilities.getImageFromURL(urlRarity);
        return this;
    }

    //Methods

    /**
     * For some reason, the databinding is not applying the titles when notifyPropertyChanged is in the setter.
     */
    void forcePropertyChanged() {
        Log.e(LOG_TAG, "title: " + title);
        notifyPropertyChanged(BR.title);
    }

    static String getJsonUrlList() {
        return "https://xenoblade.fandom.com/api/v1/Articles/List?category=Blades&limit=1000";
    }

    String getJsonUrlDetails() {
        if (pageIndex == -1) {
            return null;
        }
        return "https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=" + pageIndex + "&prop=pageprops";
    }

    /**
     * Parses JSON data from https://xenoblade.fandom.com/api/v1/Articles/List?category=Blades&limit=1000
     */
    boolean parseListData(JSONObject root, JSONObject number) throws JSONException, IOException {
        String url = number.getString("url");
        if (url.contains("Category:")) {
            return false;
        }

        String title = number.getString("title");
        if (title.contains(".")) {
            return false;
        }

        setPageIndex(number.getInt("id"));
        setTitle(title);
        setUrlPage(root.getString("basepath") + url);
        return true;
    }

    /**
     * Parses JSON data from https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=" + pageIndex + "&prop=pageprops"
     * See: http://www.tutorialspoint.com/android/android_json_parser.htm
     */
    boolean parseInfoData(String jsonResponse) throws JSONException {
        if (jsonResponse == null) {
            Log.e(LOG_TAG, "Get JSON error");
            return false;
        }
        JSONObject pages = new JSONObject(jsonResponse)
                .getJSONObject("query")
                .getJSONObject("pages");

        JSONObject pageItem = pages.getJSONObject(pages.keys().next());
        if (!pageItem.has("pageprops")) {
            return false;
        }
        JSONObject pageprops = pageItem.getJSONObject("pageprops");
        if (!pageprops.has("infoboxes")) {
            return false;
        }
        String infoBoxesRaw = pageprops.getString("infoboxes");
        if (infoBoxesRaw == null || infoBoxesRaw.length() < 1) {
            return false;
        }
        JSONArray infoboxes = new JSONArray(infoBoxesRaw);
        JSONArray data = infoboxes.getJSONObject(0)
                .getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject dataItem = data.getJSONObject(i);
            switch (dataItem.getString("type")) {
                case "image":
                    setPortrait(dataItem.getJSONArray("data")
                            .getJSONObject(0)
                            .getString("url"));
                    continue;

                case "group":
                    JSONArray subDataList = dataItem.getJSONObject("data")
                            .getJSONArray("value");
                    for (int j = 0; j < subDataList.length(); j++) {
                        JSONObject value = subDataList.getJSONObject(j)
                                .getJSONObject("data");

                        //See: https://stackoverflow.com/questions/6384240/how-to-parse-a-url-from-a-string-in-android/26426891#26426891
                        Matcher matcher = Patterns.WEB_URL.matcher(value.getString("value"));
                        if (matcher.find()) {
                            switch (value.getString("source")) {
                                case "element":
                                    setElement(matcher.group());
                                    continue;
                                case "rarity":
                                    setRarity(matcher.group());
                            }
                        }
                    }
            }
        }
        return true;
    }
}
