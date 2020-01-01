package my.id.satria.safarea;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

import my.id.satria.safarea.data.TransactionItem;

public class TransactionDetaiActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        TransactionItem transactionItem = intent.getParcelableExtra("Detail Transaksi");
        transactionItem.setOrderDesc("Donec at eros sagittis, porta erat scelerisque, tincidunt est. Vivamus quis imperdiet ante, eu bibendum nibh. Suspendisse potenti. Nulla non mollis libero. In euismod eros eget lacus commodo congue. Praesent a finibus enim. Nunc sit amet neque sit amet dolor blandit consectetur mattis in purus. Donec ut tellus enim. Duis at iaculis tellus. Nulla condimentum facilisis mauris vel ullamcorper. Suspendisse sed dignissim turpis.");

        int imageRes = transactionItem.getImage();
        int id = transactionItem.getId();
        int userid = transactionItem.getUserid();
        String name = transactionItem.getName();
        String phone = transactionItem.getPhone();
        String product = transactionItem.getProduct();
        String date = transactionItem.getOrderedAt();
        String desc = transactionItem.getOrderDesc();
        double qty = transactionItem.getQty();
        double price = transactionItem.getPrice();
        double totPrice = transactionItem.getTotalPrice();

        ImageView imageView = findViewById(R.id.imageView);
        TextView buyer = findViewById(R.id.txtPemesan);
        TextView notel = findViewById(R.id.txtNotel);
        TextView prodName = findViewById(R.id.txtProduct);
        TextView ordDesc = findViewById(R.id.txtDesc);
        TextView ordDate = findViewById(R.id.txtDate);
        TextView quantity = findViewById(R.id.txtQty);
        TextView harga = findViewById(R.id.txtHarga);
        TextView totHarga = findViewById(R.id.txtTotHarga);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
