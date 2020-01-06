package com.perjalanan.safarea.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.perjalanan.safarea.R;
import com.perjalanan.safarea.data.CatalogItem;

public class DropshipperCatalogListAdapter extends RecyclerView.Adapter<DropshipperCatalogListAdapter.DropshipperCatalogItem> {

    private ArrayList<CatalogItem> catalogList;
    private DropshipperCatalogListAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Integer position);
    }

    public void setOnItemClickListener(DropshipperCatalogListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public DropshipperCatalogListAdapter(ArrayList<CatalogItem> list) {
        catalogList = list;
    }


    public DropshipperCatalogListAdapter.DropshipperCatalogItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.catalog_item, parent, false
        );

        DropshipperCatalogListAdapter.DropshipperCatalogItem sci = new DropshipperCatalogListAdapter.DropshipperCatalogItem(view, mListener);

        return sci;
    }

    @Override
    public void onBindViewHolder(@NonNull DropshipperCatalogListAdapter.DropshipperCatalogItem holder, int position) {
        CatalogItem item = catalogList.get(holder.getAdapterPosition());
        holder.thumbnailCatalog.setImageResource(Integer.parseInt(item.getThumbnail()));
        holder.titleCatalog.setText(item.getTitle());
        holder.priceCatalog.setText(item.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }

    public static class DropshipperCatalogItem extends RecyclerView.ViewHolder {

        ImageView thumbnailCatalog;
        TextView titleCatalog;
        TextView priceCatalog;

        public DropshipperCatalogItem(@NonNull View itemView, final DropshipperCatalogListAdapter.OnItemClickListener listener) {
            super(itemView);
            thumbnailCatalog = itemView.findViewById(R.id.thumbnailCatalog);
            titleCatalog = itemView.findViewById(R.id.titleCatalog);
            priceCatalog = itemView.findViewById(R.id.priceCatalog);

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
