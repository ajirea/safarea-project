package com.perjalanan.safarea.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.perjalanan.safarea.R;
import com.perjalanan.safarea.data.TransactionItem;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionItemViewHolder>{

    private Context context;
    private ArrayList<TransactionItem> transactionList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Integer position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TransactionListAdapter(Context context, ArrayList<TransactionItem> tList) {
        this.context = context;
        transactionList = tList;
    }

    public static class TransactionItemViewHolder extends RecyclerView.ViewHolder {

        public TextView pemesan, transactionNumber, product, qty, tglDipesan, harga, totHarga;
        public ImageView images;
        public ImageButton btnDetails;

        public TransactionItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            transactionNumber = itemView.findViewById(R.id.lblTransactionNumber);
            pemesan = itemView.findViewById(R.id.lblPemesan);
            product = itemView.findViewById(R.id.txtProduct);
            qty = itemView.findViewById(R.id.txtQty);
            tglDipesan = itemView.findViewById(R.id.txtDate);
            harga = itemView.findViewById(R.id.txtPrice);
            totHarga = itemView.findViewById(R.id.txtTotPrice);
            images = itemView.findViewById(R.id.imgProd);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        Integer position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public TransactionListAdapter.TransactionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.transaction_item_list, parent, false
        );
        TransactionItemViewHolder tivh = new TransactionItemViewHolder(view, mListener);


        return tivh;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionListAdapter.TransactionItemViewHolder holder, int position) {
        TransactionItem transaction = transactionList.get(holder.getAdapterPosition());
        holder.pemesan.setText(
                context.getString(R.string.text_buyer_name, transaction.getName())
        );
        //holder.images.setImageDrawable(context.getResources().getDrawable(transaction.getImage()));
        Glide.with(context).load(transaction.getImage())
                .centerCrop()
                .into(holder.images);
        holder.product.setText(transaction.getProduct());
        holder.qty.setText(String.valueOf(transaction.getQty()));
        holder.tglDipesan.setText(transaction.getOrderedAt());
        holder.harga.setText(String.valueOf(transaction.getPrice()));
        holder.totHarga.setText(String.valueOf(transaction.getTotalPrice()));
    }


    @Override
    public int getItemCount() {
        return transactionList.size();
    }

}
