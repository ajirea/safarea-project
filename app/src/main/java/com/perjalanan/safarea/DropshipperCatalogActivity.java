package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.perjalanan.safarea.adapters.DropshipperCatalogListAdapter;
import com.perjalanan.safarea.data.CatalogItem;
import com.perjalanan.safarea.helpers.ToolbarHelper;

public class DropshipperCatalogActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private ArrayList<CatalogItem> catalogList;
    private RecyclerView mRecyclerView;
    private DropshipperCatalogListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropshipper_catalog);

        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle("Katalog Produk");

        // recycler view
        catalogList = exampleCatalogData();
        mRecyclerView = findViewById(R.id.dropshipperCatalogRecyclerView);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mAdapter = new DropshipperCatalogListAdapter(catalogList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new GridItemDecoration(2, 30, true));

        mAdapter.setOnItemClickListener(new DropshipperCatalogListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer position) {
                Intent intent = new Intent(DropshipperCatalogActivity.this, DropshipperCatalogDetailActivity.class);
                intent.putExtra("Catalog Item", catalogList.get(position));
                startActivity(intent);
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

    /**
     * Contoh data untuk catalog items
     * @return ArrayList<CatalogItem>
     */
    private ArrayList<CatalogItem> exampleCatalogData() {
        ArrayList<CatalogItem> exCatalog = new ArrayList<>();

        CatalogItem item1 = new CatalogItem(
                1,
                R.drawable.sample_product,
                "Kids Pajama Short Sleeves",
                80000D
        );

        CatalogItem item2 = new CatalogItem(
                2,
                R.drawable.sample_product,
                "Kids Pajama Long Sleeves",
                90000D
        );

        exCatalog.add(item1);
        exCatalog.add(item2);

        return exCatalog;
    }
}