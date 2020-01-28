package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.perjalanan.safarea.adapters.TransactionListAdapter;
import com.perjalanan.safarea.data.TransactionItem;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.helpers.ToolbarHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class TransactionActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private RecyclerView mRecyclerView;
    private TransactionListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue requestQueue;
    private ArrayList<TransactionItem> transactionList;
    private User user;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //Custom Toolbar
        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle(getString(R.string.text_transaction_list));

        // mengambil data user yang login
        UserLocalStore userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        //Inisiasi request volley
        requestQueue = Volley.newRequestQueue(this);

        // components
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this::getTransaction);

        //Recycle view
        transactionList = new ArrayList<>();
        getTransaction();
        mRecyclerView = findViewById(R.id.transactionListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TransactionListAdapter(this, transactionList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TransactionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer position) {
                Intent intent = new Intent(TransactionActivity.this, TransactionDetaiActivity.class);
                intent.putExtra("Detail Transaksi", transactionList.get(position));
                startActivity(intent);
            }
        });
        // event add transaction
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(l -> {
            startActivity(new Intent(this, TransactionAddActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTransaction();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //API
    public void getTransaction() {
        swipeRefreshLayout.setRefreshing(true);
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Error!");

        String orderUrl = ServerAPI.ORDER + "/" + user.getId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, orderUrl, null,
                response -> {
                    swipeRefreshLayout.setRefreshing(false);
                    try {
                        if(response.getBoolean("status")){
                            transactionList.clear();
                            for(int i = 0; i < response.getJSONArray("data").length(); i ++) {
                                JSONObject item = response.getJSONArray("data").getJSONObject(i);
                                TransactionItem order = new TransactionItem
                                (
                                ServerAPI.BASE_URL + item.getString("thumbnail"),
                                        Integer.parseInt(item.getString("id")),
                                        Integer.parseInt(item.getString("user_id")),
                                        Integer.parseInt(item.getString("qty")),
                                        item.getString("buyer_name"),
                                        item.getString("name"),
                                        item.getString("phone"),
                                        item.getString("created_at"),
                                        item.getString("description"),
                                        Double.parseDouble(item.getString("price")),
                                        Double.parseDouble(item.getString("total"))
                                );
                                transactionList.add(order);
                            }
                            mAdapter.notifyDataSetChanged();
                        }else {
                            alert.setMessage(response.getJSONObject("data")
                                    .getString("message")).show();
                        }

                    } catch (Exception e) {
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
