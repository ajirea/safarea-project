package com.perjalanan.safarea;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
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
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SupplierCatalogActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<CatalogItem> catalogList;
    private RecyclerView mRecyclerView;
    private SupplierCatalogListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_catalog);

        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle("Katalog Produk");

        //Inisiasi request volley
        requestQueue = Volley.newRequestQueue(this);

        //Inisiasi Data
        catalogList = new ArrayList<>();
        getSupCatalog();

        //component
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this::getSupCatalog);
        progressBar = findViewById(R.id.progress_circular);

        // recycler view
        catalogList = new ArrayList<>();
        getSupCatalog();
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

    private void getSupCatalog () {
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Error!");

        String supplierUrl = ServerAPI.SUPPLIER_CATALOG;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, supplierUrl, null,
                response -> {
                    try {
                        if (response.getBoolean("status")){
                            catalogList.clear();
                            for(int i = 0; i < response.getJSONArray("data").length(); i ++){
                                JSONObject item = response.getJSONArray("data").getJSONObject(i);
                                CatalogItem catalog = new CatalogItem
                                (

                                    Integer.parseInt(item.getString("id")),
                                    ServerAPI.BASE_URL + item.getString("thumbnail"),
                                    item.getString("name"),
                                    Double.parseDouble(item.getString("price"))
                                );
                                catalogList.add(catalog);
                            }
                            mAdapter.notifyDataSetChanged();
                        }else {
                            alert.setMessage(response.getJSONObject("data")
                                    .getString("message")).show();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        alert.setMessage(e.getMessage()).show();
                    }
                },error ->  {
            alert.setMessage(error.getMessage()).show();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }
        };

        requestQueue.add(request);
    }
}
