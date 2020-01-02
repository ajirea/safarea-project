package my.id.satria.safarea;

import androidx.appcompat.app.AppCompatActivity;
import my.id.satria.safarea.data.User;
import my.id.satria.safarea.helpers.ProgressDialogHelper;
import my.id.satria.safarea.repositories.ServerAPI;
import my.id.satria.safarea.repositories.UserLocalStore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialogHelper progressDialog;
    private EditText fieldUsername, fieldPassword;
    private TextView alertText;
    private UserLocalStore userLocalStore;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(this);
        if (userLocalStore.isUserLoggedIn()) startMainActivity();

        System.out.println(userLocalStore.getLoggedInUser().getAvatar());

        setContentView(R.layout.activity_login);

        fieldUsername = findViewById(R.id.fieldUsernameLogin);
        fieldPassword = findViewById(R.id.fieldPasswordLogin);
        alertText = findViewById(R.id.alertText);
        Button btnSignIn = findViewById(R.id.btnSignIn);

        // instansiasi progress dialig
        progressDialog = new ProgressDialogHelper(this,
                getString(R.string.alert_signing_in_title),
                getString(R.string.alert_please_wait_message));

        // instansiasi request menggunakan volley
        requestQueue = Volley.newRequestQueue(this);

        // handling event
        btnSignIn.setOnClickListener(this::signInOnClick);
    }

    private void signInOnClick(View view) {
        if (fieldUsername.getText().toString().trim().equals("") ||
                fieldPassword.getText().toString().trim().equals("")) {
            alertText.setText(getString(R.string.validation_required_uname_passwd));
        } else {
            authenticate();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userLocalStore.isUserLoggedIn()) startMainActivity();
    }

    /**
     * Fungsi untuk menjalankan otentikasi pengguna melalui API
     * dengan input username dan password
     */
    private void authenticate() {
        progressDialog.show();
        String loginUrl = ServerAPI.AUTH_LOGIN;
        StringRequest request = new StringRequest(Request.Method.POST, loginUrl, response -> {
            // parse object
            try {
                JSONObject resp = new JSONObject(response);

                if (!resp.getBoolean("status")) {
                    alertText.setText(resp.getJSONObject("data").getString("message"));
                    userLocalStore.clearUserData();
                    userLocalStore.setUserLoggedIn(false);
                } else {

                    JSONObject respData = resp.getJSONObject("data");

                    User user = new User(
                            Integer.parseInt(respData.getString("id")),
                            respData.getString("username"),
                            respData.getString("email"),
                            respData.getString("name"),
                            respData.getString("password"),
                            respData.getString("token"),
                            respData.getString("store_name"),
                            respData.getString("phone"),
                            respData.getString("avatar")
                    );

                    user.setAddress(respData.getString("address"));
                    user.setVillage(respData.getString("village"));
                    user.setDistrict(respData.getString("district"));
                    user.setCity(respData.getString("city"));
                    user.setProvince(respData.getString("province"));
                    user.setPostalCode(respData.getString("postal_code"));
                    user.setAdmin(
                            respData.getString("is_admin").equals("1")
                    );
                    user.setCreatedAt(respData.getString("created_at"));

                    userLocalStore.storeUserData(user);
                    userLocalStore.setUserLoggedIn(true);

                    startMainActivity();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();

        }, error -> {
            alertText.setText(getString(R.string.alert_failed_connect_server));
            progressDialog.dismiss();
        }) {
            /**
             * Data yang akan di kirim melalui request
             * @return Map
             * @throws AuthFailureError
             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", fieldUsername.getText().toString());
                params.put("password", fieldPassword.getText().toString());
                return params;
            }
        };

        requestQueue.add(request);
    }
}
