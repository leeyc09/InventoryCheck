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
 * Created by isse on 18/01/2017.
 */

public class TransferRecyclerViewAdapter extends RecyclerView.Adapter<TransferRecyclerViewAdapter.ViewHolder> {
    Context mContext;

    public TransferRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_in, parent, false);
        return new TransferRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //TODO change dummy text
        String mDate = "02/14/2017";
        String mFrom = "Orchard";
        String mTo = "Raffles City";
        String mTransferId = "0000012";

        holder.mTxtDate.setText(mContext.getString(R.string.mdtp_date) + ": " + mDate);
        holder.mItem1.setText(mContext.getString(R.string.from) + ": " + mFrom);
        holder.mItem2.setText(mContext.getString(R.string.to) + ": " + mTo);
        holder.mNumber.setText(mContext.getString(R.string.trans_id) + ": " + mTransferId);
        holder.mItemImageView.setVisibility(View.GONE);

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

        //TODO make Transfer model class

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
