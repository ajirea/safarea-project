package com.perjalanan.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.perjalanan.safarea.adapters.StockListAdapter;
import com.perjalanan.safarea.data.StockItem;
import com.perjalanan.safarea.helpers.ToolbarHelper;

import android.os.Bundle;

import java.util.ArrayList;

public class StockActivity extends AppCompatActivity {

    private ArrayList<StockItem> itemList;
    private ToolbarHelper toolbarHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        // atur custom toolbar
        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle(getString(R.string.text_stock_product));

        // stock list recycler view
        itemList = exampleStockData();
        mRecyclerView = findViewById(R.id.stockListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StockListAdapter(itemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void removeItem(Integer position) {
        itemList.remove((int)position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, itemList.size());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * Contoh data untuk stock barang
     * @return ArrayList<StockItem>
     */
    private ArrayList<StockItem> exampleStockData() {
        ArrayList<StockItem> exStock = new ArrayList<>();

        StockItem item1 = new StockItem(
                1, 1, 1, 5,
                R.drawable.sample_product,
                30000F, "Kids Pajama Short Sleeves",
                "active"
        );
        StockItem item2 = new StockItem(
                2, 2, 1, 15,
                R.drawable.sample_product,
                30000F, "Kids Pajama Long Sleeves",
                "active"
        );

        exStock.add(item1);
        exStock.add(item2);

        return exStock;
    }
}
