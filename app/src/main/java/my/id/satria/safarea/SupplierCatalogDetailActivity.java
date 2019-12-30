package my.id.satria.safarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import my.id.satria.safarea.adapters.CatalogImageAdapter;
import my.id.satria.safarea.data.CatalogItem;
import my.id.satria.safarea.dialogs.AddStockDialog;
import my.id.satria.safarea.dialogs.SuccessAddStockDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SupplierCatalogDetailActivity extends AppCompatActivity
        implements AddStockDialog.AddStockDialogListener,
        SuccessAddStockDialog.SuccessAddStockDialogListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_catalog_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        CatalogItem catalogItem = intent.getParcelableExtra("Catalog Item");
        catalogItem.setDescription("Donec at eros sagittis, porta erat scelerisque, tincidunt est. Vivamus quis imperdiet ante, eu bibendum nibh. Suspendisse potenti. Nulla non mollis libero. In euismod eros eget lacus commodo congue. Praesent a finibus enim. Nunc sit amet neque sit amet dolor blandit consectetur mattis in purus. Donec ut tellus enim. Duis at iaculis tellus. Nulla condimentum facilisis mauris vel ullamcorper. Suspendisse sed dignissim turpis.");
        catalogItem.getImages().add(new String[]{
                "https://i.picsum.photos/id/357/360/300.jpg",
                "Satu"
        });
        catalogItem.getImages().add(new String[]{
                "https://i.picsum.photos/id/1041/360/300.jpg",
                "Dua"
        });
        catalogItem.getImages().add(new String[]{
                "https://i.picsum.photos/id/882/360/300.jpg",
                "Tiga"
        });

        ViewPager imagePager = findViewById(R.id.productDetailImage);
        CatalogImageAdapter imageAdapter = new CatalogImageAdapter(this, catalogItem.getImages());
        imagePager.setAdapter(imageAdapter);

        TextView titleCatalog = findViewById(R.id.titleCatalog);
        TextView textPrice = findViewById(R.id.textPrice);
        TextView textStock = findViewById(R.id.textStock);
        TextView textDesc = findViewById(R.id.textDescription);

        toolbar.setTitle(catalogItem.getTitle());
        titleCatalog.setText(catalogItem.getTitle());
        textStock.setText("2 Stock tersedia");
        textDesc.setText(catalogItem.getDescription());
        textPrice.setText(catalogItem.getPrice().toString());

        Button btnAddStock = findViewById(R.id.btnAddStock);
        btnAddStock.setOnClickListener(l -> {
            AddStockDialog addStockDialog = new AddStockDialog();
            addStockDialog.show(getSupportFragmentManager(), "addStockDialog");
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    // pada fragment dialog_add_stock.xml
    @Override
    public void onButtonClicked(Integer stock, Integer profit, String type) {
        String alertMessage = getString(R.string.text_stock_message_take);

        if(type == "send")
            alertMessage = getString(R.string.text_stock_message_send);

        SuccessAddStockDialog sasd = new SuccessAddStockDialog(alertMessage);
        sasd.show(getSupportFragmentManager(), "successAddStockDialog");
    }

    // pada fragment success_dialog_add_stock.xml
    // untuk handle ketika tombol di pesan sukses di klik
    @Override
    public void onButtonClicked() {
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);
        finish();
    }
}
