package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.miji.com.inventorycheck.R;

/**
 * For Transfer list item
 */

public class TransferRecyclerViewAdapter extends RecyclerView.Adapter<TransferRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private int flagActivity;

    public TransferRecyclerViewAdapter(Context context, int flag) {
        this.mContext = context;
        this.flagActivity = flag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer, parent, false);
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
        holder.mNumber.setText(mContext.getString(R.string.trans_id) + ": " + mTransferId);

        //determine if request is from stock in or stock out activity
        String to = mContext.getString(R.string.to) + ": " + mTo;
        String from = mContext.getString(R.string.from) + ": " + mFrom;
        switch (flagActivity) {
            case 0:
                //from StockInActivity
                holder.mItem1.setText(from);
                holder.mItem2.setText(to);
                break;
            case 1:
                //from StockOutActivity
                holder.mItem1.setText(to);
                holder.mItem2.setText(from);
                break;
        }


    }

    @Override
    public int getItemCount() {
        //TODO change item count
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTxtDate;
        final TextView mItem1;
        final TextView mItem2;
        final TextView mNumber;

        //TODO make Transfer model class

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTxtDate = (TextView) itemView.findViewById(R.id.txt_date);
            mItem1 = (TextView) itemView.findViewById(R.id.txt_text_one);
            mItem2 = (TextView) itemView.findViewById(R.id.txt_text_two);
            mNumber = (TextView) itemView.findViewById(R.id.txt_number);

        }
    }
}
