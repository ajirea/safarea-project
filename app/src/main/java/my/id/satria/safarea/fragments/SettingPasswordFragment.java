package my.id.satria.safarea.fragments;


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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import my.id.satria.safarea.R;
import my.id.satria.safarea.SettingActivity;
import my.id.satria.safarea.helpers.ProgressDialogHelper;
import my.id.satria.safarea.repositories.RequestGlobalHeaders;
import my.id.satria.safarea.repositories.ServerAPI;

/**
 * Fragment untuk menampilkan pengaturan alamat dropshipper
 */
public class SettingPasswordFragment extends Fragment {

    private Context context;
    private View view;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting_password, container, false);
        context = container.getContext();

        // atur fragment agar memiliki akses ke toolbar
        setHasOptionsMenu(true);

        // atur toolbar title
        try {
            ((SettingActivity) getActivity()).setToolbarTitle(getString(R.string.text_change_password));
            ((SettingActivity) getActivity()).getToolbar().setOnMenuItemClickListener(i -> {

                if(i.getItemId() == R.id.save_item)
                    changePassword();

                return false;
            });
        } catch (NullPointerException e) {
            e.getStackTrace();
        }

        requestQueue = Volley.newRequestQueue(container.getContext());

        return view;
    }

    /**
     * Menambahkan menu item ke toolbar (menu item di sini adalah tombol centang)
     * @param menu actionbar
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void changePassword() {
        // membuat progress dialog
        ProgressDialogHelper pdh = new ProgressDialogHelper(context,
                getString(R.string.text_saving), getString(R.string.alert_saving_password));

        // membuat alert
        AlertDialog.Builder alert = new AlertDialog.Builder(context)
                .setTitle("Error!")
                .setPositiveButton(R.string.alert_yes_btn, (dialog, which) -> dialog.dismiss());

        // components
        EditText fieldOldPassword = view.findViewById(R.id.fieldOldPassword);
        EditText fieldNewPassword = view.findViewById(R.id.fieldNewPassword);
        EditText fieldNewPasswordConfirm = view.findViewById(R.id.fieldNewPasswordConfirm);

        if(fieldNewPassword.length() < 8 || fieldOldPassword.length() < 8) {
            Toast.makeText(getContext(), "Kata Sandi minimal 8 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        pdh.show();
        String url = ServerAPI.CHANGE_PASSWORD_ACCOUNT;
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            pdh.dismiss();
            try {
                JSONObject resp = new JSONObject(response);
                String message = resp.getJSONObject("data").getString("message");

                if(resp.getBoolean("status")) {
                    alert.setTitle("Sukses!");
                    fieldOldPassword.setText("");
                    fieldNewPassword.setText("");
                    fieldNewPasswordConfirm.setText("");
                }

                alert.setMessage(message).show();

            } catch (JSONException e) {
                alert.setMessage(e.getMessage()).show();
                e.printStackTrace();
            }
        }, error -> pdh.dismiss()) {
            @Override
            public Map<String, String> getHeaders() {
                return RequestGlobalHeaders.get(context);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("old_password", fieldOldPassword.getText().toString());
                params.put("password", fieldNewPassword.getText().toString());
                params.put("password_confirmation", fieldNewPasswordConfirm.getText().toString());

                return params;
            }
        };
        requestQueue.add(request);
    }
}
