package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.InputStream;

import app.miji.com.inventorycheck.R;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * Created by isse on 08/01/2017.
 */

public class NewItemRecyclerViewAdapter extends RecyclerView.Adapter<NewItemRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private int mItemQty;
    private FragmentManager fragmentManager;


    public NewItemRecyclerViewAdapter(Context mContext, int recyclerviewItemQty, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.mItemQty = recyclerviewItemQty;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mTxtName.setText("Dog Food");
        holder.mTxtQty.setText("10 kgs");


    }


    @Override
    public int getItemCount() {
        return mItemQty;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mItemImageView;

        final TextView mTxtName;
        final TextView mTxtQty;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemImageView = (ImageView) itemView.findViewById(R.id.img_receipt);
            mTxtQty = (TextView) itemView.findViewById(R.id.txt_qty);
            mTxtName = (TextView) itemView.findViewById(R.id.txt_name);

        }
    }
}
