package com.perjalanan.safarea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.helpers.ProgressDialogHelper;
import com.perjalanan.safarea.helpers.ToolbarHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuyerEditActivity extends AppCompatActivity {
    private EditText fieldName, fieldPhone, fieldAddress, fieldVillage, fieldDistrict, fieldCity,
            fieldProvince, fieldPos;
    private  AlertDialog.Builder alert, alertDelete;
    private ProgressDialogHelper progressDialogHelper;
    private RequestQueue requestQueue;
    private User user;
    private Integer buyerId;
    private String apiUrl;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_edit);

        // atur custom toolbar
        ToolbarHelper toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar();
        toolbarHelper.setToolbarTitle(getString(R.string.text_edit_buyer));

        Intent intent = getIntent();
        buyerId = intent.getIntExtra("buyerId", 0);

        // cek apabila buyerId sama dengan 0
        if(buyerId == 0) onBackPressed();

        // local store
        UserLocalStore userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        // setting api url
        apiUrl = ServerAPI.BUYER_ALL_DATA + "/" + buyerId + "/" + user.getId();

        // components
        fieldName = findViewById(R.id.fieldName);
        fieldPhone = findViewById(R.id.fieldPhoneNumber);
        fieldAddress = findViewById(R.id.fieldAddress);
        fieldVillage = findViewById(R.id.fieldVillage);
        fieldDistrict = findViewById(R.id.fieldDistrict);
        fieldCity = findViewById(R.id.fieldCity);
        fieldProvince = findViewById(R.id.fieldProvince);
        fieldPos = findViewById(R.id.fieldPos);
        swipeLayout = findViewById(R.id.swipeLayout);
        swipeLayout.setRefreshing(true);
        FloatingActionButton btnDelete = findViewById(R.id.btnDelete);

        // request
        requestQueue = Volley.newRequestQueue(this);

        // alert and progress dialog
        alert = new AlertDialog.Builder(this).setTitle("Error!")
                .setPositiveButton(R.string.alert_yes_btn, null);
        alertDelete = new AlertDialog.Builder(this).setTitle("Yakin akan menghapus pembeli?")
                .setPositiveButton(R.string.alert_yes_btn, (dialog, which) -> deleteBuyer())
                .setNegativeButton(R.string.alert_no_btn, null);

        progressDialogHelper = new ProgressDialogHelper(this);
        progressDialogHelper.setTitle(getString(R.string.alert_please_wait_message));

        // event handling
        btnDelete.setOnClickListener(l -> alertDelete.show());
        swipeLayout.setOnRefreshListener(this::loadBuyer);

        loadBuyer();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_item)
            saveBuyer();

        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Method untuk memeriksa apakah suatu field kosong
     * @param field EditText
     * @return Boolean
     */
    private Boolean isFieldEmpty(EditText field) {
        return field.getText().toString().trim().equals("");
    }

    /**
     * Method untuk menampilkan pesan / toast
     */
    private void showToastRequired(String message) {
        Toast.makeText(this, getString(R.string.validation_required, message),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Method untuk menghapus pembeli dari database
     */
    private void deleteBuyer() {
        progressDialogHelper.show();
        StringRequest request = new StringRequest(Request.Method.DELETE, apiUrl, response -> {
            try {
                JSONObject resp = new JSONObject(response);

                if(resp.getBoolean("status")) {
                    alert.setTitle("Sukses!");
                    alert.setOnDismissListener(l -> {
                        onBackPressed();
                    });
                }
                alert.setMessage(resp.getJSONObject("data").getString("message")).show();
            } catch (JSONException e) {
                e.printStackTrace();
                alert.setMessage(e.getMessage()).show();
            }

            progressDialogHelper.dismiss();

        }, error -> {
            progressDialogHelper.dismiss();
            alert.setMessage(error.getMessage()).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }
        };

        requestQueue.add(request);
    }

    /**
     * method untuk mendapatkan data pembeli berdasarkan buyerId dari database
     * melalui API
     */
    private void loadBuyer() {
        swipeLayout.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null, response -> {

            try {
                if(!response.getBoolean("status")) {
                    alert.setMessage(response.getJSONObject("data").getString("message"))
                        .show();
                } else {
                    JSONObject buyer = response.getJSONObject("data");
                    fieldName.setText(buyer.getString("name"));
                    fieldPhone.setText(buyer.getString("phone"));
                    fieldAddress.setText(buyer.getString("address"));
                    fieldVillage.setText(buyer.getString("village"));
                    fieldDistrict.setText(buyer.getString("district"));
                    fieldCity.setText(buyer.getString("city"));
                    fieldProvince.setText(buyer.getString("province"));
                    fieldPos.setText(buyer.getString("postal_code"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                alert.setMessage(e.getMessage()).show();
            }

            swipeLayout.setRefreshing(false);

        }, error -> {
            swipeLayout.setRefreshing(false);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }
        };

        requestQueue.add(request);
    }

    /**
     * method untuk menyimpan hasil edit pembeli
     */
    private void saveBuyer() {

        boolean isValid = false;

        if(isFieldEmpty(fieldName)) {
            showToastRequired("Nama");
        } else if(isFieldEmpty(fieldPhone)) {
            showToastRequired("Nomor telepon");
        } else if(isFieldEmpty(fieldAddress)) {
            showToastRequired("Alamat");
        } else if(isFieldEmpty(fieldVillage)) {
            showToastRequired("Desa");
        } else if(isFieldEmpty(fieldDistrict)) {
            showToastRequired("Kecamatan");
        } else if(isFieldEmpty(fieldCity)) {
            showToastRequired("Kab/Kota");
        } else if(isFieldEmpty(fieldProvince)) {
            showToastRequired("Provinsi");
        } else if(isFieldEmpty(fieldPos)) {
            showToastRequired("Kode POS");
        } else
            isValid = true;

        if(!isValid) return;

        progressDialogHelper.show();
        StringRequest request = new StringRequest(Request.Method.POST, apiUrl, response -> {

            try {
                JSONObject resp = new JSONObject(response);

                if(resp.getBoolean("status")) {
                    alert.setTitle("Sukses!")
                            .setMessage(resp.getJSONObject("data").getString("message"))
                            .show();
                } else {
                    alert.setMessage(resp.getJSONObject("data")
                            .getString("message")).show();
                }

                progressDialogHelper.dismiss();

            } catch (JSONException e) {
                e.getStackTrace();
                alert.setMessage(e.getMessage()).show();
                progressDialogHelper.dismiss();
            }

        }, error -> {
            alert.setMessage(error.getMessage()).show();
            progressDialogHelper.dismiss();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return RequestGlobalHeaders.get(getApplicationContext());
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", fieldName.getText().toString());
                params.put("phone", fieldPhone.getText().toString());
                params.put("address", fieldAddress.getText().toString());
                params.put("village", fieldVillage.getText().toString());
                params.put("district", fieldDistrict.getText().toString());
                params.put("city", fieldCity.getText().toString());
                params.put("province", fieldProvince.getText().toString());
                params.put("postal_code", fieldPos.getText().toString());

                return params;
            }
        };

        requestQueue.add(request);
    }
}
