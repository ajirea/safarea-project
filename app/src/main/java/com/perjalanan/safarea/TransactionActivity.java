package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import com.perjalanan.safarea.adapters.TransactionListAdapter;
import com.perjalanan.safarea.data.TransactionItem;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.helpers.ToolbarHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import org.json.JSONArray;
import org.json.JSONObject;

public class TransactionActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private RecyclerView mRecyclerView;
    private TransactionListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue requestQueue;
    private ArrayList<TransactionItem> transactionList;
    private User user;

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //API
    public void getTransaction() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Error!");

        String orderUrl = ServerAPI.ORDER + "/" + user.getId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, orderUrl, null,
                response -> {
                    try {
                        if(response.getBoolean("status")){
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

//    private ArrayList<TransactionItem> exampleTransactionData() {
//        ArrayList<TransactionItem> exTransaction = new ArrayList<>();
//
//        TransactionItem transaction1 = new TransactionItem(
//                R.drawable.sample_product,
//                1,
//                1,
//                3,
//                "Asep 1",
//                "Pajama 1",
//                "088392931848",
//                "01/01/2020",
//                "Contoh Order",
//                80000.00,
//                240000.00
//        );
//
//        TransactionItem transaction2 = new TransactionItem(
//                R.drawable.sample_product,
//                1,
//                1,
//                3,
//                "Asep 2",
//                "Pajama 2",
//                "088392931848",
//                "01/01/2020",
//                "Contoh Order",
//                70000.00,
//                210000.00
//        );
//
//        TransactionItem transaction3 = new TransactionItem(
//                R.drawable.sample_product,
//                1,
//                1,
//                3,
//                "Asep 3",
//                "Pajama 3",
//                "088392931848",
//                "01/01/2020",
//                "Contoh Order",
//                60000.00,
//                180000.00
//        );
//
//        TransactionItem transaction4 = new TransactionItem(
//                R.drawable.sample_product,
//                1,
//                1,
//                3,
//                "Asep 4",
//                "Pajama 4",
//                "088392931848",
//                "01/01/2020",
//                "Contoh Order",
//                50000.00,
//                150000.00
//        );
//
//        TransactionItem transaction5 = new TransactionItem(
//                R.drawable.sample_product,
//                1,
//                1,
//                3,
//                "Asep 5",
//                "Pajama 5",
//                "088392931848",
//                "01/01/2020",
//                "Contoh Order",
//                100000.00,
//                300000.00
//        );
//
//        exTransaction.add(transaction1);
//        exTransaction.add(transaction2);
//        exTransaction.add(transaction3);
//        exTransaction.add(transaction4);
//        exTransaction.add(transaction5);
//
//        return exTransaction;
//    }
}
