package app.miji.com.inventorycheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.Query;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Unit;

/**
 * Created by isse on 31/01/2017.
 */

public class UnitFirebaseAdapter extends FirebaseRecyclerAdapter<UnitFirebaseAdapter.ViewHolder, Unit> {
    private static final String LOG_TAG = UnitFirebaseAdapter.class.getSimpleName();

    public UnitFirebaseAdapter(Query query, Class<Unit> itemClass) {
        super(query, itemClass);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mUnit = getItem(position);

        //get data
        String mName = holder.mUnit.getName();


        //set data
        holder.mTxtName.setText(mName);
    }

    @Override
    protected void itemAdded(Unit item, String key, int position) {
        Log.d(LOG_TAG, "Added a new item to the adapter.");
    }

    @Override
    protected void itemChanged(Unit oldItem, Unit newItem, String key, int position) {
        Log.d(LOG_TAG, "Changed an item.");
    }

    @Override
    protected void itemRemoved(Unit item, String key, int position) {
        Log.d(LOG_TAG, "Removed an item from the adapter.");
    }

    @Override
    protected void itemMoved(Unit item, String key, int oldPosition, int newPosition) {
        Log.d(LOG_TAG, "Moved an item.");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTxtName;

        Unit mUnit;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTxtName = (TextView) itemView.findViewById(R.id.txt_name);
        }
    }
}
