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
import app.miji.com.inventorycheck.activity.NewItemsActivity;
import app.miji.com.inventorycheck.fragment.ItemListFragment;
import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.fragment.NewItemsFragment;
import app.miji.com.inventorycheck.model.Transfer;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * For Transfer list item
 */

public class TransferRecyclerViewAdapter extends RecyclerView.Adapter<TransferRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private int flagActivity;
    private List<Transfer> list;

    public TransferRecyclerViewAdapter(Context context, int flag, List<Transfer> list) {
        this.mContext = context;
        this.flagActivity = flag;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer, parent, false);
        return new TransferRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mTransfer = list.get(position);

        //get data
        final String mDate = list.get(position).getDate();
        final String mTime = list.get(position).getTime();
        final String mFrom = list.get(position).getFromLocation();
        final String mTo = list.get(position).getToLocation();
        final String mTransferId = list.get(position).getTransferID();

        final String details = Utility.getTransferDetails(mContext, mDate, mTime, mTransferId, mFrom, mTo);


        //set text
        holder.mTxtDate.setText(mContext.getString(R.string.mdtp_date) + ": " + mDate);
        holder.mNumber.setText(mContext.getString(R.string.trans_id) + ": " + mTransferId);


        //determine if request is from stock in or stock out activity
        final String to = mContext.getString(R.string.to) + ": " + mTo;
        final String from = mContext.getString(R.string.from) + ": " + mFrom;
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
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTxtDate;
        final TextView mItem1;
        final TextView mItem2;
        final TextView mNumber;

        Transfer mTransfer;

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
