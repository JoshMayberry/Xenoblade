package com.example.android.xenoblade;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.xenoblade.databinding.ListItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

//See: https://www.javacodegeeks.com/2013/07/java-generics-tutorial-example-class-interface-methods-wildcards-and-much-more.html#generics-naming-convention
class BaseAdapter<T extends BaseContainer<T>> extends ArrayAdapter<T> {
    String LOG_TAG = BaseAdapter.class.getSimpleName();

    BaseAdapter(Context context, List<T> containerList) {
        super(context, 0, containerList);
    }

    private LayoutInflater inflater;

    //See: https://github.com/nomanr/android-databinding-example/blob/master/app/src/main/java/com/databinding/example/databindingexample/adapters/SimpleAdapter.java
    //Use: https://stackoverflow.com/questions/40557087/how-can-i-use-android-databinding-in-a-listview-and-still-use-a-viewholder-patte/40557175#40557175
    @Override
    public View getView(int position, View scrapView, ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(getContext());
        }

        ListItemBinding binding = DataBindingUtil.getBinding(scrapView);
        if (binding == null) {
            binding = ListItemBinding.inflate(inflater, parent, false);
        }

        //The data binding does not seem to work with automatically changing the items... So this will be used for now...
        T container = getItem(position);
        if (container != null) {
            binding.title.setText(container.getTitle());
            binding.subText.setText(container.getSubText());

            String[] urlList = {container.getImage(), container.getSubImage()};
            int[] dimenList = {R.dimen.sizeImage, R.dimen.sizeSubImage};
            ImageView[] viewList = {binding.image, binding.subImage};

            //See: http://square.github.io/picasso/#features
            //See: https://stackoverflow.com/questions/47980845/does-picassos-resizedimen-method-take-dp-or-pixels/47980931#47980931
            //Use: https://futurestud.io/tutorials/picasso-image-resizing-scaling-and-fit#centerinside
            for (int i = 0; i < urlList.length; i++) {
                if (urlList[i].isEmpty()) {
                    viewList[i].setVisibility(View.GONE);
                    continue;
                }

                viewList[i].setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(urlList[i])
                        .resizeDimen(dimenList[i], dimenList[i])
                        .centerInside()
                        .into(viewList[i]);
            }
        }
        return binding.getRoot();
    }
}
