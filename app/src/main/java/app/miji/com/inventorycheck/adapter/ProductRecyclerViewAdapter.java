package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Product;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Product> list;


    public ProductRecyclerViewAdapter(Context mContext, List<Product> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_take, parent, false);
        return new ProductRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mProduct = list.get(position);

        //TODO change IMAGE
        holder.mImgItem.setImageResource(R.drawable.cake);
        holder.mTxtItem.setText(list.get(position).getName());

        //TODO quantity and unit depends on database count
        holder.mTxtQty.setText("30");
        holder.mTxtUnit.setText("pcs");
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final ImageView mImgItem;
        final TextView mTxtItem;
        final TextView mTxtQty;
        final TextView mTxtUnit;

        Product mProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImgItem = (ImageView) mView.findViewById(R.id.img_item);
            mTxtItem = (TextView) mView.findViewById(R.id.txt_name);
            mTxtQty = (TextView) mView.findViewById(R.id.txt_qty);
            mTxtUnit = (TextView) mView.findViewById(R.id.txt_unit);
        }

    }

}
