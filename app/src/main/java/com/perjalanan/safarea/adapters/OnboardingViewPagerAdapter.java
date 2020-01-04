package com.perjalanan.safarea.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import com.perjalanan.safarea.R;
import com.perjalanan.safarea.data.OnboardingItem;

public class OnboardingViewPagerAdapter extends PagerAdapter {

    Context context ;
    List<OnboardingItem> listOnboarding ;

    public OnboardingViewPagerAdapter(Context context, List<OnboardingItem> listOnboarding) {
        this.context = context;
        this.listOnboarding = listOnboarding;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.onboarding_intro_screen, null);

        ImageView background = layoutScreen.findViewById(R.id.intro_background);
        ImageView logos = layoutScreen.findViewById(R.id.intro_logo);
        TextView title = layoutScreen.findViewById(R.id.intro_title);
        TextView description = layoutScreen.findViewById(R.id.intro_desc);

        title.setText(listOnboarding.get(position).getTitle());
        description.setText(listOnboarding.get(position).getDescription());
        background.setImageResource(listOnboarding.get(position).getScreenImg1());
        logos.setImageResource(listOnboarding.get(position).getScreenImg2());

        container.addView(layoutScreen);

        return layoutScreen;

    }

    @Override
    public int getCount() {
        return listOnboarding.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


}
