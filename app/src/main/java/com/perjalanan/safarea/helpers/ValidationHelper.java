package com.perjalanan.safarea.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.EditText;
import android.widget.Toast;

import com.perjalanan.safarea.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class ValidationHelper {

    public static Boolean isTextEmpty(@NonNull EditText editText, @Nullable String label, @Nullable Boolean showMessage,
                                      @Nullable Context context) {
        Boolean status = editText.getText().toString().trim().isEmpty();

        if (showMessage != null && status) {
            showToast(
                    context.getString(
                            R.string.validation_required, label
                    ),
                    context
            );
        }

        return status;
    }

    public static void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static Boolean hasInstalledPackage(Context context, String packageName) {
        try {
            PackageManager pkg = context.getPackageManager();
            pkg.getPackageGids(packageName);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
