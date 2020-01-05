package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.perjalanan.safarea.adapters.TransactionListAdapter;
import com.perjalanan.safarea.data.TransactionItem;
import com.perjalanan.safarea.helpers.ToolbarHelper;

public class TransactionActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;
    private RecyclerView mRecyclerView;
    private TransactionListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //Custom Toolbar
        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle(getString(R.string.text_transaction_list));


        //Recycle view
        mRecyclerView = findViewById(R.id.transactionListRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TransactionListAdapter(this, exampleTransactionData());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TransactionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer position) {
                Intent intent = new Intent(TransactionActivity.this,TransactionDetaiActivity.class);
                intent.putExtra("Detail Transaksi",exampleTransactionData().get(position));
                startActivity(intent);
            }
        });
        // event
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

    private ArrayList<TransactionItem> exampleTransactionData() {
        ArrayList<TransactionItem> exTransaction = new ArrayList<>();

        TransactionItem transaction1 = new TransactionItem(
                R.drawable.sample_product,
                1,
                1,
                3,
                "Asep 1",
                "Pajama 1",
                "088392931848",
                "01/01/2020",
                "Contoh Order",
                80000.00,
                240000.00
        );

        TransactionItem transaction2 = new TransactionItem(
                R.drawable.sample_product,
                1,
                1,
                3,
                "Asep 2",
                "Pajama 2",
                "088392931848",
                "01/01/2020",
                "Contoh Order",
                70000.00,
                210000.00
        );

        TransactionItem transaction3 = new TransactionItem(
                R.drawable.sample_product,
                1,
                1,
                3,
                "Asep 3",
                "Pajama 3",
                "088392931848",
                "01/01/2020",
                "Contoh Order",
                60000.00,
                180000.00
        );

        TransactionItem transaction4 = new TransactionItem(
                R.drawable.sample_product,
                1,
                1,
                3,
                "Asep 4",
                "Pajama 4",
                "088392931848",
                "01/01/2020",
                "Contoh Order",
                50000.00,
                150000.00
        );

        TransactionItem transaction5 = new TransactionItem(
                R.drawable.sample_product,
                1,
                1,
                3,
                "Asep 5",
                "Pajama 5",
                "088392931848",
                "01/01/2020",
                "Contoh Order",
                100000.00,
                300000.00
        );

        exTransaction.add(transaction1);
        exTransaction.add(transaction2);
        exTransaction.add(transaction3);
        exTransaction.add(transaction4);
        exTransaction.add(transaction5);

        return exTransaction;
    }
}
