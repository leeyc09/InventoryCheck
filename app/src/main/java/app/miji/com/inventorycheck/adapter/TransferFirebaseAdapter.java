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
import app.miji.com.inventorycheck.model.Transfer;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * Created by isse on 02/05/2017.
 */

public class TransferFirebaseAdapter extends FirebaseRecyclerAdapter<TransferFirebaseAdapter.ViewHolder, Transfer> {

    private static final String LOG_TAG = TransferFirebaseAdapter.class.getSimpleName();

    public TransferFirebaseAdapter(Query query, Class<Transfer> itemClass) {
        super(query, itemClass);
    }

    @Override
    public TransferFirebaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer, parent, false);
        Context context = parent.getContext();

        return new TransferFirebaseAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final TransferFirebaseAdapter.ViewHolder holder, int position) {
        holder.mTransfer = getItem(position);
        final Context mContext = holder.getContext();
        String flagActivity = mContext.getClass().getSimpleName();

        //get data
        final String mDate = holder.mTransfer.getDate();
        final String mTime = holder.mTransfer.getTime();
        final String mFrom = holder.mTransfer.getFromLocation();
        final String mTo = holder.mTransfer.getToLocation();
        final String mTransferId = holder.mTransfer.getTransferID();

        final String details = Utility.getTransferDetails(mContext, mDate, mTime, mTransferId, mFrom, mTo);


        //set text
        holder.mTxtDate.setText(mContext.getString(R.string.mdtp_date) + ": " + mDate);
        holder.mNumber.setText(mContext.getString(R.string.trans_id) + ": " + mTransferId);


        //determine if request is from stock in or stock out activity
        final String to = mContext.getString(R.string.to) + ": " + mTo;
        final String from = mContext.getString(R.string.from) + ": " + mFrom;
        switch (flagActivity) {
            case "StockInActivity":
                //from StockInActivity
                holder.mItem1.setText(from);
                holder.mItem2.setText(to);
                break;
            case "StockOutActivity":
                //from StockOutActivity
                holder.mItem1.setText(to);
                holder.mItem2.setText(from);
                break;
        }

        //show all items when clicked
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ItemListActivity.class);
                //put extras
                intent.putExtra(ItemListActivity.DETAILS, details);//whole delivery details
                intent.putExtra(ItemListFragment.TRANSFER, holder.mTransfer);//transfer object
                intent.putExtra(ItemListActivity.BASE64IMAGE, ""); //todo replace null string for image

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void itemAdded(Transfer item, String key, int position) {
        Log.d(LOG_TAG, "Added a new item to the adapter.");
    }

    @Override
    protected void itemChanged(Transfer oldItem, Transfer newItem, String key, int position) {
        Log.d(LOG_TAG, "Changed an item.");
    }

    @Override
    protected void itemRemoved(Transfer item, String key, int position) {
        Log.d(LOG_TAG, "Removed an item from the adapter.");
    }

    @Override
    protected void itemMoved(Transfer item, String key, int oldPosition, int newPosition) {
        Log.d(LOG_TAG, "Moved an item.");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final Context mContext;
        final TextView mTxtDate;
        final TextView mItem1;
        final TextView mItem2;
        final TextView mNumber;

        Transfer mTransfer;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mView = itemView;
            mContext = context;
            mTxtDate = (TextView) itemView.findViewById(R.id.txt_date);
            mItem1 = (TextView) itemView.findViewById(R.id.txt_text_one);
            mItem2 = (TextView) itemView.findViewById(R.id.txt_text_two);
            mNumber = (TextView) itemView.findViewById(R.id.txt_number);
        }

        public Context getContext() {
            return mContext;
        }
    }
}
