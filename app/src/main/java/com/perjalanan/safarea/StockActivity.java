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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.adapters.StockListAdapter;
import com.perjalanan.safarea.data.StockItem;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.helpers.ProgressDialogHelper;
import com.perjalanan.safarea.helpers.ToolbarHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class StockActivity extends AppCompatActivity {

    private ArrayList<StockItem> stockList;
    private ToolbarHelper toolbarHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private User user;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue requestQueue;
    private AlertDialog.Builder alert;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        // atur custom toolbar
        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle(getString(R.string.text_stock_product));

        // components
        progressDialogHelper = new ProgressDialogHelper(this);
        progressDialogHelper.setMessage(getString(R.string.alert_please_wait_message));
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        alert = new AlertDialog.Builder(this).setTitle("Error!")
            .setPositiveButton(R.string.alert_yes_btn, null);

        // user
        UserLocalStore userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        // request queue
        requestQueue = Volley.newRequestQueue(this);

        // stock list recycler view
        stockList = new ArrayList<>();
        getStock();

        swipeRefreshLayout.setOnRefreshListener(this::getStock);
    }

    private void initRecycler() {
        mRecyclerView = findViewById(R.id.stockListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StockListAdapter(stockList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void removeItem(Integer position) {
        deleteStock(stockList.get(position).getId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * method untuk mengambil data stock melalui API
     */
    private void getStock() {
        swipeRefreshLayout.setRefreshing(true);
        String url = ServerAPI.DROPSHIPPER + user.getId() + "/stock";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                if(response.getBoolean("status")) {
                    stockList.clear();
                    for(int i = 0; i < response.getJSONArray("data").length(); i++) {
                        JSONObject item = response.getJSONArray("data").getJSONObject(i);
                        StockItem stock = new StockItem(
                                Integer.parseInt(item.getString("id")),
                                Integer.parseInt(item.getString("product_id")),
                                Integer.parseInt(item.getString("user_id")),
                                Integer.parseInt(item.getString("qty")),
                                ServerAPI.BASE_URL + item.getString("thumbnail"),
                                Double.parseDouble(item.getString("profit_price")),
                                item.getString("name"),
                                item.getString("status"),
                                item.getString("status_description")
                        );

                        stockList.add(stock);
                    }

                    initRecycler();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                alert.setMessage(e.getMessage()).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        }, error -> {
            error.printStackTrace();
            alert.setMessage(error.getMessage()).show();
            swipeRefreshLayout.setRefreshing(false);
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return RequestGlobalHeaders.get(getBaseContext());
            }
        };

        requestQueue.add(request);
    }

    /**
     * Method untuk menghapus stock dari database
     * @param stockId Integer
     */
    private void deleteStock(Integer stockId) {
        String url = ServerAPI.DROPSHIPPER + user.getId() + "/" + stockId + "/cancel";
        confirmDeleteRequest(url);
    }

    /**
     * Method untuk konfirmasi penerimaan barang/stock
     * @param stockId Integer
     */
    public void confirmStock(Integer stockId) {
        String url = ServerAPI.DROPSHIPPER + user.getId() + "/" + stockId + "/confirm";
        confirmDeleteRequest(url);
    }

    /**
     * Method request untuk confirm dan cancel stock barang
     * @param url String
     */
    private void confirmDeleteRequest(String url) {
        progressDialogHelper.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject resp = new JSONObject(response);
                progressDialogHelper.dismiss();

                if(resp.getBoolean("status")) {
                    alert.setTitle("Sukses!");
                    getStock();
                }

                alert.setMessage(resp.getJSONObject("data").getString("message")).show();

                // reset alert title
                alert.setOnDismissListener(l -> alert.setTitle("Error!"));

            } catch (JSONException e) {
                alert.setMessage(e.getMessage()).show();
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
            progressDialogHelper.dismiss();
            alert.setMessage(error.getMessage()).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getBaseContext());
            }
        };

        requestQueue.add(request);
    }
}
