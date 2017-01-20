package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.miji.com.inventorycheck.ItemListActivity;
import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Delivery;

/**
 * For Delivery List items
 */

public class DeliveryRecyclerViewAdapter extends RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder> {
    Context mContext;
    List<Delivery> list;

    public DeliveryRecyclerViewAdapter(Context context, List<Delivery> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new DeliveryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mDelivery = list.get(position);


        //get data
        String mDate = list.get(position).getDate();
        String mTime = list.get(position).getTime();
        String mDeliveredBy = list.get(position).getDeliveryMan();
        String mRefNo = list.get(position).getReferenceNo();
        String mLocation = list.get(position).getLocation();
        String mImage = list.get(position).getImage();

        //set data
        holder.mTxtDate.setText(mContext.getString(R.string.mdtp_date) + ": " + mDate);
        holder.mItem1.setText(mContext.getString(R.string.location) + ": " + mLocation);
        holder.mItem2.setText(mContext.getString(R.string.delivered_by) + ": " + mDeliveredBy);
        holder.mNumber.setText(mContext.getString(R.string.reference) + ": " + mRefNo);
        //TODO set image dynamically
        holder.mItemImageView.setImageResource(R.drawable.receipt);

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
        if (list != null)
            return list.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mItemImageView;
        final TextView mTxtDate;
        final TextView mItem1;
        final TextView mItem2;
        final TextView mNumber;

        Delivery mDelivery;

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
