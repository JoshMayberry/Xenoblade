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
import java.util.function.Supplier;

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
                List<T> data = new ArrayList<>();

                //Get a list of available containers
                try {
                    ContainerUtils.Group orderGroup = ContainerUtils.orderList.get(position);
                    String jsonResponse = QueryUtilities.makeHttpRequest(orderGroup.urlJsonList);
                    if (jsonResponse == null) {
                        Log.e(LOG_TAG, "Get JSON error");
                        return null;
                    }

                    JSONObject root = new JSONObject(jsonResponse);
                    JSONArray items = root.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        T container = (T) orderGroup.newInstance();
                        if (!container.parseListData(root, items.getJSONObject(i))) {
                            continue;
                        }
                        if (!container.parseInfoData(QueryUtilities.makeHttpRequest(container.getJsonUrlDetails()))) {
                            continue;
                        }
                        data.add(container);
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
            protected void onPostExecute(List<T> data) {
                containerList.setValue(data);
            }
        }.execute();
    }
}

