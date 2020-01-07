package com.perjalanan.safarea.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.perjalanan.safarea.BuyerEditActivity;
import com.perjalanan.safarea.R;
import com.perjalanan.safarea.data.BuyerItem;

public class BuyerListAdapter extends RecyclerView.Adapter<BuyerListAdapter.BuyerItemViewHolder> implements Filterable {

    private ArrayList<BuyerItem> buyerList;
    private ArrayList<BuyerItem> buyerListFull;

    public static class BuyerItemViewHolder extends RecyclerView.ViewHolder {

        public TextView textBuyerName;
        public TextView textBuyerPhoneNumber;
        public ImageButton btnEdit;

        public BuyerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textBuyerName = itemView.findViewById(R.id.textBuyerName);
            textBuyerPhoneNumber = itemView.findViewById(R.id.textBuyerPhoneNumber);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public BuyerListAdapter(ArrayList<BuyerItem> bList) {
        buyerList = bList;
        buyerListFull = new ArrayList<>(bList);
    }

    public void good() {
        System.out.println("good");
    }

    @NonNull
    @Override
    public BuyerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.buyer_list_item, parent, false
        );

        return new BuyerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyerItemViewHolder holder, int position) {
        BuyerItem buyer = buyerList.get(holder.getAdapterPosition());
        holder.textBuyerName.setText(buyer.getName());
        holder.textBuyerPhoneNumber.setText(buyer.getPhone());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BuyerEditActivity.class);
                intent.putExtra("buyerId", buyer.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buyerList.size();
    }

    @Override
    public Filter getFilter() {
        return buyerFilter;
    }

    private Filter buyerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<BuyerItem> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(buyerListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(BuyerItem item : buyerListFull) {
                    if(item.getName().toLowerCase().contains(filterPattern)
                        || item.getPhone().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.values instanceof ArrayList) {
                buyerList.clear();
                buyerList.addAll((ArrayList<BuyerItem>)results.values);
                notifyDataSetChanged();
            }
        }
    };
}
