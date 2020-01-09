package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import com.perjalanan.safarea.adapters.CatalogImageAdapter;
import com.perjalanan.safarea.data.CatalogItem;
import com.perjalanan.safarea.dialogs.AddStockDialog;
import com.perjalanan.safarea.dialogs.SuccessAddStockDialog;

public class DropshipperCatalogDetailActivity extends AppCompatActivity
        {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropshipper_catalog_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        CatalogItem catalogItem = intent.getParcelableExtra("Catalog Item");
        catalogItem.setDescription("Donec at eros sagittis, porta erat scelerisque, tincidunt est. Vivamus quis imperdiet ante, eu bibendum nibh. Suspendisse potenti. Nulla non mollis libero. In euismod eros eget lacus commodo congue. Praesent a finibus enim. Nunc sit amet neque sit amet dolor blandit consectetur mattis in purus. Donec ut tellus enim. Duis at iaculis tellus. Nulla condimentum facilisis mauris vel ullamcorper. Suspendisse sed dignissim turpis.");
        catalogItem.getImages().add(new String[]{
                "https://i.picsum.photos/id/357/360/300.jpg",
                "Satu"
        });
        catalogItem.getImages().add(new String[]{
                "https://i.picsum.photos/id/1041/360/300.jpg",
                "Dua"
        });
        catalogItem.getImages().add(new String[]{
                "https://i.picsum.photos/id/882/360/300.jpg",
                "Tiga"
        });

        TabLayout dottedIndicator = findViewById(R.id.dottedIndicator);
        ViewPager imagePager = findViewById(R.id.productDetailImage);
        CatalogImageAdapter imageAdapter = new CatalogImageAdapter(this, catalogItem.getImages());
        imagePager.setAdapter(imageAdapter);
        dottedIndicator.setupWithViewPager(imagePager);

        TextView titleCatalog = findViewById(R.id.titleCatalog);
        TextView textPrice = findViewById(R.id.textPrice);
        TextView textStock = findViewById(R.id.textStock);
        TextView textDesc = findViewById(R.id.textDescription);

        toolbar.setTitle(catalogItem.getTitle());
        titleCatalog.setText(catalogItem.getTitle());
        textStock.setText("2 Stock tersedia");
        textDesc.setText(catalogItem.getDescription());
        textPrice.setText(catalogItem.getPrice().toString());

        Button btnAddStock = findViewById(R.id.btnAddStock);
        btnAddStock.setOnClickListener(l -> {
            AddStockDialog addStockDialog = new AddStockDialog();
            addStockDialog.show(getSupportFragmentManager(), "addStockDialog");
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
