package com.perjalanan.safarea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.adapters.BuyerListAdapter;
import com.perjalanan.safarea.data.BuyerItem;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.helpers.ToolbarHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class BuyerActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    private ArrayList<BuyerItem> buyerList;
    private RequestQueue requestQueue;
    private UserLocalStore userLocalStore;
    private RecyclerView mRecyclerView;
    private BuyerListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        // atur custom toolbar
        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle(getString(R.string.text_buyer_lists));

        // mengambil data user yang login
        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        // inisiasi request queue
        requestQueue = Volley.newRequestQueue(this);

        // inisiasi data
        buyerList = new ArrayList<>();

        // component
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this::getBuyers);
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.buyerListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BuyerListAdapter(buyerList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBuyers();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_item)
            startActivity(new Intent(this, BuyerAddActivity.class));

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        getMenuInflater().inflate(R.menu.menu_add, menu);

        MenuItem searchItem = menu.findItem(R.id.search_item);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Contoh data untuk pembeli
     * @return ArrayList<BuyerItem>
     */
    private ArrayList<BuyerItem> exampleBuyerData() {
        ArrayList<BuyerItem> exBuyers = new ArrayList<>();

        BuyerItem buyer1 = new BuyerItem(1, 1, "John Terkapar Sukses", "+62-858-6733-1231");
        BuyerItem buyer2 = new BuyerItem(1, 1, "Barian Ogo", "+62-858-6733-2123");

        exBuyers.add(buyer1);
        exBuyers.add(buyer2);

        return exBuyers;
    }

    /**
     * Method untuk mengambil data pembeli dari API
     */
    private void getBuyers() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("Error!");

        String url = ServerAPI.BUYER_ALL_DATA + "/" + user.getId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("status")) {
                            buyerList.clear();
                            for(int i = 0; i < response.getJSONArray("data").length(); i++) {
                                JSONObject item = response.getJSONArray("data").getJSONObject(i);
                                BuyerItem buyer = new BuyerItem(
                                        Integer.parseInt(item.getString("id")),
                                        Integer.parseInt(item.getString("user_id")),
                                        item.getString("name"),
                                        item.getString("phone")
                                );
                                buyer.setCreatedAt(item.getString("created_at"));
                                buyer.setDeletedAt(item.getString("deleted_at"));
                                buyerList.add(buyer);
                            }
                            initRecyclerView();
                            mAdapter.notifyDataSetChanged();

                        } else {
                            alert.setMessage(response.getJSONObject("data")
                                    .getString("message")).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        alert.setMessage(e.getMessage()).show();
                    }
                }, error -> alert.setMessage(error.getMessage()).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }
        };

        requestQueue.add(request);
    }
}
