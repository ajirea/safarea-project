package my.id.satria.safarea.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.id.satria.safarea.R;
import my.id.satria.safarea.StockActivity;
import my.id.satria.safarea.data.StockItem;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockItemViewHolder> {

    private ArrayList<StockItem> stockList;
    private Boolean openedDialog = false;

    public static class StockItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailProduct;
        public ImageButton btnRemove;
        public TextView titleProduct, textProfit, textStock;

        public StockItemViewHolder(@NonNull View itemView) {
            super(itemView);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            titleProduct = itemView.findViewById(R.id.titleProduct);
            textProfit = itemView.findViewById(R.id.textProfit);
            textStock = itemView.findViewById(R.id.textStock);
            thumbnailProduct = itemView.findViewById(R.id.thumbnailProduct);
        }
    }

    public StockListAdapter(ArrayList<StockItem> mList) {
        stockList = mList;
    }

    @NonNull
    @Override
    public StockItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
          R.layout.stock_list_item, parent, false
        );

        StockItemViewHolder sivh = new StockItemViewHolder(view);

        return sivh;
    }

    @Override
    public void onBindViewHolder(@NonNull StockItemViewHolder holder, int position) {
        StockItem item = stockList.get(holder.getAdapterPosition());
        Context context = holder.itemView.getContext();

        holder.thumbnailProduct.setImageResource(item.getThumbnail());
        holder.titleProduct.setText(item.getName());
        holder.textStock.setText(
                context.getString(R.string.text_stock_available, item.getQty())
        );
        holder.textProfit.setText(
                context.getString(R.string.text_profit_price, item.getProfitPrice().toString())
        );

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!openedDialog)
                    showDeleteDialog(context, position);
            }
        });
    }

    /**
     * Method untuk menampilkan alert dialog dengan layout custom
     * @param context
     * @param position
     */
    private void showDeleteDialog(Context context, Integer position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View layoutView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null, false);

        // ambil komponen yang ada di dalam layout dialog_confirmation.xml
        Button btnAccept = layoutView.findViewById(R.id.btnAddStock);
        Button btnDecline = layoutView.findViewById(R.id.btnDecline);
        TextView textTitle = layoutView.findViewById(R.id.textTitle);
        TextView textDesc = layoutView.findViewById(R.id.textDescription);

        // atur teks
        textTitle.setText(context.getString(R.string.text_alert_stock_delete_title));
        textDesc.setText(context.getString(R.string.text_alert_stock_delete_desc));

        // atur view ke dialog dan tampilkan
        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        btnAccept.setOnClickListener(l -> {
            ((StockActivity) context).removeItem(position);
            alertDialog.dismiss();
            openedDialog = false;
        });
        btnDecline.setOnClickListener(l -> {
            alertDialog.dismiss();
            openedDialog = false;
        });
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
