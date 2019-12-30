package my.id.satria.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.id.satria.safarea.adapters.SupplierCatalogListAdapter;
import my.id.satria.safarea.data.CatalogItem;
import my.id.satria.safarea.data.StockItem;
import my.id.satria.safarea.helpers.ToolbarHelper;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class SupplierCatalogActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private ArrayList<CatalogItem> catalogList;
    private RecyclerView mRecyclerView;
    private SupplierCatalogListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_catalog);

        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar();
        toolbarHelper.setToolbarTitle("Katalog Produk");

        // recycler view
        catalogList = exampleCatalogData();
        mRecyclerView = findViewById(R.id.supplierCatalogRecyclerView);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mAdapter = new SupplierCatalogListAdapter(catalogList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new GridItemDecoration(2, 30, true));

        mAdapter.setOnItemClickListener(new SupplierCatalogListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer position) {
                Intent intent = new Intent(SupplierCatalogActivity.this, SupplierCatalogDetailActivity.class);
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
                80000F
        );

        CatalogItem item2 = new CatalogItem(
                2,
                R.drawable.sample_product,
                "Kids Pajama Long Sleeves",
                90000F
        );

        exCatalog.add(item1);
        exCatalog.add(item2);

        return exCatalog;
    }
}
