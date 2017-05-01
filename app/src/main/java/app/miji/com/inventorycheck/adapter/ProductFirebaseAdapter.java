package app.miji.com.inventorycheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.Query;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Product;

/**
 * Created by isse on 01/05/2017.
 */

public class ProductFirebaseAdapter extends FirebaseRecyclerAdapter<ProductFirebaseAdapter.ViewHolder, Product> {

    private static final String LOG_TAG = ProductFirebaseAdapter.class.getSimpleName();

    public ProductFirebaseAdapter(Query query, Class<Product> itemClass) {
        super(query, itemClass);
    }

    @Override
    public ProductFirebaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductFirebaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductFirebaseAdapter.ViewHolder holder, int position) {
        holder.mProduct = getItem(position);

        //get data
        String mName = holder.mProduct.getName();
        String mImage = holder.mProduct.getImage();
        String mQuantity = String.valueOf(holder.mProduct.getQuantity());
        String mUnit = holder.mProduct.getUnit();


        //set data
        holder.mTxtName.setText(mName);
        holder.mTxtQuantity.setText(mQuantity);
        holder.mTxtUnit.setText(mUnit);

        //TODO change IMAGE
        holder.mImgItem.setImageResource(R.drawable.cake);
    }

    @Override
    protected void itemAdded(Product item, String key, int position) {
        Log.d(LOG_TAG, "Added a new item to the adapter.");
    }

    @Override
    protected void itemChanged(Product oldItem, Product newItem, String key, int position) {
        Log.d(LOG_TAG, "Changed an item.");
    }

    @Override
    protected void itemRemoved(Product item, String key, int position) {
        Log.d(LOG_TAG, "Removed an item from the adapter.");
    }

    @Override
    protected void itemMoved(Product item, String key, int oldPosition, int newPosition) {
        Log.d(LOG_TAG, "Moved an item.");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTxtName;
        final ImageView mImgItem;
        final TextView mTxtQuantity;
        final TextView mTxtUnit;

        Product mProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTxtName = (TextView) itemView.findViewById(R.id.txt_name);
            mImgItem = (ImageView) itemView.findViewById(R.id.img_item);
            mTxtQuantity = (TextView) itemView.findViewById(R.id.txt_qty);
            mTxtUnit = (TextView) itemView.findViewById(R.id.txt_unit);
        }
    }
}
