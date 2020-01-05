package com.perjalanan.safarea.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.navigation.Navigation;
import com.perjalanan.safarea.R;
import com.perjalanan.safarea.SettingActivity;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.helpers.MultipartRequest;
import com.perjalanan.safarea.helpers.ProgressDialogHelper;
import com.perjalanan.safarea.repositories.RequestGlobalHeaders;
import com.perjalanan.safarea.repositories.ServerAPI;
import com.perjalanan.safarea.repositories.UserLocalStore;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment pengaturan akun
 * di fragment ini akan ditampilkan pengaturan akun general
 * Seperti nama, username, email, no telp, dan avatar
 */
public class SettingAccountFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private UserLocalStore userLocalStore;
    private User user;
    private ImageView imageAvatar;
    private EditText fieldName, fieldUsername, fieldPhoneNumber, fieldEmail, fieldStoreName;
    private Uri imageUri;
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
        ImageButton btnPickImage = view.findViewById(R.id.btnPickImage);

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

        btnPickImage.setOnClickListener(l -> openFileChooser());

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
        return item.getText().toString().trim().equals("");
    }

    /**
     * Method untuk mengambil file dari storage smartphone
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null) {
            imageUri = data.getData();

            // set avatar
            Glide.with(imageAvatar.getContext())
                    .load(imageUri)
                    .centerCrop()
                    .into(imageAvatar);
        }

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
        MultipartRequest postRequest = new MultipartRequest(Request.Method.POST, url, response -> {
            try {
                // parsing json string ke object
                JSONObject resp = new JSONObject(new String(response.data));

                // cek apakah status true or false
                if(resp.getBoolean("status")) {
                    alert.setTitle("Sukses!");
                    alert.setMessage("Berhasil menyimpan pengaturan");
                    alert.show();

                    User user = userLocalStore.getLoggedInUser();
                    user.setName(fieldName.getText().toString());
                    user.setStoreName(fieldStoreName.getText().toString());
                    user.setPhone(fieldPhoneNumber.getText().toString());

                    // ambil avatar baru
                    user.setAvatar(resp.getJSONObject("data").getJSONObject("user").getString("avatar"));

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
            public Map<String, String> getHeaders() throws AuthFailureError {
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

            /**
             * Tambahkan gambar avatar ke request body
             * @return
             * @throws AuthFailureError
             */
            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();

                // cek apakah imageUri kosong atau tidak
                if(imageUri == null) return params;

                try {
                    // Ubah imageUri ke dalam bentuk bitmap
                    Bitmap image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                    // Tambahkan ke request body
                    params.put("avatar", new DataPart("avatar_image.jpg", byteArrayOutputStream.toByteArray(), "image/jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };

        requestQueue.add(postRequest);
        requestQueue.start();
    }
}
