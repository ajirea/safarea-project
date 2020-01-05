package com.perjalanan.safarea;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.perjalanan.safarea.helpers.ToolbarHelper;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class TransactionAddActivity extends AppCompatActivity {
    private ToolbarHelper toolbarHelper;
    private String dateTime;
    private EditText fieldDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_add);

        // atur custom toolbar
        toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.initToolbar();
        toolbarHelper.setToolbarTitle(getString(R.string.text_add_transaction));

        fieldDate = findViewById(R.id.fieldDate);
        fieldDate.setOnClickListener(l -> showDatePicker());

    }

    private void showDatePicker() {
        final Calendar cal = Calendar.getInstance();
        Integer mYear = cal.get(Calendar.YEAR);
        Integer mMonth = cal.get(Calendar.MONTH);
        Integer mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {

                    dateTime = dayOfMonth + "-" + (month+1) + "-" + year;

                    showTimePicker();

                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar cal = Calendar.getInstance();
        Integer mHour = cal.get(Calendar.HOUR_OF_DAY);
        Integer mMinute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    dateTime = dateTime + " " + hourOfDay + ":" + minute;
                    fieldDate.setText(dateTime);
                },
                mHour, mMinute, false);

        timePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
