package my.id.satria.safarea;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.id.satria.safarea.adapters.TransactionListAdapter;
import my.id.satria.safarea.helpers.ToolbarHelper;

public class TransactionActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //Custom Toolbarrrrrrrrrrrr
        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle(getString(R.string.text_buyer_lists));

        //Recycle view
        mRecyclerView = findViewById(R.id.transactionListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        //mAdapter = new TransactionListAdapter()
    }
}
