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

public class BaseFragment<T extends BaseContainer<T>> extends Fragment {
    BaseAdapter<T> baseAdapter;
    FragmentBaseBinding binding;
    public String LOG_TAG = BaseFragment.class.getSimpleName();

    int position = -1;

    BaseFragment setPosition(int position) {
        this.position = position;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (position == -1) throw new AssertionError();
        Context context = getContext();

        //See: https://stackoverflow.com/questions/34706399/how-to-use-data-binding-with-fragment/40527833#40527833
        binding = FragmentBaseBinding.inflate(inflater, container, false);

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

        //See: https://material.io/design/communication/empty-states.html
        //See: https://developer.android.com/reference/android/widget/AdapterView.html#setEmptyView(android.view.View)
        binding.list.setEmptyView(binding.emptyView);

        baseAdapter = new BaseAdapter(context, new ArrayList<T>());
        binding.list.setAdapter(baseAdapter);
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
                binding.emptyView.setText(R.string.blade_error);

                baseAdapter.clear();
                if (!containerList.isEmpty()) {
                    baseAdapter.addAll(containerList);
                }
            }
        });

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

