package com.perjalanan.safarea;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.adapters.CatalogImageAdapter;
import com.perjalanan.safarea.data.CatalogItem;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.dialogs.AddStockDialog;
import com.perjalanan.safarea.dialogs.SuccessAddStockDialog;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.helpers.FormatHelper;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupplierCatalogDetailActivity extends AppCompatActivity
        implements AddStockDialog.AddStockDialogListener,
        SuccessAddStockDialog.SuccessAddStockDialogListener {

    private Toolbar toolbar;
    private int itemId;
    private ArrayList<String[]> images;
    private RequestQueue requestQueue;
    private CatalogImageAdapter imageAdapter;
    private User user;
    private EditText fieldProfit, fieldQty;

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

        //comps
        fieldProfit = findViewById(R.id.fieldProfit);
        fieldQty = findViewById(R.id.fieldQty);

        // mengambil data user yang login
        UserLocalStore userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

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

        //Event handling
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
    public void onButtonClicked(Integer stock, Double profitPrice, String type) {
        addStock(stock, profitPrice, type);
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
    public void addStock(Integer stock, Double profitPrice, String type) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Error!");

        String addStockUrl = ServerAPI.DROPSHIPPER + user.getId() + "/" + itemId + "/stock";
        StringRequest request = new StringRequest(Request.Method.POST, addStockUrl, response -> {
            try {
                JSONObject resp = new JSONObject(response);

                if(resp.getBoolean("status")) {
                    alert.setTitle("Sukses!")
                            .setMessage(resp.getJSONObject("data").getString("message"))
                            .show();
                    clearAllFields();
                } else {
                    alert.setMessage(resp.getJSONObject("data")
                            .getString("message")).show();
                }
            } catch (JSONException e) {
                e.getStackTrace();
                alert.setMessage(e.getMessage()).show();
            }

        }, error -> {
            alert.setMessage(error.getMessage()).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type",type);
                params.put("profit_price",profitPrice.toString());
                params.put("qty", stock.toString());
                return params;
            }
        };

        requestQueue.add(request);
    }
    public void clearAllFields(){
        fieldProfit.setText("");
        fieldQty.setText("");
    }
}
