package com.perjalanan.safarea;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.perjalanan.safarea.helpers.ToolbarHelper;

public class AboutActivity extends AppCompatActivity {

    private ImageView image;
    private TextView appVer, about;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // atur custom toolbar
        ToolbarHelper toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar();
        toolbarHelper.setToolbarTitle(getString(R.string.text_about_app_header));

        //Comps
        image = findViewById(R.id.aboutLogo);
        appVer = findViewById(R.id.txtAppVer);
        about = findViewById(R.id.txtAbout);

        image.setImageResource(R.drawable.logo);
        appVer.setText(R.string.app_ver);
        about.setText(R.string.about_app_desc);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
