package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.adapters.DropshipperCatalogListAdapter;
import com.perjalanan.safarea.data.CatalogItem;
import com.perjalanan.safarea.helpers.ToolbarHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DropshipperCatalogActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private ArrayList<CatalogItem> catalogList;
    private RecyclerView mRecyclerView;
    private DropshipperCatalogListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserLocalStore userLocalStore;
    private ArrayList<String[]> images;
    private Boolean isSelectingProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropshipper_catalog);

        Intent dropshipperCatalogIntent = getIntent();
        isSelectingProduct = dropshipperCatalogIntent.getBooleanExtra("isSelectingProduct", false);

        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle("Katalog Produk");

        if (isSelectingProduct)
            toolbarHelper.setToolbarTitle(getString(R.string.text_select_product));

        //Inisiasi request volley
        requestQueue = Volley.newRequestQueue(this);

        //component
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this::getDropshipperCatalog);

        //Inisiasi Data
        userLocalStore = new UserLocalStore(getBaseContext());

        // recycler view
        catalogList = new ArrayList<>();
        getDropshipperCatalog();

        catalogList = new ArrayList<>();
        getDropshipperCatalog();

        mRecyclerView = findViewById(R.id.dropshipperCatalogRecyclerView);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mAdapter = new DropshipperCatalogListAdapter(catalogList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new GridItemDecoration(2, 30, true));

        //Event handling
        mAdapter.setOnItemClickListener(position -> {
            if (!isSelectingProduct) {
                Intent intent = new Intent(DropshipperCatalogActivity.this,
                        DropshipperCatalogDetailActivity.class);
                intent.putExtra("Catalog Item", catalogList.get(position));
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra("productItem", catalogList.get(position));
                setResult(RESULT_OK, intent);
                finish();
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

    /**catalog items

     */
    private void getDropshipperCatalog () {
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Error!");

        String dropshipperUrl = ServerAPI.DROPSHIPPER + userLocalStore.getLoggedInUser().getId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, dropshipperUrl, null,
                response -> {
                    try {
                        if (response.getBoolean("status")){
                            catalogList.clear();
                            for(int i = 0; i < response.getJSONArray("data").length(); i ++){
                                JSONObject item = response.getJSONArray("data").getJSONObject(i);
                                CatalogItem catalog = new CatalogItem
                                        (
                                                Integer.parseInt(item.getString("product_id")),
                                                ServerAPI.BASE_URL + item.getString("thumbnail"),
                                                item.getString("name"),
                                                Double.parseDouble(item.getString("price"))
                                        );
                                catalog.setStock(Integer.parseInt(item.getString("qty")));
                                catalog.setDescription(item.getString("description"));
                                images = new ArrayList<>();
                                for(int j = 0; j < item.getJSONArray("images").length(); j++) {
                                    JSONObject image = item.getJSONArray("images").getJSONObject(j);
                                    images.add(new String[]{
                                            ServerAPI.BASE_URL + image.getString("path"),
                                            image.getString("name")
                                    });
                                }
                                catalog.setImages(images);
                                catalogList.add(catalog);
                            }
                            mAdapter.notifyDataSetChanged();
                        }else {
                            alert.setMessage(response.getJSONObject("data")
                                    .getString("message")).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        alert.setMessage(e.getMessage()).show();
                    }
                },error -> alert.setMessage(error.getMessage()).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }
        };

        requestQueue.add(request);
    }
}