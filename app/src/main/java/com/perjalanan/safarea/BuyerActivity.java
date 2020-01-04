package com.perjalanan.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.perjalanan.safarea.adapters.BuyerListAdapter;
import com.perjalanan.safarea.data.BuyerItem;
import com.perjalanan.safarea.helpers.ToolbarHelper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BuyerActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
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

        // recyclerview buyer
        mRecyclerView = findViewById(R.id.buyerListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BuyerListAdapter(exampleBuyerData());

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
}
