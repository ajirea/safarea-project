package com.perjalanan.safarea;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.adapters.CatalogImageAdapter;
import com.perjalanan.safarea.data.CatalogItem;
import com.perjalanan.safarea.dialogs.AddStockDialog;
import com.perjalanan.safarea.dialogs.SuccessAddStockDialog;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.perjalanan.safarea.helpers.FormatHelper;
import com.perjalanan.safarea.repositories.ServerAPI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SupplierCatalogDetailActivity extends AppCompatActivity
        implements AddStockDialog.AddStockDialogListener,
        SuccessAddStockDialog.SuccessAddStockDialogListener {

    private Toolbar toolbar;
    private int itemId;
    private ArrayList<String[]> images;
    private RequestQueue requestQueue;
    private CatalogImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_catalog_detail);


        //toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Init Volley
        requestQueue = Volley.newRequestQueue(this);

        //Init Page
        Intent intent = getIntent();
        CatalogItem catalogItem = intent.getParcelableExtra("Catalog Item");
        System.out.println("Images " + catalogItem.getImages().size());
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

    // pada fragment dialog_add_stock.xml
    @Override
    public void onButtonClicked(Integer stock, Integer profit, String type) {
        String alertMessage = getString(R.string.text_stock_message_take);

        if (type == "send")
            alertMessage = getString(R.string.text_stock_message_send);

        SuccessAddStockDialog sasd = new SuccessAddStockDialog(alertMessage);
        sasd.show(getSupportFragmentManager(), "successAddStockDialog");
    }

    // pada fragment success_dialog_add_stock.xml
    // untuk handle ketika tombol di pesan sukses di klik
    @Override
    public void onButtonClicked() {
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);
        finish();
    }

    //API
    public void getDetails() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Error!");

        String supplyDetailsUrl = ServerAPI.SUPPLIER_CATALOG + "/" + itemId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, supplyDetailsUrl, null,
                response -> {
                    try {
                        if(!response.getBoolean("status")){
                            alert.setMessage(response.getJSONObject("data").getString("message"))
                                    .show();
                        }else{
                            JSONObject details = response.getJSONObject("data");
                            TextView itemDesc = findViewById(R.id.textDescription);
                            itemDesc.setText(
                                    Html.fromHtml(details.getString("description"))
                            );
                            Integer.parseInt(details.getString("stock"));

                            for(int i = 0; i < details.getJSONArray("images").length(); i++) {
                                JSONObject image = details.getJSONArray("images").getJSONObject(i);
                                images.add(new String[]{
                                        ServerAPI.BASE_URL + image.getString("path"),
                                        image.getString("name")
                                });
                            }
                            imageAdapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> alert.setMessage(error.getMessage()).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        requestQueue.add(request);
    }
}
