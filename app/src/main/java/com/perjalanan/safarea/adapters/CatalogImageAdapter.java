package com.perjalanan.safarea.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.perjalanan.safarea.R;

public class CatalogImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String[]> images;
    private ImageClickListener mListener;

    public CatalogImageAdapter(Context context, ArrayList<String[]> images) {
        this.context = context;
        this.images = images;
    }

    public void onImageClickListener(CatalogImageAdapter.ImageClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_gallery_item, null);

        ImageView imageItem = view.findViewById(R.id.imageItem);
        Glide.with(imageItem.getContext())
                .load(images.get(position)[0])
                .into(imageItem);

        imageItem.setContentDescription(images.get(position)[1]);

        imageItem.setOnClickListener(v -> mListener.onClickListener(v));

        container.addView(view);

        return view;
    }

    public interface ImageClickListener {
        void onClickListener(View view);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
