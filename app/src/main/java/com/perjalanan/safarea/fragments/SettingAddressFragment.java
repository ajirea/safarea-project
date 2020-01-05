package com.perjalanan.safarea.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perjalanan.safarea.R;
import com.perjalanan.safarea.SettingActivity;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.helpers.ProgressDialogHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragment untuk menampilkan pengaturan alamat dropshipper
 */
public class SettingAddressFragment extends Fragment {

    private Context context;
    private User user;
    private UserLocalStore userLocalStore;
    private RequestQueue requestQueue;
    private EditText fieldAddress, fieldVillage, fieldDistrict, fieldCity, fieldProvince, fieldPos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_address, container, false);

        // atur fragment agar memiliki akses ke toolbar
        setHasOptionsMenu(true);

        context = container.getContext();

        // load data
        userLocalStore = new UserLocalStore(container.getContext());
        user = userLocalStore.getLoggedInUser();

        // instantiate request queue
        requestQueue = Volley.newRequestQueue(context);

        // get components
        fieldAddress = view.findViewById(R.id.fieldAddress);
        fieldVillage = view.findViewById(R.id.fieldVillage);
        fieldDistrict = view.findViewById(R.id.fieldDistrict);
        fieldCity = view.findViewById(R.id.fieldCity);
        fieldProvince = view.findViewById(R.id.fieldProvince);
        fieldPos = view.findViewById(R.id.fieldPos);

        // set text
        fieldAddress.setText(user.getAddress());
        fieldVillage.setText(user.getVillage());
        fieldDistrict.setText(user.getDistrict());
        fieldCity.setText(user.getCity());
        fieldProvince.setText(user.getProvince());
        fieldPos.setText(user.getPostalCode());

        // atur toolbar title
        try {
            ((SettingActivity) getActivity()).setToolbarTitle(getString(R.string.text_change_address));
            ((SettingActivity) getActivity()).getToolbar().setOnMenuItemClickListener(item -> {
                if(item.getItemId() == R.id.save_item)
                    saveAddress();
                return false;
            });
        } catch (NullPointerException e) {
            e.getStackTrace();
        }

        return view;
    }

    /**
     * Menambahkan menu item ke toolbar (menu item di sini adalah tombol centang)
     * @param menu Menu
     * @param inflater MenuInflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Menu untuk memeriksa apakah komponen edit teks kosong atau tidak
     * @param item EditText
     * @return Boolean
     */
    private static Boolean isTextEmpty(EditText item) {
        return item.getText().toString().trim().equals("");
    }

    /**
     * Method untuk menyimpan alamat
     */
    private void saveAddress() {
        ProgressDialogHelper pdh = new ProgressDialogHelper(context);
        pdh.setMessage(getString(R.string.text_saving));

        AlertDialog.Builder alert = new AlertDialog.Builder(context).setTitle("Error!")
                .setPositiveButton(R.string.alert_yes_btn, null);

        boolean valid = false;

        if(isTextEmpty(fieldAddress)) {
            alert.setMessage("Alamat tidak boleh kosong").show();
        } else if(isTextEmpty(fieldVillage)) {
            alert.setMessage("Desa tidak boleh kosong").show();
        } else if(isTextEmpty(fieldDistrict)) {
            alert.setMessage("Kecamatan tidak boleh kosong").show();
        } else if(isTextEmpty(fieldCity)) {
            alert.setMessage("Kab/Kota tidak boleh kosong").show();
        } else if(isTextEmpty(fieldProvince)) {
            alert.setMessage("Provinsi tidak boleh kosong").show();
        } else if(isTextEmpty(fieldPos)) {
            alert.setMessage("Kode POS tidak boleh kosong").show();
        } else {
            valid = true;
        }

        if(!valid) return;

        pdh.show();
        String url = ServerAPI.CHANGE_ADDRESS_ACCOUNT;
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

            try {
                JSONObject resp = new JSONObject(response);
                if(resp.getBoolean("status")) {
                    user.setAddress(fieldAddress.getText().toString());
                    user.setVillage(fieldVillage.getText().toString());
                    user.setDistrict(fieldDistrict.getText().toString());
                    user.setCity(fieldCity.getText().toString());
                    user.setProvince(fieldProvince.getText().toString());
                    user.setPostalCode(fieldPos.getText().toString());

                    userLocalStore.storeUserData(user);

                    alert.setTitle("Sukses");
                }

                alert.setMessage(resp.getJSONObject("data").getString("message"))
                        .show();

            } catch (JSONException e) {
                alert.setMessage(e.getMessage()).show();
                e.printStackTrace();
            }
            pdh.dismiss();
        }, error -> pdh.dismiss()) {
            @Override
            public Map<String, String> getHeaders() {
                return RequestGlobalHeaders.get(context);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
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
