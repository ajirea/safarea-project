package com.perjalanan.safarea.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.perjalanan.safarea.R;
import com.perjalanan.safarea.data.CatalogItem;

public class SupplierCatalogListAdapter extends RecyclerView.Adapter<SupplierCatalogListAdapter.SupplierCatalogItem> {

    private ArrayList<CatalogItem> catalogList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Integer position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SupplierCatalogListAdapter(ArrayList<CatalogItem> list) {
        catalogList = list;
    }

    @NonNull
    @Override
    public SupplierCatalogItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.catalog_item, parent, false
        );

        SupplierCatalogItem sci = new SupplierCatalogItem(view, mListener);

        return sci;
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierCatalogItem holder, int position) {
        CatalogItem item = catalogList.get(holder.getAdapterPosition());

        Glide.with(holder.context).load(item.getThumbnail())
                .centerCrop()
                .into(holder.thumbnailCatalog);

        holder.titleCatalog.setText(item.getTitle());
        holder.priceCatalog.setText(item.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }

    public static class SupplierCatalogItem extends RecyclerView.ViewHolder {

        ImageView thumbnailCatalog;
        TextView titleCatalog;
        TextView priceCatalog;
        Context context;

        public SupplierCatalogItem(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            thumbnailCatalog = itemView.findViewById(R.id.thumbnailCatalog);
            titleCatalog = itemView.findViewById(R.id.titleCatalog);
            priceCatalog = itemView.findViewById(R.id.priceCatalog);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
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
}
