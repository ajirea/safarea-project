package com.perjalanan.safarea;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.perjalanan.safarea.data.TransactionItem;
import com.perjalanan.safarea.helpers.FormatHelper;
import com.perjalanan.safarea.helpers.ToolbarHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class
TransactionDetaiActivity extends AppCompatActivity {

    private ToolbarHelper toolbarHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar(true);
        toolbarHelper.setToolbarTitle("Detail Transaksi");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        //parcelable
        TransactionItem transactionItem = intent.getParcelableExtra("Detail Transaksi");

        ImageView imageView = findViewById(R.id.imageView);
        TextView labelBuyer = findViewById(R.id.lblPemesan);
        TextView labelPhoneNumber = findViewById(R.id.lblNotel);
        TextView labelTransactionNumber = findViewById(R.id.lblTransactionNumber);
        TextView prodName = findViewById(R.id.txtProduct);
        TextView ordDesc = findViewById(R.id.txtDesc);
        TextView ordDate = findViewById(R.id.txtDate);
        TextView quantity = findViewById(R.id.txtQty);
        TextView harga = findViewById(R.id.txtHarga);
        TextView totHarga = findViewById(R.id.txtTotHarga);

        Glide.with(imageView).load(transactionItem.getImage())
                .centerCrop()
                .into(imageView);

        labelTransactionNumber.setText(getString(
                R.string.label_transaction_number, String.valueOf(transactionItem.getId())
        ));

        labelBuyer.setText(getString(
                R.string.text_buyer_name, transactionItem.getName()
        ));

        labelPhoneNumber.setText(getString(
                R.string.label_no_telp_format, transactionItem.getPhone()
        ));

        prodName.setText(transactionItem.getProduct());
        ordDesc.setText(transactionItem.getOrderDesc());
        ordDate.setText(transactionItem.getOrderedAt());
        quantity.setText(String.valueOf(transactionItem.getQty()));
        harga.setText(FormatHelper.priceFormat(transactionItem.getPrice()));
        totHarga.setText(FormatHelper.priceFormat(transactionItem.getTotalPrice()));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
