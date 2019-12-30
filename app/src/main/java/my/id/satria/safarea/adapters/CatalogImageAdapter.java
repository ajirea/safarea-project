package my.id.satria.safarea.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import my.id.satria.safarea.R;

public class CatalogImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String[]> images;

    public CatalogImageAdapter(Context context, ArrayList<String[]> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_gallery_item, null);

        ImageView imageItem = view.findViewById(R.id.imageItem);
        Picasso.get().load(images.get(position)[0])
                .resize(420, 300)
                .centerCrop()
                .into(imageItem);

        container.addView(view);

        return view;
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
