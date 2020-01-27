package com.perjalanan.safarea;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.data.BuyerItem;
import com.perjalanan.safarea.data.CatalogItem;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.helpers.ProgressDialogHelper;
import com.perjalanan.safarea.helpers.ToolbarHelper;
import com.perjalanan.safarea.helpers.ValidationHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TransactionAddActivity extends AppCompatActivity {
    private static final Integer REQUEST_BUYER_OK = 1,
            REQUEST_PRODUCT_OK = 2;

    private ToolbarHelper toolbarHelper;
    private String dateTime;
    private EditText fieldDate, fieldName, fieldPhone, fieldProduct, fieldQty, fieldDesc;
    private BuyerItem buyer;
    private CatalogItem product;
    private User user;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_add);

        // atur custom toolbar
        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar();
        toolbarHelper.setToolbarTitle(getString(R.string.text_add_transaction));

        // get user local store
        UserLocalStore userLocalStore = new UserLocalStore(getBaseContext());
        user = userLocalStore.getLoggedInUser();

        // init request queue
        requestQueue = Volley.newRequestQueue(this);

        // components
        fieldName = findViewById(R.id.fieldName);
        fieldDate = findViewById(R.id.fieldDate);
        fieldProduct = findViewById(R.id.fieldNameItem);
        fieldPhone = findViewById(R.id.fieldPhoneNumber);
        fieldQty = findViewById(R.id.fieldQty);
        fieldDesc = findViewById(R.id.fieldDesc);
        fieldDate.setOnClickListener(l -> showDatePicker());
        fieldDate.setClickable(true);
        fieldDate.setInputType(InputType.TYPE_NULL);

        // atur form nama pembeli menjadi hanya bisa di baca dan bisa di klik
        fieldName.setInputType(InputType.TYPE_NULL);
        fieldName.setClickable(true);
        fieldPhone.setInputType(InputType.TYPE_NULL);
        fieldProduct.setInputType(InputType.TYPE_NULL);
        fieldProduct.setClickable(true);

        // event handling
        fieldName.setOnClickListener(l -> {
            Intent intent = new Intent(TransactionAddActivity.this, BuyerActivity.class);
            intent.putExtra("isSelectingBuyer", true);
            startActivityForResult(intent, REQUEST_BUYER_OK);
        });

        fieldProduct.setOnClickListener(l -> {
            Intent intent = new Intent(TransactionAddActivity.this, DropshipperCatalogActivity.class);
            intent.putExtra("isSelectingProduct", true);
            startActivityForResult(intent, REQUEST_PRODUCT_OK);
        });
//        fieldName.setOnFocusChangeListener((l, hasFocus) -> {
//            if (!hasFocus) return;
//            l.clearFocus();
//            Intent intent = new Intent(TransactionAddActivity.this, BuyerActivity.class);
//            intent.putExtra("isSelectingBuyer", true);
//            startActivityForResult(intent, REQUEST_BUYER_OK);
//        });

//        fieldProduct.setOnFocusChangeListener((l, hasFocus) -> {
//            if (!hasFocus) return;
//            l.clearFocus();
//            Intent intent = new Intent(TransactionAddActivity.this, DropshipperCatalogActivity.class);
//            intent.putExtra("isSelectingProduct", true);
//            startActivityForResult(intent, REQUEST_PRODUCT_OK);
//        });
    }

    private String zeroFormat(Integer num) {
        if (num < 10)
            return "0" + num;
        return String.valueOf(num);
    }

    private void showDatePicker() {
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {

                    dateTime = year + "-" + zeroFormat((month + 1)) + "-" + zeroFormat(dayOfMonth);

                    showTimePicker();

                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar cal = Calendar.getInstance();
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    dateTime = dateTime + " " + zeroFormat(hourOfDay) + ":" + zeroFormat(minute)
                            + ":" + cal.get(Calendar.SECOND);
                    fieldDate.setText(dateTime);
                },
                mHour, mMinute, false);

        timePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.save_item)
            storeTransaction();

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_BUYER_OK) {
            if (resultCode == RESULT_OK) {
                buyer = Objects.requireNonNull(data).getParcelableExtra("buyerItem");
                fieldName.setText(Objects.requireNonNull(buyer).getName());
                fieldPhone.setText(Objects.requireNonNull(buyer).getPhone());

            } else if (resultCode == RESULT_CANCELED) {
                buyer = null;
                fieldName.setText("");
                fieldPhone.setText("");
            }
        } else if (requestCode == REQUEST_PRODUCT_OK) {
            if (resultCode == RESULT_OK) {
                product = Objects.requireNonNull(data).getParcelableExtra("productItem");
                fieldProduct.setText(Objects.requireNonNull(product).getTitle());

            } else if (resultCode == RESULT_CANCELED) {
                buyer = null;
                fieldName.setText("");
                fieldPhone.setText("");
            }
        }
    }

    /**
     * method untuk menyimpan transaksi ke database malalui API
     */
    private void storeTransaction() {
        ProgressDialogHelper progressDialogHelper = new ProgressDialogHelper(this);
        progressDialogHelper.setMessage(getString(R.string.alert_please_wait_message));
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setPositiveButton(R.string.alert_yes_btn, null);

        if (ValidationHelper.isTextEmpty(fieldName, "Nama Pembeli", true, getApplicationContext())) {
            return;
        } else if (ValidationHelper.isTextEmpty(fieldPhone, "Nomor Telepon", true, getApplicationContext())) {
            return;
        } else if (ValidationHelper.isTextEmpty(fieldDate, "Tanggal", true, getApplicationContext())) {
            return;
        } else if (ValidationHelper.isTextEmpty(fieldProduct, "Barang", true, getApplicationContext())) {
            return;
        } else if (ValidationHelper.isTextEmpty(fieldQty, "Barang", true, getApplicationContext())) {
            return;
        }

        progressDialogHelper.show();
        String url = ServerAPI.ORDER + "/" + user.getId();
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject resp = new JSONObject(response);

                if (resp.getBoolean("status")) {
                    alert.setTitle("Sukses!");
                }

                alert.setMessage(resp.getJSONObject("data").getString("message"))
                        .show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialogHelper.dismiss();
        }, error -> {
            error.printStackTrace();
            progressDialogHelper.dismiss();
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("buyer_id", buyer.getId().toString());
                params.put("product_id", product.getId().toString());
                params.put("created_at", fieldDate.getText().toString());
                params.put("qty", fieldQty.getText().toString());
                params.put("description", fieldDesc.getText().toString());

                System.out.println(params.toString());

                return params;
            }
        };

        requestQueue.add(request);
    }
}
