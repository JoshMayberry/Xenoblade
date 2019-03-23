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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//See: https://medium.com/@taman.neupane/basic-example-of-livedata-and-viewmodel-14d5af922d0#7e70
//Use: https://medium.com/androiddevelopers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4#e65b
class BaseViewModel<T extends BaseContainer<T>> extends AndroidViewModel {

    private String LOG_TAG = BaseViewModel.class.getSimpleName();
    private final MutableLiveData<List<T>> containerList = new MutableLiveData<>();
    private final MutableLiveData<Integer> progress = new MutableLiveData<>();

    BaseViewModel(@NonNull Application application) {
        super(application);
    }

    LiveData<List<T>> getContainerList() {
        return containerList;
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
    void loadContainerList(final int position) {
        assert position != -1;
        new AsyncTask<Void, Integer, List<T>>() {
            @Override
            protected List<T> doInBackground(Void... voids) {
                Map<String, T> dataMap = new HashMap<>();

                //Get a list of available containers
                try {
                    ContainerUtilities.Group orderGroup = ContainerUtilities.orderList.get(position);
                    String jsonResponseList = QueryUtilities.makeHttpRequest(orderGroup.urlJsonList);

                    if (jsonResponseList == null) {
                        Log.e(LOG_TAG, "Get JSON error");
                        return null;
                    }

                    JSONObject root = new JSONObject(jsonResponseList);
                    JSONArray items = root.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        T container = (T) orderGroup.newInstance();

                        List<T> containerList = container.getContainerList(root, items.getJSONObject(i));
                        if (containerList == null) {
                            continue;
                        }

                        //Do not add duplicates
                        for (T item : containerList) {
                            if (!dataMap.containsKey(item.title.trim())) {
                                dataMap.put(item.title.trim(), item);
                            }
                        }

                        publishProgress(dataMap.size());
                    }

                } catch (IOException error) {
                    Log.e(LOG_TAG, "IO Error", error);
                    return null;
                } catch (JSONException error) {
                    Log.e(LOG_TAG, "Parse JSON error", error);
                    return null;
                }

                //Sort Results
                //See: https://stackoverflow.com/questions/1026723/how-to-convert-a-map-to-list-in-java/1026736#1026736
                //Use: https://stackoverflow.com/questions/6826112/sorting-names-in-a-list-alphabetically/6826149#6826149
                List<T> data = new ArrayList<>(dataMap.values());
                Collections.sort(data, new Comparator<T>() {
                    @Override
                    public int compare(T item1, T item2) {
                        return item1.title.compareToIgnoreCase(item2.title);
                    }
                });
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
            protected void onPostExecute(List<T> data) {
                containerList.setValue(data);
            }
        }.execute();
    }
}

