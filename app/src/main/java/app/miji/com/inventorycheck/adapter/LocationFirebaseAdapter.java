package app.miji.com.inventorycheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.Query;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Location;

/**
 * Created by isse on 31/01/2017.
 */

public class LocationFirebaseAdapter extends FirebaseRecyclerAdapter<LocationFirebaseAdapter.ViewHolder, Location> {
    private static final String LOG_TAG = LocationFirebaseAdapter.class.getSimpleName();

    public LocationFirebaseAdapter(Query query, Class<Location> itemClass) {
        super(query, itemClass);
    }

    @Override
    public LocationFirebaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new LocationFirebaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationFirebaseAdapter.ViewHolder holder, int position) {
        holder.mLocation = getItem(position);

        //get data
        String mName = holder.mLocation.getName();

        //set data
        holder.mTxtName.setText(mName);
    }

    @Override
    protected void itemAdded(Location item, String key, int position) {
        Log.d(LOG_TAG, "Added a new item to the adapter.");
    }

    @Override
    protected void itemChanged(Location oldItem, Location newItem, String key, int position) {
        Log.d(LOG_TAG, "Changed an item.");
    }

    @Override
    protected void itemRemoved(Location item, String key, int position) {
        Log.d(LOG_TAG, "Removed an item from the adapter.");
    }

    @Override
    protected void itemMoved(Location item, String key, int oldPosition, int newPosition) {
        Log.d(LOG_TAG, "Moved an item.");
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
