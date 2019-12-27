package my.id.satria.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.id.satria.safarea.adapters.BuyerListAdapter;
import my.id.satria.safarea.data.BuyerItem;
import my.id.satria.safarea.helpers.ToolbarHelper;

import android.os.Bundle;

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
        toolbarHelper.initToolbar();

        // recyclerview buyer
        mRecyclerView = findViewById(R.id.buyerListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BuyerListAdapter(exampleBuyerData());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
