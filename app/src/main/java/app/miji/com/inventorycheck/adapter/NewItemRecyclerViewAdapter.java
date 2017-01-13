package app.miji.com.inventorycheck.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Item;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * Adapter for adding items in the list
 */

public class NewItemRecyclerViewAdapter extends RecyclerView.Adapter<NewItemRecyclerViewAdapter.ViewHolder> {

    private List<Item> itemList;
    private int defaultImageId;


    public NewItemRecyclerViewAdapter(List<Item> itemList, int defaultImageId) {
        this.itemList = itemList;
        this.defaultImageId = defaultImageId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = itemList.get(position);

        //TODO: add also the image

        //get values
        String name = itemList.get(position).getName();
        String qty = itemList.get(position).getQty() + " " + itemList.get(position).getUnit();
        String image = itemList.get(position).getImage();

        holder.mTxtName.setText(name);
        holder.mTxtQty.setText(qty);


        //image
        if (image != null) {
            //convert base64
            Bitmap bitmap = Utility.decodeBase64Image(image);
            //display image
            holder.mItemImageView.setImageBitmap(bitmap);
        } else {
            //no image, display default image
            holder.mItemImageView.setImageResource(defaultImageId);
        }
    }


    @Override
    public int getItemCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }


    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mItemImageView;
        final TextView mTxtName;
        final TextView mTxtQty;

        Item mItem;


        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemImageView = (ImageView) itemView.findViewById(R.id.imageView);
            mTxtQty = (TextView) itemView.findViewById(R.id.txt_qty);
            mTxtName = (TextView) itemView.findViewById(R.id.txt_name);

        }
    }
}
