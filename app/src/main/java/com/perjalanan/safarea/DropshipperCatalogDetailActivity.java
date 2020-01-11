package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import com.perjalanan.safarea.adapters.CatalogImageAdapter;
import com.perjalanan.safarea.data.CatalogItem;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.dialogs.AddStockDialog;
import com.perjalanan.safarea.helpers.FormatHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DropshipperCatalogDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private int itemId;
    private ArrayList<String[]> images;
    private RequestQueue requestQueue;
    private CatalogImageAdapter imageAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropshipper_catalog_detail);

        //toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // mengambil data user yang login
        UserLocalStore userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        //Init Volley
        requestQueue = Volley.newRequestQueue(this);

        //init page
        Intent intent = getIntent();

        CatalogItem catalogItem = intent.getParcelableExtra("Catalog Item");
        itemId = catalogItem.getId();


        images = catalogItem.getImages();
        TabLayout dottedIndicator = findViewById(R.id.dottedIndicator);
        ViewPager imagePager = findViewById(R.id.productDetailImage);
        imageAdapter = new CatalogImageAdapter(this, images);
        imagePager.setAdapter(imageAdapter);
        dottedIndicator.setupWithViewPager(imagePager);

        TextView titleCatalog = findViewById(R.id.titleCatalog);
        TextView textPrice = findViewById(R.id.textPrice);
        TextView textStock = findViewById(R.id.textStock);
        TextView textDesc = findViewById(R.id.textDescription);

        toolbar.setTitle(catalogItem.getTitle());
        titleCatalog.setText(catalogItem.getTitle());
        textStock.setText(getString(R.string.text_stock_available, catalogItem.getStock()));
        textPrice.setText(FormatHelper.priceFormat(catalogItem.getPrice()));
        textDesc.setText(Html.fromHtml(catalogItem.getDescription()));

        imageAdapter.onImageClickListener(l -> {
            Intent imageIntent = new Intent(this, ImageViewActivity.class);
            imageIntent.putExtra("catalogItem", catalogItem);
            startActivity(imageIntent);
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
