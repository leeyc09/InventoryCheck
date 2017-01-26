package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.SetupContent.SetupItem;


public class SetupRecyclerViewAdapter extends RecyclerView.Adapter<SetupRecyclerViewAdapter.ViewHolder> {

    private final List<SetupItem> mValues;
    private Context mContext;

    public SetupRecyclerViewAdapter(Context mContext, List<SetupItem> mValues) {
        this.mContext = mContext;
        this.mValues = mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String title = mValues.get(position).title;

        holder.mTxtSetup.setText(title);

        //TODO add onclick Listener

    }

    @Override
    public int getItemCount() {
        if (mValues != null)
            return mValues.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTxtSetup;

        SetupItem mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTxtSetup = (TextView) mView.findViewById(R.id.txt_setup);

        }
    }
}
