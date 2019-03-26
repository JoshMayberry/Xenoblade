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
/**
 * Handles recycling views on a {@link GenericFragment}
 * @param <T> What child of {@link GenericContainer} to use for the fragment
 * @see Item
 * @see Blade
 * @see Location
 * @see HeartToHeart
 */
public class GenericAdapter<T extends GenericContainer<T>> extends ArrayAdapter<T> {
    String LOG_TAG = GenericAdapter.class.getSimpleName();

    private LayoutInflater inflater;

    public GenericAdapter(Context context, List<T> containerList) {
        super(context, 0, containerList);
    }

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
        //Ideally, the code from here to the end will be gone
        //I think it does not work because the containers are populated before the views are created. This means that the setter methods are called before they can notify the binding that a change has taken place?
        T container = getItem(position);
        if (container != null) {
            binding.title.setText(container.getTitle());
            binding.subText.setText(container.getSubText());

            if (container.resourceImage != -1 || container.resourceSubImage != -1) {
                offlineImage(binding, container);
            } else {
                onlineImage(binding, container);
            }
        }
        return binding.getRoot();
    }

    private void offlineImage(ListItemBinding binding, T container) {
        if (container.resourceImage != -1) {
            binding.image.setImageResource(container.resourceImage);
            binding.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        if (container.resourceSubImage != -1) {
            binding.subImage.setImageResource(container.resourceSubImage);
        }
    }

    //See: http://square.github.io/picasso/#features
    //See: https://stackoverflow.com/questions/47980845/does-picassos-resizedimen-method-take-dp-or-pixels/47980931#47980931
    //Use: https://futurestud.io/tutorials/picasso-image-resizing-scaling-and-fit#centerinside
    private void onlineImage(ListItemBinding binding, T container) {
        String[] urlList = {container.getImage(), container.getSubImage()};
        int[] dimenList = {R.dimen.sizeImage, R.dimen.sizeSubImage};
        ImageView[] viewList = {binding.image, binding.subImage};

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
}
