package com.example.android.xenoblade;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BladeLoader extends AsyncTaskLoader {
    private String url;
    public static final String LOG_TAG = BladeLoader.class.getName();

    public BladeLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Blade> loadInBackground() {

        //FOR DEBUGGING: Show loading wheel
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        if (url.isEmpty()) {
            return null;
        }

        // Perform the HTTP request for earthquake data and process the response.
        String jsonResponse = null;
        try {
            jsonResponse = QueryUtilities.makeHttpRequest(url);
        } catch (IOException error) {
            Log.e(LOG_TAG, "Error closing input stream", error);
        }
        if (jsonResponse == null) {
            return null;
        }
        return parseJSON(jsonResponse);
    }

    static List<Blade> parseJSON(String jsonText) {
        if (TextUtils.isEmpty(jsonText)) {
            return null;
        }

        List<Blade> earthquakeList = new ArrayList<>();
        Log.e(LOG_TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        try {
//            JSONArray features = new JSONObject(jsonText)
//                    .getJSONArray("features");
//
//            for (int i = 0; i < features.length(); i++) {
//                JSONObject properties = features.getJSONObject(i)
//                        .getJSONObject("properties");
//
////                earthquakeList.add(new Blade(
////                        properties.getDouble("mag"),
////                        properties.getString("place"),
////                        properties.getLong("time"),
////                        properties.getString("url")));
//            }
//        } catch (JSONException error) {
//            error.printStackTrace();
//            return null;
//        }
        return earthquakeList;
    }
}
