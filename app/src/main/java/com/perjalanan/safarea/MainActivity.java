package com.perjalanan.safarea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.adapters.TransactionListAdapter;
import com.perjalanan.safarea.data.TransactionItem;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private UserLocalStore userLocalStore;
    private User user;
    private RecyclerView mRecyclerView;
    private TransactionListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue requestQueue;
    private ArrayList<TransactionItem> transactionList;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public boolean onSupportNavigateUp() {
        drawerLayout.openDrawer(navigationView, true);
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        eventListener(item, null);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mengambil Data User Login
        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        //Init Volley
        requestQueue = Volley.newRequestQueue(this);

        // swipeRefresh
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this::getRecent);

        //Recycler View
        transactionList = new ArrayList<>();
        getRecent();
        mRecyclerView = findViewById(R.id.transactionRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TransactionListAdapter(this, transactionList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TransactionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer position) {
                Intent intent = new Intent(MainActivity.this, TransactionDetaiActivity.class);
                intent.putExtra("Detail Transaksi", transactionList.get(position));
                startActivity(intent);
            }
        });

        initNavigationAndDrawer();
        initMainBtnEvent();
    }

    private void eventListener(@Nullable MenuItem menuItem, @Nullable View v) {
        Integer id = null;

        if(menuItem != null) {
            id = menuItem.getItemId();
        } else if(v != null) {
            id = v.getId();
        }

        switch(id) {
            case R.id.menuHome: {
                runActivity(MainActivity.class);
                break;
            }
            case R.id.menuSupplier: {
                runActivity(SupplierCatalogActivity.class);
                break;
            }
            case R.id.menuDropshipper: {
                runActivity(DropshipperCatalogActivity.class);
                break;
            }
            case R.id.menuStock: {
                runActivity(StockActivity.class);
                break;
            }
            case R.id.menuBuyer: {
                runActivity(BuyerActivity.class);
                break;
            }
            case R.id.menuTransaction: {
                runActivity(TransactionActivity.class);
                break;
            }
            case R.id.menuSetting: {
                runActivity(SettingActivity.class);
                break;
            }
            case R.id.menuLogout: {
                logoutDialog();
                break;
            }
        }
    }

    private void runActivity(Class cls) {
        startActivity(new Intent(this, cls));
        drawerLayout.closeDrawer(navigationView, true);
    }

    private void initNavigationAndDrawer() {
        // activate custom toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawerLayout);
        View header = navigationView.getHeaderView(0);

        header.setOnClickListener(l -> {
            runActivity(SettingActivity.class);
        });

        ImageView drawerAvatar = header.findViewById(R.id.imageAvatar);
        TextView drawerUsername = header.findViewById(R.id.textUsername);
        TextView drawerEmail = header.findViewById(R.id.textEmail);

        try {
            drawerUsername.setText(user.getUsername());
            drawerEmail.setText(user.getEmail());
            Glide.with(drawerAvatar.getContext())
                    .load(ServerAPI.BASE_URL.concat(user.getAvatar()))
                    .into(drawerAvatar);
        } catch (NullPointerException e) {
            e.getStackTrace();
        }

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ImageView imageAvatar = myToolbar.findViewById(R.id.imageAvatar);
            TextView textUsername = myToolbar.findViewById(R.id.textUsername);
            textUsername.setText(user.getUsername());
            Glide.with(imageAvatar.getContext())
                    .load(ServerAPI.BASE_URL.concat(user.getAvatar()))
                    .into(imageAvatar);

            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                    R.string.menu_open,
                    R.string.menu_close);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();
            navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
            navigationView.bringToFront();

            // set event click listener
            imageAvatar.setOnClickListener(l -> {
                runActivity(SettingActivity.class);
            });
            textUsername.setOnClickListener(l -> {
                runActivity(SettingActivity.class);
            });
        }
    }

    private void initMainBtnEvent() {
        findViewById(R.id.menuSupplier).setOnClickListener(this);
        findViewById(R.id.menuDropshipper).setOnClickListener(this);
        findViewById(R.id.menuStock).setOnClickListener(this);
        findViewById(R.id.menuBuyer).setOnClickListener(this);
        findViewById(R.id.menuTransaction).setOnClickListener(this);
        findViewById(R.id.menuSetting).setOnClickListener(this);
    }

    private void logoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(true);
        alertDialog.setTitle("Yakin ingin keluar?");
        alertDialog.setPositiveButton(R.string.alert_yes_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userLocalStore.setUserLoggedIn(false);
                runActivity(LoginActivity.class);
                userLocalStore.clearUserData();
                Glide.get(getApplicationContext()).clearMemory();
                finish();
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.alert_no_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        eventListener(null, v);
    }

    //API
    public void getRecent() {
        swipeRefreshLayout.setRefreshing(true);
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Error!");

        String orderUrl = ServerAPI.RECENT + "/" + user.getId();
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
