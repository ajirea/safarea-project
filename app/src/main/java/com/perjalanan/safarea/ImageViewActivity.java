package com.perjalanan.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.perjalanan.safarea.adapters.CatalogImageAdapter;
import com.perjalanan.safarea.adapters.ImageViewAdapter;
import com.perjalanan.safarea.data.CatalogItem;

import java.util.Objects;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Intent intent = getIntent();
        CatalogItem catalogItem = intent.getParcelableExtra("catalogItem");

        ImageButton btnClose = findViewById(R.id.btnClose);
        ViewPager photoView = findViewById(R.id.photoView);
        ImageViewAdapter mAdapter = new ImageViewAdapter(this,
                Objects.requireNonNull(catalogItem).getImages());
        photoView.setAdapter(mAdapter);

        btnClose.setOnClickListener(l -> onBackPressed());
    }
}
