package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.miji.com.inventorycheck.R;

/**
 * For Delivery List items
 */

public class DeliveryRecyclerViewAdapter extends RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder> {
    Context mContext;

    public DeliveryRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new DeliveryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //TODO change dummy text
        String mDate = "02/14/2017";
        String mLocation = "Marina Bay";
        String mDeliveredBy = "Usui Takumi";
        String mRefNo = "0000012";

        holder.mTxtDate.setText(mContext.getString(R.string.mdtp_date) + ": " + mDate);
        holder.mItem1.setText(mContext.getString(R.string.location) + ": " + mLocation);
        holder.mItem2.setText(mContext.getString(R.string.delivered_by) + ": " + mDeliveredBy);
        holder.mNumber.setText(mContext.getString(R.string.reference) + ": " + mRefNo);
        holder.mItemImageView.setImageResource(R.drawable.receipt);

    }

    @Override
    public int getItemCount() {
        //TODO change item count
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mItemImageView;
        final TextView mTxtDate;
        final TextView mItem1;
        final TextView mItem2;
        final TextView mNumber;

        //TODO make Delivery model class
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemImageView = (ImageView) itemView.findViewById(R.id.img_item);
            mTxtDate = (TextView) itemView.findViewById(R.id.txt_date);
            mItem1 = (TextView) itemView.findViewById(R.id.txt_text_one);
            mItem2 = (TextView) itemView.findViewById(R.id.txt_text_two);
            mNumber = (TextView) itemView.findViewById(R.id.txt_number);


        }
    }
}
