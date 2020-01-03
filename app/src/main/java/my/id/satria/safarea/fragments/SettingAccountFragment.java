package my.id.satria.safarea.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.navigation.Navigation;
import my.id.satria.safarea.R;
import my.id.satria.safarea.SettingActivity;
import my.id.satria.safarea.data.User;
import my.id.satria.safarea.helpers.ProgressDialogHelper;
import my.id.satria.safarea.repositories.RequestGlobalHeaders;
import my.id.satria.safarea.repositories.ServerAPI;
import my.id.satria.safarea.repositories.UserLocalStore;

/**
 * Fragment pengaturan akun
 * di fragment ini akan ditampilkan pengaturan akun general
 * Seperti nama, username, email, no telp, dan avatar
 */
public class SettingAccountFragment extends Fragment {

    private UserLocalStore userLocalStore;
    private User user;
    private Toolbar toolbar;
    private ImageView imageAvatar;
    private EditText fieldName, fieldUsername, fieldPhoneNumber, fieldEmail, fieldStoreName;
    private RequestQueue requestQueue;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_account, container, false);

        // atur fragment agar memiliki akses ke toolbar
        setHasOptionsMenu(true);

        context = container.getContext();

        // load data dropshipper yg login
        userLocalStore = new UserLocalStore(context);
        user = userLocalStore.getLoggedInUser();

        // start request queue volley
        requestQueue = Volley.newRequestQueue(container.getContext());

        // components
        imageAvatar = view.findViewById(R.id.imageAvatar);
        fieldName = view.findViewById(R.id.fieldName);
        fieldUsername = view.findViewById(R.id.fieldUsername);
        fieldPhoneNumber = view.findViewById(R.id.fieldPhoneNumber);
        fieldEmail = view.findViewById(R.id.fieldEmail);
        fieldStoreName = view.findViewById(R.id.fieldStoreName);
        Button btnChangeAddress = view.findViewById(R.id.btnChangeAddress);
        Button btnChangePassword = view.findViewById(R.id.btnChangePassword);

        // set avatar
        Glide.with(imageAvatar.getContext())
                .load(ServerAPI.BASE_URL + user.getAvatar())
                .into(imageAvatar);

        // set text
        fieldName.setText(user.getName());
        fieldUsername.setText(user.getUsername());
        fieldUsername.setEnabled(false);
        fieldPhoneNumber.setText(user.getPhone());
        fieldEmail.setText(user.getEmail());
        fieldStoreName.setText(user.getStoreName());

        // event handling
        btnChangeAddress.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_settingAccountFragment_to_settingAddressFragment)
        );

        btnChangePassword.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_settingAccountFragment_to_settingPasswordFragment)
        );

        // toolbar handler
        try {
            ((SettingActivity) getActivity()).getToolbar().setOnMenuItemClickListener(i -> {
                if(i.getItemId() == R.id.save_item) {
                    saveAccount();
                }
                return false;
            });
        } catch (NullPointerException e) {
            e.getStackTrace();
        }

        return view;
    }

    /**
     * Menambahkan menu item ke toolbar (menu item di sini adalah tombol centang)
     * @param menu menu
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        // atur ulang toolbar title
        try {
            ((SettingActivity) getActivity()).setToolbarTitle(getString(R.string.text_pengaturan));
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
        super.onResume();
    }

    /**
     * Menu untuk memeriksa apakah komponen edit teks kosong atau tidak
     * @param item EditText
     * @return Boolean
     */
    private Boolean isEmpty(EditText item) {
        return fieldStoreName.getText().toString().trim().equals("");
    }

    /**
     * Fungsi untuk menyimpan informasi akun yang sudah di edit
     */
    private void saveAccount() {
        // membuat alert dialog untuk request yang selesai
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(true);
        alert.setTitle("Error!");
        alert.setPositiveButton(R.string.alert_yes_btn, (dialog, which) -> dialog.dismiss());

        // cek apakah ada yang kosong
        if(isEmpty(fieldName) || isEmpty(fieldPhoneNumber) || isEmpty(fieldStoreName)) {
            alert.setTitle("Error!");
            alert.setMessage("Nama, nama toko, dan nomor telepon tidak boleh kosong");
            alert.show();
            return;
        }

        // membuat progress dialog
        ProgressDialogHelper pdh = new ProgressDialogHelper(context);
        pdh.setMessage("Sedang menyimpan");
        pdh.show();

        // request simpan data user
        String url = ServerAPI.EDIT_ACCOUNT.concat(userLocalStore.getLoggedInUser().getUsername());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                // parsing json string ke object
                JSONObject resp = new JSONObject(response);

                // cek apakah status true or false
                if(resp.getBoolean("status")) {
                    alert.setTitle("Sukses!");
                    alert.setMessage("Berhasil menyimpan pengaturan");
                    alert.show();

                    User user = userLocalStore.getLoggedInUser();
                    user.setName(fieldName.getText().toString());
                    user.setStoreName(fieldStoreName.getText().toString());
                    user.setPhone(fieldPhoneNumber.getText().toString());

                    userLocalStore.storeUserData(user);
                } else {
                    alert.setMessage(resp.getJSONObject("data").getString("message"));
                    alert.show();
                }
            } catch (JSONException e) {
                alert.setMessage("Terjadi kesalahan pada server");
                alert.show();
                e.printStackTrace();
            }

            pdh.dismiss();
        }, error -> {
            alert.setMessage(error.getMessage());
            alert.show();
            pdh.dismiss();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // mengambil global header
                return RequestGlobalHeaders.get(context);
            }

            @Override
            protected Map<String, String> getParams() {
                // membuat body data untuk request
                Map<String, String> params = new HashMap<>();
                params.put("name", fieldName.getText().toString());
                params.put("store_name", fieldStoreName.getText().toString());
                params.put("phone", fieldPhoneNumber.getText().toString());

                return params;
            }
        };

        requestQueue.add(postRequest);
    }
}
