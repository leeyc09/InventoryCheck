package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.activity.ItemListActivity;
import app.miji.com.inventorycheck.fragment.ItemListFragment;
import app.miji.com.inventorycheck.model.Delivery;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * Created by isse on 01/05/2017.
 */

public class DeliveryFirebaseAdapter extends FirebaseRecyclerAdapter<DeliveryFirebaseAdapter.ViewHolder, Delivery> {
    private static final String LOG_TAG = DeliveryFirebaseAdapter.class.getSimpleName();

    public DeliveryFirebaseAdapter(Query query, Class<Delivery> itemClass) {
        super(query, itemClass);
    }


    @Override
    public DeliveryFirebaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        Context context = parent.getContext();
        return new DeliveryFirebaseAdapter.ViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(final DeliveryFirebaseAdapter.ViewHolder holder, int position) {
        holder.mDelivery = getItem(position);
        final Context mContext = holder.getContext();


        //get data
        String mDate = holder.mDelivery.getDate();
        String mTime = holder.mDelivery.getTime();
        String mDeliveredBy = holder.mDelivery.getDeliveryMan();
        String mRefNo = holder.mDelivery.getReferenceNo();
        String mLocation = holder.mDelivery.getLocation();
        String mImage = holder.mDelivery.getImage();

        //set data
        holder.mTxtDate.setText(mContext.getString(R.string.mdtp_date) + ": " + mDate);
        holder.mItem1.setText(mContext.getString(R.string.location) + ": " + mLocation);
        holder.mItem2.setText(mContext.getString(R.string.delivered_by) + ": " + mDeliveredBy);
        holder.mNumber.setText(mContext.getString(R.string.reference) + ": " + mRefNo);
        //TODO set image dynamically
        holder.mItemImageView.setImageResource(R.drawable.receipt);

        final String details = Utility.getDeliveryDetails(mContext, mDate, mTime, mDeliveredBy, mRefNo, mLocation);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ItemListActivity.class);
                //put extras
                intent.putExtra(ItemListActivity.DETAILS, details);//whole delivery details
                intent.putExtra(ItemListFragment.DELIVERY, holder.mDelivery);//delivery object
                intent.putExtra(ItemListActivity.BASE64IMAGE, ""); //todo replace null string for image
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    protected void itemAdded(Delivery item, String key, int position) {
        Log.d(LOG_TAG, "Added a new item to the adapter.");
    }

    @Override
    protected void itemChanged(Delivery oldItem, Delivery newItem, String key, int position) {
        Log.d(LOG_TAG, "Changed an item.");
        notifyDataSetChanged();
    }

    @Override
    protected void itemRemoved(Delivery item, String key, int position) {
        Log.d(LOG_TAG, "Removed an item from the adapter.");
    }

    @Override
    protected void itemMoved(Delivery item, String key, int oldPosition, int newPosition) {
        Log.d(LOG_TAG, "Moved an item.");
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final Context mContext;
        final ImageView mItemImageView;
        final TextView mTxtDate;
        final TextView mItem1;
        final TextView mItem2;
        final TextView mNumber;

        Delivery mDelivery;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mView = itemView;
            mContext = context;
            mItemImageView = (ImageView) itemView.findViewById(R.id.img_item);
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
