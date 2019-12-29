package my.id.satria.safarea.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.Navigation;
import my.id.satria.safarea.R;
import my.id.satria.safarea.SettingActivity;

/**
 * Fragment pengaturan akun
 * di fragment ini akan ditampilkan pengaturan akun general
 * Seperti nama, username, email, no telp, dan avatar
 */
public class SettingAccountFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_account, container, false);

        // atur fragment agar memiliki akses ke toolbar
        setHasOptionsMenu(true);

        Button btnChangeAddress = view.findViewById(R.id.btnChangeAddress);
        Button btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnChangeAddress.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_settingAccountFragment_to_settingAddressFragment)
        );

        btnChangePassword.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_settingAccountFragment_to_settingPasswordFragment)
        );

        return view;
    }

    /**
     * Menambahkan menu item ke toolbar (menu item di sini adalah tombol centang)
     * @param menu
     * @param inflater
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
        } catch (NullPointerException e) {}
        super.onResume();
    }
}
