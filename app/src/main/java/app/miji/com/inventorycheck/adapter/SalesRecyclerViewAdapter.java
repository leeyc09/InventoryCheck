package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.miji.com.inventorycheck.activity.ItemListActivity;
import app.miji.com.inventorycheck.fragment.ItemListFragment;
import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Sales;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * For Sales list item
 */

public class SalesRecyclerViewAdapter extends RecyclerView.Adapter<SalesRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Sales> list;

    public SalesRecyclerViewAdapter(Context mContext, List<Sales> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public SalesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
        return new SalesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mSales = list.get(position);

        //get data
        String mDate = list.get(position).getDate();
        String mTime = list.get(position).getTime();
        String mLocation = list.get(position).getLocation();
        String mCustomer = list.get(position).getCustomer();
        String mRefNo = list.get(position).getReferenceNo();
        String mImage = list.get(position).getImage();
        //String mItems = list.get(position).getItems();

        final String details = Utility.getSalesDetails(mContext, mDate, mTime, mCustomer, mRefNo, mLocation);

        //set data
        holder.mTxtDate.setText(mContext.getString(R.string.mdtp_date) + ": " + mDate);
        holder.mTxtLocation.setText(mContext.getString(R.string.location) + ": " + mLocation);
        holder.mTxtRefNo.setText(mContext.getString(R.string.reference) + ": " + mRefNo);
        holder.mTxtCustomer.setText(mContext.getString(R.string.customer) + ": " + mCustomer);

        //show all items when clicked
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ItemListActivity.class);
                //put extras
                intent.putExtra(ItemListActivity.DETAILS, details);//whole sales details
                intent.putExtra(ItemListFragment.SALES, holder.mSales);//sales object
                intent.putExtra(ItemListActivity.BASE64IMAGE, ""); //todo replace null string for image
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTxtDate;
        final TextView mTxtLocation;
        final TextView mTxtRefNo;
        final TextView mTxtCustomer;

        Sales mSales;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            this.mTxtDate = (TextView) itemView.findViewById(R.id.txt_date);
            this.mTxtLocation = (TextView) itemView.findViewById(R.id.txt_location);
            this.mTxtRefNo = (TextView) itemView.findViewById(R.id.txt_ref_no);
            this.mTxtCustomer = (TextView) itemView.findViewById(R.id.txt_customer);
        }
    }
}
