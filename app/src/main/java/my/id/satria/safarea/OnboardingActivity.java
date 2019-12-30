package my.id.satria.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import my.id.satria.safarea.R;
import my.id.satria.safarea.adapters.OnboardingViewPagerAdapter;
import my.id.satria.safarea.data.OnboardingItem;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private OnboardingViewPagerAdapter onboardingViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    Button btnGetStarted;
    Animation btnAnim;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking if onboarding activity's been opened before
        if(restorePrefData()){

            Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(mainActivity);
            finish();

        }

        setContentView(R.layout.activity_onboarding);

        //Initialize Indicator View, Buttons, and Animation
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_getStarted);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        //Setting up array list
        List<OnboardingItem> listOnboarding = new ArrayList<>();

        //First Page
        listOnboarding.add(new OnboardingItem("Daftar Gratis", "Dropshipper mendaftar langsung ke" +
                " supplier",R.drawable.bg_auth_shape_1x, R.drawable.onboarding_step_1));

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

        //Setting up the Tab Layout with Viewpager
        tabIndicator.setupWithViewPager(screenPager);

        //Setting up the Button Click
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();

                if (position < listOnboarding.size()){

                    position ++;
                    screenPager.setCurrentItem(position);
                }

                //When the last page reached
                if (position == listOnboarding.size()){

                    //Showing the get started button , hiding the indicator and next button
                    loadLastScreen();

                }
            }
        });

        //Method to show 'get started' button when indicator reached the last screen
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == listOnboarding.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Get Started button click
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open Main Activity
                Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainActivity);

                //Saving boolean value so the onboarding screen only shows once
                savePrefsData();
                finish();

            }
        });
    }

    private boolean restorePrefData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isOnboardingOpenedBefore = pref.getBoolean("isOnboardingOpened",false);
        return  isOnboardingOpenedBefore;

    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isOnboardingOpened",true);
        editor.commit();

    }

    //Method to show the get started button
    private void loadLastScreen() {
        btnGetStarted.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        //Setting up the animation
        btnGetStarted.setAnimation(btnAnim);
    }
}
