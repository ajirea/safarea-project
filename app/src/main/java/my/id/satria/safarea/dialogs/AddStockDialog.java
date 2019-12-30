package my.id.satria.safarea.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import my.id.satria.safarea.R;

public class AddStockDialog extends BottomSheetDialogFragment {

    private AddStockDialogListener mListener;

    public interface AddStockDialogListener {
        void onButtonClicked(Integer stock, Integer profit, String type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_stock, container, false);

        Button btnTake = view.findViewById(R.id.btnTake);
        Button btnSend = view.findViewById(R.id.btnSend);


        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAction(view, "take");
                dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAction(view, "send");
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (AddStockDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement AddStockDialogListener");
        }
    }

    private void onClickAction(View view, String type) {
        TextView fieldQty = view.findViewById(R.id.fieldQty);
        TextView fieldProfit = view.findViewById(R.id.fieldProfit);
        try {
            mListener.onButtonClicked(
                    Integer.parseInt(fieldQty.getText().toString()),
                    Integer.parseInt(fieldProfit.getText().toString()),
                    type
            );
        } catch (NumberFormatException e) {
            Toast.makeText(view.getContext(), "Silahkan isi terlebih dahulu",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
