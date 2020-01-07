package com.perjalanan.safarea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.helpers.ProgressDialogHelper;
import com.perjalanan.safarea.helpers.ToolbarHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuyerAddActivity extends AppCompatActivity {
    private EditText fieldName, fieldPhone, fieldAddress, fieldVillage, fieldDistrict, fieldCity,
        fieldProvince, fieldPos;
    private  AlertDialog.Builder alert;
    private ProgressDialogHelper progressDialogHelper;
    private RequestQueue requestQueue;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_add);

        // atur custom toolbar
        ToolbarHelper toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar();
        toolbarHelper.setToolbarTitle(getString(R.string.text_add_buyer));

        // components
        fieldName = findViewById(R.id.fieldName);
        fieldPhone = findViewById(R.id.fieldPhoneNumber);
        fieldAddress = findViewById(R.id.fieldAddress);
        fieldVillage = findViewById(R.id.fieldVillage);
        fieldDistrict = findViewById(R.id.fieldDistrict);
        fieldCity = findViewById(R.id.fieldCity);
        fieldProvince = findViewById(R.id.fieldProvince);
        fieldPos = findViewById(R.id.fieldPos);

        // request
        requestQueue = Volley.newRequestQueue(this);

        // local store
        userLocalStore = new UserLocalStore(this);

        // alert and progress dialog
        alert = new AlertDialog.Builder(this).setTitle("Error!")
                .setPositiveButton(R.string.alert_yes_btn, null);
        progressDialogHelper = new ProgressDialogHelper(this);
        progressDialogHelper.setTitle(getString(R.string.text_saving));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_item)
            storeBuyer();

        return false;
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
     * method untuk menyimpan pembeli
     */
    private void storeBuyer() {

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
        String url = ServerAPI.BUYER_ALL_DATA;
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

            try {
                JSONObject resp = new JSONObject(response);

                if(resp.getBoolean("status")) {
                    alert.setTitle("Sukses!")
                            .setMessage(resp.getJSONObject("data").getString("message"))
                            .show();
                    clearAllFields();
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
                params.put("user_id", Integer.toString(userLocalStore.getLoggedInUser().getId()));
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

    private void clearAllFields() {
        fieldName.setText("");
        fieldPhone.setText("");
        fieldAddress.setText("");
        fieldVillage.setText("");
        fieldDistrict.setText("");
        fieldCity.setText("");
        fieldProvince.setText("");
        fieldPos.setText("");
    }
}
