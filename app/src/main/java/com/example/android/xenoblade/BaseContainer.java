package com.example.android.xenoblade;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Must be public to use databinding
/**
 * Holds data pertaining to a list item for a {@link BaseAdapter}
 * This is a Generic class that is meant to be extended by a child class.
 *
 * Some methods (such as {@link #getContainerList}) have a default action.
 * They can be overridden by children to modify behavior.
 *
 * @param <T> What child to use for the fragment
 * @see Item
 * @see Blade
 * @see Location
 * @see HeartToHeart
 *
 * Use: https://stackoverflow.com/questions/18204190/java-abstract-classes-returning-this-pointer-for-derived-classes/39897781#39897781
 */
public abstract class BaseContainer<T extends BaseContainer<T>> extends BaseObservable {
    String LOG_TAG = BaseContainer.class.getSimpleName();

    int indexPage = -1;
    String title = "";
    String subText = "";
    String urlPage = "";
    String urlImage = "";
    String urlSubImage = "";

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
        notifyPropertyChanged(BR.subText);
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

    /**
     * Returns a list of sub-items for this container.
     * If there are no sub items, then just return a list of length 1 that contains {@code this}.
     * Can be overridden to parse the {@link JSONObject}s differently.
     * @see #populateContainerInfo
     */
    List<T> getContainerList(JSONObject root, JSONObject number) throws JSONException, IOException {
        List<T> data = new ArrayList<>();
        if (!populateContainerInfo(root, number)) {
            return null;
        }
        data.add((T) this);
        return data;
    }

    /**
     * Populates this container with data from the provided {@link JSONObject}s.
     * Can be overridden to parse the {@link JSONObject}s differently.
     * @see #parseListData
     * @see #parseDetailData
     */
    boolean populateContainerInfo(JSONObject root, JSONObject number) throws JSONException, IOException {
        if (!parseListData(root, number)) {
            return false;
        }

        String jsonResponseDetails = getJsonUrlDetails();
        if (jsonResponseDetails == null) {
            return false;
        }

        return parseDetailData(QueryUtilities.makeHttpRequest(jsonResponseDetails));
    }

    /**
     * What URL to get detailed information about this container from.
     * Can be overridden to parse the {@link JSONObject}s differently.
     * @see #parseDetailData
     */
    String getJsonUrlDetails() {
        if (indexPage == -1) {
            return null;
        }
        return "https://xenoblade.fandom.com/api/v1/Articles/Details?ids=" + indexPage;
    }

    /**
     * Gets basic information from a list-style {@link JSONObject}.
     * Can be overridden to parse the {@link JSONObject}s differently.
     * @see ContainerUtilities.Group#urlJsonList
     */
    boolean parseListData(JSONObject root, JSONObject number) throws JSONException {
        //Remove category pages
        String url = number.getString("url");
        if (url.contains("Category:")) {
            return false;
        }

        //Remove image pages
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
     * Gets detailed information from a single-style {@link JSONObject}.
     * Can be overridden to parse the {@link JSONObject}s differently.
     * @see #getJsonUrlDetails
     */
    boolean parseDetailData(String jsonResponse) throws JSONException {
        JSONObject items = new JSONObject(jsonResponse)
                .getJSONObject("items");
        JSONObject number = items.getJSONObject(items.keys().next());
        setSubText(number.getString("abstract"));
        setImage(number.getString("thumbnail"));
        return true;
    }
}
