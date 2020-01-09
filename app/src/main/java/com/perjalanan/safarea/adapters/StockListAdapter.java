package com.perjalanan.safarea.adapters;

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

import com.bumptech.glide.Glide;
import com.perjalanan.safarea.R;
import com.perjalanan.safarea.StockActivity;
import com.perjalanan.safarea.data.StockItem;
import com.perjalanan.safarea.helpers.FormatHelper;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockItemViewHolder> {

    private ArrayList<StockItem> stockList;
    private Boolean openedDialog = false;

    public static class StockItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailProduct;
        public ImageButton btnRemove;
        public Button btnAccept;
        public TextView titleProduct, textProfit, textStock, textStatus;
        public AlertDialog.Builder alert;

        public StockItemViewHolder(@NonNull View itemView) {
            super(itemView);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            titleProduct = itemView.findViewById(R.id.titleProduct);
            textProfit = itemView.findViewById(R.id.textProfit);
            textStock = itemView.findViewById(R.id.textStock);
            textStatus = itemView.findViewById(R.id.textStatus);
            thumbnailProduct = itemView.findViewById(R.id.thumbnailProduct);
            alert = new AlertDialog.Builder(itemView.getContext()).setTitle(
                    "Yakin " + itemView.getContext().getString(R.string.text_accept_goods) + " ?"
            ).setPositiveButton(R.string.alert_yes_btn, null)
            .setNegativeButton(R.string.alert_no_btn, null);
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

        return new StockItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockItemViewHolder holder, int position) {
        StockItem item = stockList.get(holder.getAdapterPosition());
        Context context = holder.itemView.getContext();

        Glide.with(holder.thumbnailProduct).load(item.getThumbnail())
                .centerCrop().into(holder.thumbnailProduct);
        holder.titleProduct.setText(item.getName());
        holder.textStock.setText(
                context.getString(R.string.text_stock_available, item.getQty())
        );
        holder.textProfit.setText(
                context.getString(R.string.text_profit_price, FormatHelper
                        .priceFormat(item.getProfitPrice()))
        );

        if(item.getStatus().equals("active"))
            holder.textStatus.setVisibility(View.GONE);
        else
            holder.textStatus.setText(item.getStatusDescription());

        if(item.getStatus().equals("sending"))
            holder.btnAccept.setVisibility(View.VISIBLE);
        else
            holder.btnAccept.setVisibility(View.GONE);

        holder.alert.setPositiveButton(R.string.alert_yes_btn, (dialog, which) -> {
            ((StockActivity) context).confirmStock(item.getId());
        });

        holder.btnAccept.setOnClickListener(v -> holder.alert.show());
        holder.btnRemove.setOnClickListener(v -> {
            if(!openedDialog)
                showDeleteDialog(context, position);
        });
    }

    /**
     * Method untuk menampilkan alert dialog dengan layout custom
     * @param context Context
     * @param position Integer
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
