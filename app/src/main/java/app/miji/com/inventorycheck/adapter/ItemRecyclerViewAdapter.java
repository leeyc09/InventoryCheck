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
import app.miji.com.inventorycheck.model.Item;

/**
 * Created by isse on 24/01/2017.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Item> list;

    public ItemRecyclerViewAdapter(Context mContext, List<Item> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_itemdetail, parent, false);
        return new ItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItem = list.get(position);

        //get data
        String image = list.get(position).getImage();
        String name = list.get(position).getName();
        String quantity = list.get(position).getQty();
        String unit = list.get(position).getUnit();

        //set data
        //TODO set image
        holder.mImageView.setImageResource(R.drawable.cake);
        holder.mTxtName.setText(name);
        holder.mTxtQty.setText(quantity + " " + unit);

    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImageView;
        final TextView mTxtName;
        final TextView mTxtQty;

        Item mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImageView = (ImageView) mView.findViewById(R.id.imageView);
            mTxtName = (TextView) mView.findViewById(R.id.txt_name);
            mTxtQty = (TextView) mView.findViewById(R.id.txt_qty);

        }
    }
}
