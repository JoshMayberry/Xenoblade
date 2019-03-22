package com.example.android.xenoblade;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

//https://xenoblade.fandom.com/api/v1/Articles/List?category=Blades&limit=1000
//https://xenoblade.fandom.com/api/v1/Articles/List?category=XC2_Locations&limit=1000
//https://xenoblade.fandom.com/api/v1/Articles/List?category=XC2_Items&limit=1000

/**
 * Helper methods related to requesting and receiving earthquake data from the Fandom for Xenoblade Chronicles 2.
 * See: http://www.wikia.com/api/v1#!/Articles/getList_get_2
 * Use: https://gist.github.com/udacityandroid/10892631f57f9f073ab9e1d11cfaafcf
 */
class QueryUtilities {
    private static final String LOG_TAG = QueryUtilities.class.getSimpleName();

    //https://xenoblade.fandom.com/api/v1/Articles/List?category=XC2_Items&limit=1000
    //https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=72759&prop=revisions&rvlimit=1&rvprop=content
    //https://xenoblade.fandom.com/api/v1/Articles/List?category=Quests_by_Location&limit=1000

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtilities} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class title QueryUtilities (and an object instance of QueryUtilities is not needed).
     */
    private QueryUtilities() {
    }

//    static ArrayList<Blade> getBlades() {
//        ArrayList<Blade> bladeList = new ArrayList<>();
//        bladeList.add(new Blade().parse_sample());
//        bladeList.add(new Blade().parse_sample());
//        bladeList.add(new Blade().parse_sample());
//        return bladeList;
//    }
//
//    static ArrayList<Location> getLocations() {
//        ArrayList<Location> locationList = new ArrayList<>();
//        locationList.add(new Location().parse_sample());
//        return locationList;
//    }

    /**
     * Returns new URL object from the given string URL.
     */
    static URL createUrl(String stringUrl) {
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException error) {
            Log.e(LOG_TAG, "Error with creating URL ", error);
            return null;
        }
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    static String makeHttpRequest(String requestUrl) throws IOException {
        if (TextUtils.isEmpty(requestUrl)) {
            return "";
        }

        String jsonResponse = "";
        URL url = createUrl(requestUrl);
        if (url == null) {
            return "";
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    static String readFromStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        StringBuilder output = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            output.append(line);
            line = reader.readLine();
        }
        return output.toString();
    }
}