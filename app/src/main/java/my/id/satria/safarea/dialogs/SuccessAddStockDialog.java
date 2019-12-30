package my.id.satria.safarea.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import my.id.satria.safarea.R;

public class SuccessAddStockDialog extends BottomSheetDialogFragment {

    private String message;
    private SuccessAddStockDialogListener mListener;

    public SuccessAddStockDialog(String message) {
        this.message = message;
    }

    public interface SuccessAddStockDialogListener {
        void onButtonClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_added_stock, container, false);

        TextView textMessage = view.findViewById(R.id.textMessage);
        Button btnGoToStock = view.findViewById(R.id.btnGoToStock);
        textMessage.setText(message);

        btnGoToStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked();
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (SuccessAddStockDialogListener) context;
        } catch (ClassCastException e) {}
    }
}
