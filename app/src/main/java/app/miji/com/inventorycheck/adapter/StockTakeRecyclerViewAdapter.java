package app.miji.com.inventorycheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Item;

public class StockTakeRecyclerViewAdapter extends RecyclerView.Adapter<StockTakeRecyclerViewAdapter.ViewHolder> {


    public StockTakeRecyclerViewAdapter() {
    }

    @Override
    public StockTakeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_take, parent, false);
        return new StockTakeRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockTakeRecyclerViewAdapter.ViewHolder holder, int position) {


        //TODO change dummy items
        holder.imgItem.setImageResource(R.drawable.cake);
        holder.txtItem.setText("Shortcake");
        holder.txtQty.setText("30");
        holder.txtUnit.setText("pcs");

    }

    @Override
    public int getItemCount() {
        //TODO change item count based on list
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView imgCancel;
        final ImageView imgItem;
        final TextView txtItem;
        final TextView txtQty;
        final TextView txtUnit;

        Item mItem;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imgCancel = (ImageView) mView.findViewById(R.id.img_delete);
            imgItem = (ImageView) mView.findViewById(R.id.img_item);
            txtItem = (TextView) mView.findViewById(R.id.txt_name);
            txtQty = (TextView) mView.findViewById(R.id.txt_qty);
            txtUnit = (TextView) mView.findViewById(R.id.txt_unit);


        }
    }
}
