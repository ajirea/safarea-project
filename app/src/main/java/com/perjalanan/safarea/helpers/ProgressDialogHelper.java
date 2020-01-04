package com.perjalanan.safarea.helpers;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Kelas untuk membuat progress dialog
 */
public class ProgressDialogHelper {

    private ProgressDialog progressDialog;

    public ProgressDialogHelper(Context context, String title, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
    }

    public ProgressDialogHelper(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setTitle(String title) {
        progressDialog.setTitle(title);
    }

    public void setMessage(String message) {
        progressDialog.setMessage(message);
    }

    public void show() { progressDialog.show(); }

    public void dismiss() { progressDialog.dismiss(); }
}
