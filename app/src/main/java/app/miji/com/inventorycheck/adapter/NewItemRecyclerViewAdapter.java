package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import app.miji.com.inventorycheck.R;

/**
 * Created by isse on 08/01/2017.
 */

public class NewItemRecyclerViewAdapter extends RecyclerView.Adapter<NewItemRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private int mItemQty;


    public NewItemRecyclerViewAdapter(Context mContext, int recyclerviewItemQty) {
        this.mContext = mContext;
        this.mItemQty = recyclerviewItemQty;
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
