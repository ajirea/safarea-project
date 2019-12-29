package my.id.satria.safarea.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import my.id.satria.safarea.R;
import my.id.satria.safarea.SettingActivity;

/**
 * Fragment untuk menampilkan pengaturan alamat dropshipper
 */
public class SettingPasswordFragment extends Fragment {


    public SettingPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_password, container, false);

        // atur fragment agar memiliki akses ke toolbar
        setHasOptionsMenu(true);

        // atur toolbar title
        try {
            ((SettingActivity) getActivity()).setToolbarTitle(getString(R.string.text_change_password));
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
