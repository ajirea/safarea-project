package com.perjalanan.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.adapters.SupplierCatalogListAdapter;
import com.perjalanan.safarea.data.CatalogItem;
import com.perjalanan.safarea.helpers.ProgressDialogHelper;
import com.perjalanan.safarea.helpers.ToolbarHelper;
import com.perjalanan.safarea.repositories.ServerAPI;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SupplierCatalogActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private ProgressDialogHelper progressDialog;
    private ArrayList<CatalogItem> catalogList;
    private RecyclerView mRecyclerView;
    private SupplierCatalogListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_catalog);

        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle("Katalog Produk");

        //Instansiasi progress dialog
        progressDialog = new ProgressDialogHelper(this,
                getString(R.string.alert_getting_supplier_data_title),
                getString(R.string.alert_please_wait_message));

        //Instansiasi request volley
        requestQueue = Volley.newRequestQueue(this);

        //Setting up JSON
        progressDialog.show();
        String supplierUrl = ServerAPI.SUPPLIER_CATALOGUE;


        // recycler view
        //catalogList = exampleCatalogData();
        mRecyclerView = findViewById(R.id.dropshipperCatalogRecyclerView);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mAdapter = new SupplierCatalogListAdapter(catalogList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new GridItemDecoration(2, 30, true));


        //Event handling
        mAdapter.setOnItemClickListener(new SupplierCatalogListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer position) {
                Intent intent = new Intent(SupplierCatalogActivity.this, SupplierCatalogDetailActivity.class);
                intent.putExtra("Catalog Item", catalogList.get(position));
                startActivity(intent);
            }
        });
    }

    /**
     * Handle tombol back pada toolbar ketika di klik
     * @return boolean
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

//    /**
//     * Contoh data untuk catalog items
//     * @return ArrayList<CatalogItem>
//     */
//    private ArrayList<CatalogItem> exampleCatalogData() {
//        ArrayList<CatalogItem> exCatalog = new ArrayList<>();
//
//        CatalogItem item1 = new CatalogItem(
//                1,
//                R.drawable.sample_product,
//                "Kids Pajama Short Sleeves",
//                80000D
//        );
//        item1.setStock(5);
//
//        CatalogItem item2 = new CatalogItem(
//                2,
//                R.drawable.sample_product,
//                "Kids Pajama Long Sleeves",
//                90000D
//        );
//        item2.setStock(20);
//
//        exCatalog.add(item1);
//        exCatalog.add(item2);
//
//        return exCatalog;
//    }
}
