package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.miji.com.inventorycheck.ItemListActivity;
import app.miji.com.inventorycheck.R;

/**
 * For Sales list item
 */

public class SalesRecyclerViewAdapter extends RecyclerView.Adapter<SalesRecyclerViewAdapter.ViewHolder> {
    private Context mContext;

    public SalesRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public SalesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
        return new SalesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //TODO change dummy text
        String mDate = "02/14/2017";
        String mLocation = "Orchard";
        String mCustomer = "Masahiro Miji";
        String mRefNo = "0000012";
        String mItems = "Items: strawberry, Apple, cheese, yogurt, pie, brush, tissue, ballpen";

        holder.txtDate.setText(mDate);
        holder.txtLocation.setText(mLocation);
        holder.txtRefNo.setText(mRefNo);
        holder.txtCustomer.setText(mCustomer);
        holder.txtItems.setText(mItems);

        //show all items when clicked
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ItemListActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        //TODO change item count
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView txtDate;
        final TextView txtLocation;
        final TextView txtRefNo;
        final TextView txtCustomer;
        final TextView txtItems;

        //TODO make Sales model class

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            this.txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            this.txtLocation = (TextView) itemView.findViewById(R.id.txt_location);
            this.txtRefNo = (TextView) itemView.findViewById(R.id.txt_ref_no);
            this.txtCustomer = (TextView) itemView.findViewById(R.id.txt_customer);
            this.txtItems = (TextView) itemView.findViewById(R.id.txt_items);
        }
    }
}
