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
    private OnboardingViewPagerAdapter onboardingViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);


        //Setting up array list
        List<OnboardingItem> listOnboarding = new ArrayList<>();

        //First Page
        listOnboarding.add(new OnboardingItem("Daftar Gratis", "Dropshipper mendaftar langsung ke" +
                "supplier",R.drawable.bg_auth_shape_1x, R.drawable.onboarding_step_1));

        //Second Page
        listOnboarding.add(new OnboardingItem("Login Akun", "Dropshipper mendapat akun " +
                "untuk Login",R.drawable.bg_auth_shape_1x, R.drawable.onboarding_step_2));

        //Third Page
        listOnboarding.add(new OnboardingItem("Tentukan Barang", "Dropshipper menentukan barang yang" +
                "akan di stok melalui aplikasi" , R.drawable.bg_auth_shape_1x,
                R.drawable.onboarding_step_3));

        //Fourth Page
        listOnboarding.add(new OnboardingItem("Dapatkan Barang", "Supplier mengirim barang jika jarak" +
                "dropshipper dengan supplier jauh",R.drawable.bg_auth_shape_1x,
                R.drawable.onboarding_step_4));

        //Fifth Page
        listOnboarding.add(new OnboardingItem("Tawarkan Barang", "Dropshipper menawarkan barang kepada" +
                "pelanggan melalui manual ataupun aplikasi",R.drawable.bg_auth_shape_1x,
                R.drawable.onboarding_step_5));

        //Sixth Page
        listOnboarding.add(new OnboardingItem("Catat Transaksi", "Dropshipper mencatat transaksi" +
                "di aplikasi",R.drawable.bg_auth_shape_1x, R.drawable.onboarding_step_6));

        //Seventh Page
        listOnboarding.add(new OnboardingItem("Laporan Transaksi", "Hasil transaksi dikirim ke" +
                "supplier",R.drawable.bg_auth_shape_1x, R.drawable.onboarding_step_7));

        //Setting up the Viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        onboardingViewPagerAdapter = new OnboardingViewPagerAdapter(this, listOnboarding);
        screenPager.setAdapter(onboardingViewPagerAdapter);

    }
}
