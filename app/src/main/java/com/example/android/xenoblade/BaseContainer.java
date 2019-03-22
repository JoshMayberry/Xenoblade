package com.example.android.xenoblade;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Use: https://stackoverflow.com/questions/18204190/java-abstract-classes-returning-this-pointer-for-derived-classes/39897781#39897781
//Must be public to use databinding
public class BaseContainer<T extends BaseContainer<T>> extends BaseObservable {
    String LOG_TAG = BaseContainer.class.getSimpleName();

    int indexPage = -1;
    String title = "";
    String subText = "";
    String urlPage = "";
    String urlImage = "";
    String urlSubImage = "";

    static final Pattern regexRemoveLink = Pattern.compile("<[^<]*>");


    //https://xenoblade.fandom.com/api/v1/Articles/List?category=Blades&limit=1000
    //https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=72205&prop=pageprops
    //https://xenoblade.fandom.com/api/v1/Articles/Details?ids=72205

    @Override
    public String toString() {
        return "BaseContainer{" +
                "title='" + title + '\'' +
                "subText='" + subText + '\'' +
                ", indexPage='" + indexPage + '\'' +
                ", urlPage='" + urlPage + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", urlSubImage='" + urlSubImage + '\'' +
                '}';
    }

    BaseContainer() {
    }

    //Getters
    //See: https://codelabs.developers.google.com/codelabs/android-databinding/index.html?index=..%2F..index#6
    //See: Android Data Binding Library - Update UI using Observable objects: https://www.youtube.com/watch?v=gP_zj-CIBvM
    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public String getSubText() {
        return subText;
    }

    String getImage() {
        return urlImage;
    }

    String getSubImage() {
        return urlSubImage;
    }

    int getIndexPage() {
        return indexPage;
    }

    Uri getPage() {
        if (urlPage.isEmpty()) {
            return null;
        }
        return Uri.parse(urlPage);
    }

    //Setters
    T setIndexPage(int indexPage) {
        this.indexPage = indexPage;
        return (T) this;
    }

    T setTitle(String text) {
        this.title = text;
        notifyPropertyChanged(BR.title);
        return (T) this;
    }

    T setSubText(String text) {
        this.subText = text;
//        notifyPropertyChanged(BR.subText);
        return (T) this;
    }

    T setPage(String url) {
        this.urlPage = url;
        return (T) this;
    }

    T setImage(String url) {
        this.urlImage = url;
        return (T) this;
    }

    T setSubImage(String url) {
        this.urlSubImage = url;
        return (T) this;
    }

    String getJsonUrlDetails() {
        if (indexPage == -1) {
            return null;
        }
        return "https://xenoblade.fandom.com/api/v1/Articles/Details?ids=" + indexPage;
    }

    /**
     * Can be overridden to parse it differently
     */
    boolean parseListData(JSONObject root, JSONObject number) throws JSONException {
        String url = number.getString("url");
        if (url.contains("Category:")) {
            return false;
        }

        String title = number.getString("title");
        if (title.contains(".")) {
            return false;
        }

        setIndexPage(number.getInt("id"));
        setTitle(title);
        setPage(root.getString("basepath") + url);
        return (indexPage != -1) && (!urlPage.isEmpty());
    }

    /**
     * Can be overridden to parse it differently
     */
    boolean parseInfoData(String jsonResponse) throws JSONException {
        Log.e(LOG_TAG, "@parseInfoData 1: " + this);
        JSONObject items = new JSONObject(jsonResponse)
                .getJSONObject("items");
        JSONObject number = items.getJSONObject(items.keys().next());
        setSubText(number.getString("abstract"));
        setImage(number.getString("thumbnail"));
        return true;
    }
}
