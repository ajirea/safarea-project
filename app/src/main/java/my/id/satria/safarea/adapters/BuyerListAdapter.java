package my.id.satria.safarea.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.id.satria.safarea.R;
import my.id.satria.safarea.data.BuyerItem;

public class BuyerListAdapter extends RecyclerView.Adapter<BuyerListAdapter.BuyerItemViewHolder> {

    private ArrayList<BuyerItem> buyerList;

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
    }

    @NonNull
    @Override
    public BuyerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.buyer_list_item, parent, false
        );
        BuyerItemViewHolder bivh = new BuyerItemViewHolder(view);

        return bivh;
    }

    @Override
    public void onBindViewHolder(@NonNull BuyerItemViewHolder holder, int position) {
        BuyerItem buyer = buyerList.get(holder.getAdapterPosition());
        holder.textBuyerName.setText(buyer.getName());
        holder.textBuyerPhoneNumber.setText(buyer.getPhone());
    }

    @Override
    public int getItemCount() {
        return buyerList.size();
    }
}
