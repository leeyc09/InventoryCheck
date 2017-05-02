package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.Query;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.activity.ItemListActivity;
import app.miji.com.inventorycheck.fragment.ItemListFragment;
import app.miji.com.inventorycheck.model.Sales;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * Created by isse on 02/05/2017.
 */

public class SalesFirebaseAdapter extends FirebaseRecyclerAdapter<SalesFirebaseAdapter.ViewHolder, Sales> {
    private static final String LOG_TAG = SalesFirebaseAdapter.class.getSimpleName();

    public SalesFirebaseAdapter(Query query, Class<Sales> itemClass) {
        super(query, itemClass);
    }

    @Override
    public SalesFirebaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
        Context context = parent.getContext();
        return new SalesFirebaseAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final SalesFirebaseAdapter.ViewHolder holder, int position) {
        holder.mSales = getItem(position);
        final Context mContext = holder.getContext();

        //get data
        String mDate = holder.mSales.getDate();
        String mTime = holder.mSales.getTime();
        String mLocation = holder.mSales.getLocation();
        String mCustomer = holder.mSales.getCustomer();
        String mRefNo = holder.mSales.getReferenceNo();
        String mImage = holder.mSales.getImage();
        //String mItems = holder.mSales.getItems();

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
    protected void itemAdded(Sales item, String key, int position) {
        Log.d(LOG_TAG, "Added a new item to the adapter.");
    }

    @Override
    protected void itemChanged(Sales oldItem, Sales newItem, String key, int position) {
        Log.d(LOG_TAG, "Changed an item.");
    }

    @Override
    protected void itemRemoved(Sales item, String key, int position) {
        Log.d(LOG_TAG, "Removed an item from the adapter.");
    }

    @Override
    protected void itemMoved(Sales item, String key, int oldPosition, int newPosition) {
        Log.d(LOG_TAG, "Moved an item.");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final Context mContext;
        final TextView mTxtDate;
        final TextView mTxtLocation;
        final TextView mTxtRefNo;
        final TextView mTxtCustomer;

        Sales mSales;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.mView = itemView;
            this.mContext = context;
            this.mTxtDate = (TextView) itemView.findViewById(R.id.txt_date);
            this.mTxtLocation = (TextView) itemView.findViewById(R.id.txt_location);
            this.mTxtRefNo = (TextView) itemView.findViewById(R.id.txt_ref_no);
            this.mTxtCustomer = (TextView) itemView.findViewById(R.id.txt_customer);
        }

        public Context getContext() {
            return mContext;
        }
    }
}
