package my.id.satria.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import my.id.satria.safarea.R;
import my.id.satria.safarea.adapters.OnboardingViewPagerAdapter;
import my.id.satria.safarea.data.OnboardingItem;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private OnboardingViewPagerAdapter OnboardingViewPagerAdapter;
    OnboardingViewPagerAdapter onboardingViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_intro_screen);


        //Setting up array list
        List<OnboardingItem> listOnboarding = new ArrayList<>();

        //First Page
        listOnboarding.add(new OnboardingItem("", "Dropshipper mendaftar langsung ke" +
                "supplier",R.drawable.bg_auth_shape_1x, R.drawable.onboarding_check_in));

        //Second Page
        listOnboarding.add(new OnboardingItem("", "Dropshipper mendapat akun " +
                "untuk Login",R.drawable.bg_auth_shape_1x, R.drawable.onboarding_login));

        //Third Page
        listOnboarding.add(new OnboardingItem("", "Dropshipper menentukan barang yang" +
                "akan di stok melalui aplikasi" , R.drawable.bg_auth_shape_1x,
                R.drawable.onboarding_inventory));

        //Fourth Page
        listOnboarding.add(new OnboardingItem("", "Supplier mengirim barang jika jarak" +
                "dropshipper dengan supplier jauh",R.drawable.bg_auth_shape_1x,
                R.drawable.onboarding_box));

        //Fifth Page
        listOnboarding.add(new OnboardingItem("", "Dropshipper menawarkan barang kepada" +
                "pelanggan melalui manual ataupun aplikasi",R.drawable.bg_auth_shape_1x,
                R.drawable.onboarding_mother_2));

        //Sixth Page
        listOnboarding.add(new OnboardingItem("", "Dropshipper mencatat transaksi" +
                "di aplikasi",R.drawable.bg_auth_shape_1x, R.drawable.onboarding_clipboard));

        //Seventh Page
        listOnboarding.add(new OnboardingItem("", "Hasil transaksi dikirim ke" +
                "supplier",R.drawable.bg_auth_shape_1x, R.drawable.onboarding_sharing_content));

        //Setting up the Viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        OnboardingViewPagerAdapter = new OnboardingViewPagerAdapter(this,listOnboarding);
        screenPager.setAdapter(onboardingViewPagerAdapter);

    }
}
