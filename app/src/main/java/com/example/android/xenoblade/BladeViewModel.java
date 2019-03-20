package com.example.android.xenoblade;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//See: https://medium.com/@taman.neupane/basic-example-of-livedata-and-viewmodel-14d5af922d0#7e70
//Use: https://medium.com/androiddevelopers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4#e65b
class BladeViewModel extends AndroidViewModel {
    private String LOG_TAG = BladeViewModel.class.getSimpleName();
    private final MutableLiveData<List<Blade>> bladeList = new MutableLiveData<>();
    private final MutableLiveData<Integer> progress = new MutableLiveData<>();

    BladeViewModel(@NonNull Application application) {
        super(application);
        loadBladeList();
    }

    LiveData<List<Blade>> getBladeList() {
        return bladeList;
    }

    //See: https://stackoverflow.com/questions/48239657/how-to-handle-android-livedataviewmodel-with-progressbar-on-screen-rotate/53238717#53238717
    LiveData<Integer> getProgress() {
        return progress;
    }

    /**
     * Loads a list of blades.
     * Clicking on a blade will open the fandom article for it.
     */
    @SuppressLint("StaticFieldLeak")
    //See: https://medium.com/androiddevelopers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4#fb19
    private void loadBladeList() {
        new AsyncTask<Void, Integer, List<Blade>>() {
            @Override
            protected List<Blade> doInBackground(Void... voids) {
                List<Blade> data = new ArrayList<>();

//                //FOR DEBUGGING: Show loading wheel
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                //Get a list of available blades
                try {
                    String jsonResponse = QueryUtilities.makeHttpRequest(Blade.getJsonUrlList());
                    if (jsonResponse == null) {
                        Log.e(LOG_TAG, "Get JSON error");
                        return null;
                    }

                    JSONObject root = new JSONObject(jsonResponse);
                    JSONArray items = root.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        Blade blade = new Blade();
                        if (!blade.parseListData(root, items.getJSONObject(i))) {
                            continue;
                        }
                        if (!blade.parseInfoData(QueryUtilities.makeHttpRequest(blade.getJsonUrlDetails()))) {
                            continue;
                        }
                        data.add(blade);
                        publishProgress(data.size());
                    }

                } catch (IOException error) {
                    Log.e(LOG_TAG, "IO Error", error);
                    return null;
                } catch (JSONException error) {
                    Log.e(LOG_TAG, "Parse JSON error", error);
                    return null;
                }
                return data;
            }

            @Override
            protected void onProgressUpdate(Integer... valueList) {
                if (valueList.length < 1) {
                    return;
                }
                progress.setValue(valueList[0]);
            }

            @Override
            protected void onPostExecute(List<Blade> data) {
                bladeList.setValue(data);
            }
        }.execute();
    }
}

