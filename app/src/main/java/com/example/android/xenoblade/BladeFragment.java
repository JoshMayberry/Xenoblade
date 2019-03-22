package com.example.android.xenoblade;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.FileObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.android.xenoblade.databinding.FragmentBladeBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BladeFragment extends Fragment {

    BladeAdapter bladeAdapter;
    FragmentBladeBinding binding;
    private static final int BLADE_LOADER_ID = 1;
    public static final String LOG_TAG = MyFragmentPagerAdapter.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();

        //See: https://stackoverflow.com/questions/34706399/how-to-use-data-binding-with-fragment/40527833#40527833
        binding = FragmentBladeBinding.inflate(inflater, container, false);

        //See: https://developer.android.com/training/basics/network-ops/connecting.html
        //Use: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html#DetermineConnection
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Log.e(LOG_TAG, "No Network Connection");
            binding.loadingIndicator.setVisibility(View.GONE);
            binding.emptyView.setText(R.string.error_no_internet);
            return binding.getRoot();
        }

        //See: https://material.io/design/communication/empty-states.html
        //See: https://developer.android.com/reference/android/widget/AdapterView.html#setEmptyView(android.view.View)
        binding.list.setEmptyView(binding.emptyView);

        bladeAdapter = new BladeAdapter(context, new ArrayList<Blade>());
        binding.list.setAdapter(bladeAdapter);
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Blade blade = bladeAdapter.getItem(position);
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, blade.getPage());
                startActivity(websiteIntent);
            }
        });

        //See: https://stackoverflow.com/questions/49405616/cannot-resolve-symbol-viewmodelproviders-on-appcompatactivity/49407157#49407157
        //Use: https://medium.com/androiddevelopers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4#e65b
        BladeViewModel model = ViewModelProviders.of(this).get(BladeViewModel.class);
        model.getBladeList().observe(this, new Observer<List<Blade>>() {
            @Override
            public void onChanged(@Nullable List<Blade> bladeList) {
                // update UI
                if (bladeList == null) {
                    return;
                }

                binding.counter.setVisibility(View.GONE);
                binding.loadingIndicator.setVisibility(View.GONE);
                binding.emptyView.setText(R.string.error_no_blades);

                bladeAdapter.clear();
                if (!bladeList.isEmpty()) {
                    bladeAdapter.addAll(bladeList);
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

