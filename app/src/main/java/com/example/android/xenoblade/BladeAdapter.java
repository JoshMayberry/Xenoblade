package com.example.android.xenoblade;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
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
    private String LOG_TAG = BladeAdapter.class.getSimpleName();
    private LayoutInflater inflater;

    //See: https://github.com/nomanr/android-databinding-example/blob/master/app/src/main/java/com/databinding/example/databindingexample/adapters/SimpleAdapter.java
    //Use: https://stackoverflow.com/questions/40557087/how-can-i-use-android-databinding-in-a-listview-and-still-use-a-viewholder-patte/40557175#40557175
    @Override
    public View getView(int position, View scrapView, ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(getContext());
//            inflater = ((Activity) parent.getContext()).getLayoutInflater();
        }

        ListItemBladeBinding binding = DataBindingUtil.getBinding(scrapView);
        if (binding == null) {
            binding = ListItemBladeBinding.inflate(inflater, parent, false);
        }


        //The databinding does not seem to work with automatically changing the items... So this will be used for now...
        Blade blade = getItem(position);
        assert blade != null;

//        binding.portrait.setImageBitmap(blade.getPortrait());
//        binding.element.setImageBitmap(blade.getPortrait());
//        binding.rarity.setImageBitmap(blade.getPortrait());
        binding.name.setText(blade.getTitle());

        return binding.getRoot();
    }
}
