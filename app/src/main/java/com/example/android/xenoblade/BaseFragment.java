package com.example.android.xenoblade;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.example.android.xenoblade.databinding.FragmentBaseBinding;

import java.util.ArrayList;
import java.util.List;

//See: http://www.tutorialspoint.com/java/java_documentation.htm
/**
 * Displays a list of {@link BaseContainer} objects.
 * @param <T> What child of {@link BaseContainer} to use for the fragment
 * @see Item
 * @see Blade
 * @see Location
 * @see HeartToHeart
 */
public class BaseFragment<T extends BaseContainer<T>> extends Fragment {
    public String LOG_TAG = BaseFragment.class.getSimpleName();
    FragmentBaseBinding binding;

    int position = -1;
    BaseAdapter<T> baseAdapter;

    /**
     * Must be called so the fragment knows which element to use from {@link ContainerUtilities#orderList}.
     * @param position Which index in the TabLayout {@link MainActivity.MyFragmentPagerAdapter} this fragment is
     */
    public BaseFragment setPosition(int position) {
        this.position = position;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();

        //See: https://stackoverflow.com/questions/34706399/how-to-use-data-binding-with-fragment/40527833#40527833
        binding = FragmentBaseBinding.inflate(inflater, container, false);

        //Network connection check
        //See: https://developer.android.com/training/basics/network-ops/connecting.html
        //Use: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html#DetermineConnection
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Log.e(LOG_TAG, "No Network Connection");
            binding.loadingIndicator.setVisibility(View.GONE);
            binding.emptyView.setText(R.string.internet_error);
            return binding.getRoot();
        }

        //Empty list state
        //See: https://material.io/design/communication/empty-states.html
        //See: https://developer.android.com/reference/android/widget/AdapterView.html#setEmptyView(android.view.View)
        binding.list.setEmptyView(binding.emptyView);

        //Create Adapter
        baseAdapter = new BaseAdapter(context, new ArrayList<T>());
        binding.list.setAdapter(baseAdapter);

        //Open fandom page when an item is clicked
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                T container = baseAdapter.getItem(position);
                if (container == null) {
                    return;
                }
                Uri uri = container.getPage();
                if (uri == null) {
                    return;
                }
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(websiteIntent);
            }
        });

        //Populate list
        //See: https://stackoverflow.com/questions/49405616/cannot-resolve-symbol-viewmodelproviders-on-appcompatactivity/49407157#49407157
        //Use: https://medium.com/androiddevelopers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4#e65b
        BaseViewModel model = ViewModelProviders.of(this).get(BaseViewModel.class);
        model.loadContainerList(position);
        model.getContainerList().observe(this, new Observer<List<T>>() {
            @Override
            public void onChanged(@Nullable List<T> containerList) {
                // update UI
                if (containerList == null) {
                    return;
                }

                binding.counter.setVisibility(View.GONE);
                binding.loadingIndicator.setVisibility(View.GONE);
                binding.emptyView.setText(ContainerUtilities.orderList.get(position).error);

                baseAdapter.clear();
                if (!containerList.isEmpty()) {
                    baseAdapter.addAll(containerList);
                }
            }
        });

        //Display progress
        model.getProgress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer progress) {
                // update UI
                if (progress == null) {
                    return;
                }

                binding.counter.setText(String.valueOf(progress));
            }
        });

        return binding.getRoot();
    }
}

