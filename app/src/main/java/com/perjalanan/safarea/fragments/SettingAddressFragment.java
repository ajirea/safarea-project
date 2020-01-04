package com.perjalanan.safarea.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.perjalanan.safarea.R;
import com.perjalanan.safarea.SettingActivity;
import com.perjalanan.safarea.data.User;
import com.perjalanan.safarea.repositories.UserLocalStore;

/**
 * Fragment untuk menampilkan pengaturan alamat dropshipper
 */
public class SettingAddressFragment extends Fragment {

    private User user;
    private UserLocalStore userLocalStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_address, container, false);

        // atur fragment agar memiliki akses ke toolbar
        setHasOptionsMenu(true);

        // load data
        userLocalStore = new UserLocalStore(container.getContext());
        user = userLocalStore.getLoggedInUser();

        EditText fieldAddress = view.findViewById(R.id.fieldAddress);
        EditText fieldVillage = view.findViewById(R.id.fieldVillage);
        EditText fieldDistrict = view.findViewById(R.id.fieldDistrict);
        EditText fieldCity = view.findViewById(R.id.fieldCity);
        EditText fieldProvince = view.findViewById(R.id.fieldProvince);
        EditText fieldPos = view.findViewById(R.id.fieldPos);

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
        } catch (NullPointerException e) {}

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

}
