package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.adapters.BuyerListAdapter;
import com.perjalanan.safarea.data.BuyerItem;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.helpers.ToolbarHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BuyerActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    private ArrayList<BuyerItem> buyerList;
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    private BuyerListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Boolean isSelectingBuyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        Intent intent = getIntent();
        isSelectingBuyer = intent.getBooleanExtra("isSelectingBuyer", false);

        // atur custom toolbar
        ToolbarHelper toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle(getString(R.string.text_buyer_lists));

        if (isSelectingBuyer) toolbarHelper.setToolbarTitle(getString(R.string.text_select_buyer));

        // mengambil data user yang login
        UserLocalStore userLocalStore = new UserLocalStore(this);
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

    /**
     * Method untuk memulai recycler view
     * di panggil ketika proses request data dari api selesai
     */
    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.buyerListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BuyerListAdapter(buyerList);
        mAdapter.setHideBtnEdit(isSelectingBuyer);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // cek apakah activity dalam status sedang memilih pembeli
        if (isSelectingBuyer) {

            // jika ya, maka kembali ke activity sebelumnya sambil mengirim buyer item
            mAdapter.setOnClickListener(this::returnBuyerItem);
        }
    }

    private void returnBuyerItem(Integer position) {
        BuyerItem buyer = buyerList.get(position);
        Intent intent = new Intent();
        intent.putExtra("buyerItem", buyer);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ambil data buyer ketika resume activity
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

        if (!isSelectingBuyer)
            getMenuInflater().inflate(R.menu.menu_add, menu);

        // ambil komponen tombol search
        MenuItem searchItem = menu.findItem(R.id.search_item);

        // tampilkan search view
        SearchView searchView = (SearchView) searchItem.getActionView();

        // handle event ketika user melakukan pencarian
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // event untuk jenis pencariannya akan dilakukan ketika user menekan tombol cari
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // event untuk jenis pencariannya akan dilakukan ketika user sedang mengetik text
            // di form pencarian
            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
                            // clear buyer data
                            buyerList.clear();

                            // menambahkan data ke buyer list
                            for(int i = 0; i < response.getJSONArray("data").length(); i++) {

                                // ambil item data berdasarkan indeks ke-i
                                JSONObject item = response.getJSONArray("data").getJSONObject(i);

                                // buat data object / orm
                                BuyerItem buyer = new BuyerItem(
                                        Integer.parseInt(item.getString("id")),
                                        Integer.parseInt(item.getString("user_id")),
                                        item.getString("name"),
                                        item.getString("phone")
                                );
                                buyer.setCreatedAt(item.getString("created_at"));
                                buyer.setDeletedAt(item.getString("deleted_at"));

                                // masukkan ke buyerList
                                buyerList.add(buyer);
                            }

                            // jalankan recyclerview
                            initRecyclerView();

                            // beri notifikasi ke adapter bahwa ada data baru yang di tambahkan
                            mAdapter.notifyDataSetChanged();

                        } else {
                            alert.setMessage(response.getJSONObject("data")
                                    .getString("message")).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        alert.setMessage(e.getMessage()).show();
                    }

                    // hilangkan ikon refresh ketika proses request ke API sudah selesai dijalankan
                    swipeRefreshLayout.setRefreshing(false);

                }, error -> {

            // tampilkan pesan error
            alert.setMessage(error.getMessage()).show();

            // hilangkan ikon refresh ketika proses request ke API sudah selesai dijalankan
            swipeRefreshLayout.setRefreshing(false);

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }
        };

        // tambahkan request ke daftar tunggu
        requestQueue.add(request);
    }
}
