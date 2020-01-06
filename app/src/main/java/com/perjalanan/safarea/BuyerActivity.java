package com.perjalanan.safarea;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private RecyclerView.Adapter mAdapter;
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
        getBuyers();

        // component
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this::getBuyers);

        // recyclerview buyer
        mRecyclerView = findViewById(R.id.buyerListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BuyerListAdapter(buyerList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // event
        FloatingActionButton btnAdd = findViewById(R.id.btnDetails);
        btnAdd.setOnClickListener(l -> {
            startActivity(new Intent(this, BuyerAddActivity.class));
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
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
                            ProgressBar progress = findViewById(R.id.progress_circular);
                            progress.setVisibility(View.INVISIBLE);
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
                }, error -> {
            alert.setMessage(error.getMessage()).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }
        };

        requestQueue.add(request);
    }
}
