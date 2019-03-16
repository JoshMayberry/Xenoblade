package com.example.android.xenoblade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.xenoblade.databinding.ListItemBladeBinding;

import java.util.List;

class BladeAdapter extends ArrayAdapter<Blade> {
    public BladeAdapter(Context context, List<Blade> bladeList) {
        super(context, 0, bladeList);
    }

    //See: https://github.com/nomanr/android-databinding-example/blob/master/app/src/main/java/com/databinding/example/databindingexample/adapters/SimpleAdapter.java
    @Override
    public View getView(int position, View scrapView, ViewGroup parent) {
        ListItemBladeBinding binding;
        if (scrapView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            binding = ListItemBladeBinding.inflate(inflater, parent, false);
        } else {
            binding = ListItemBladeBinding.bind(scrapView);
        }

        Blade blade = getItem(position);
        assert blade != null;

        binding.portrait.setImageBitmap(blade.getPortrait());
        binding.element.setImageBitmap(blade.getPortrait());
        binding.rarity.setImageBitmap(blade.getPortrait());
        binding.name.setText(blade.getTitle());

        return binding.getRoot();
    }
}
