package com.example.android.xenoblade;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.android.xenoblade.databinding.FragmentBladeBinding;

import java.util.ArrayList;
import java.util.List;

public class BladeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Blade>> {

    BladeAdapter bladeAdapter;
    FragmentBladeBinding binding;
    private static final int BLADE_LOADER_ID = 1;
    public static final String LOG_TAG = MyFragmentPagerAdapter.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //See: https://stackoverflow.com/questions/34706399/how-to-use-data-binding-with-fragment/40527833#40527833
        binding = FragmentBladeBinding.inflate(inflater, container, false);

        Context context = getContext();

        //See: https://developer.android.com/training/basics/network-ops/connecting.html
        //Use: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html#DetermineConnection
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
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
                Uri earthquakeUri = Uri.parse(blade.getUrlPage());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });

        getLoaderManager().initLoader(BLADE_LOADER_ID, null, this);
        return binding.getRoot();
    }

    @Override
    public Loader<List<Blade>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Blade>> loader, List<Blade> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Blade>> loader) {

    }
}
