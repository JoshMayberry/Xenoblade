package com.example.android.xenoblade;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.xenoblade.databinding.ListItemBladeBinding;
import com.squareup.picasso.Picasso;

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
        }

        ListItemBladeBinding binding = DataBindingUtil.getBinding(scrapView);
        if (binding == null) {
            binding = ListItemBladeBinding.inflate(inflater, parent, false);
        }

        Blade blade = getItem(position);
        assert blade != null;

        //The data binding does not seem to work with automatically changing the items... So this will be used for now...
        binding.name.setText(blade.getTitle());

        //See: http://square.github.io/picasso/#features
        //See: https://stackoverflow.com/questions/47980845/does-picassos-resizedimen-method-take-dp-or-pixels/47980931#47980931
        //Use: https://futurestud.io/tutorials/picasso-image-resizing-scaling-and-fit#centerinside
        Picasso.get()
                .load(blade.getElement())
                .resizeDimen(R.dimen.elementSize, R.dimen.elementSize)
                .centerInside()
                .into(binding.element);
        Picasso.get()
                .load(blade.getPortrait())
                .resizeDimen(R.dimen.portraitSize, R.dimen.portraitSize)
                .centerInside()
                .into(binding.portrait);

        return binding.getRoot();
    }
}
