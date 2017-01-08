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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        setupItemSpinner(holder.spinnerName);
        setupUnitSpinner(holder.spinnerUnit);
        setupImagePicker(holder.mItemImageView);
    }


    @Override
    public int getItemCount() {
        return mItemQty;
    }


    private void setupItemSpinner(MaterialBetterSpinner materialSpinner) {
        //dummy items to show in spinner
        final String[] ITEMS = new String[]{"Cupcake", "Brownies", "Tiramisu", "Cake", "Burger"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line, ITEMS);
        materialSpinner.setAdapter(adapter);
    }

    private void setupUnitSpinner(MaterialBetterSpinner materialSpinner) {
        final String[] UNITS = new String[]{"pcs", "kg", "m"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line, UNITS);
        materialSpinner.setAdapter(adapter);
    }


    private void setupImagePicker(final ImageView mItemImageView) {
        mItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start TedBottomPicker
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(mContext)
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                //Do something with selected uri
                                InputStream inputStream;
                                try {
                                    inputStream = mContext.getContentResolver().openInputStream(uri);
                                    //the "image" received here is the image itself
                                    Bitmap b = BitmapFactory.decodeStream(inputStream);
                                    mItemImageView.setImageBitmap(b);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .create();

                tedBottomPicker.show(fragmentManager);
            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mItemImageView;

        final TextInputLayout mTxtInQty;
        final TextView mTxtQty;

        final MaterialBetterSpinner spinnerName;
        final MaterialBetterSpinner spinnerUnit;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemImageView = (ImageView) itemView.findViewById(R.id.img_receipt);
            mTxtInQty = (TextInputLayout) itemView.findViewById(R.id.input_name);
            mTxtQty = (TextView) itemView.findViewById(R.id.txt_qty);
            spinnerName = (MaterialBetterSpinner) itemView.findViewById(R.id.spinner_name);
            spinnerUnit = (MaterialBetterSpinner) itemView.findViewById(R.id.spinner_unit);

        }
    }
}
