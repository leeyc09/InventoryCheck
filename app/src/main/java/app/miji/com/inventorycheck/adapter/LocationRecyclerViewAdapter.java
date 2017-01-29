package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Location;

/**
 * Adapter for location list
 */

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Location> list;

    public LocationRecyclerViewAdapter(Context context, List<Location> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public LocationRecyclerViewAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new LocationRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mLocation = list.get(position);

        //get data
        String mName = list.get(position).getName();

        //set data
        holder.mTxtName.setText(mName);

    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final TextView mTxtName;

        Location mLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTxtName = (TextView) itemView.findViewById(R.id.txt_name);
        }
    }
}
