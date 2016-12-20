package app.miji.com.inventorycheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.object.HomeContent.HomeItem;


public class MyHomeItemRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeItemRecyclerViewAdapter.ViewHolder> {

    private final List<HomeItem> mValues;

    public MyHomeItemRecyclerViewAdapter(List<HomeItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String title = mValues.get(position).title;
        Integer thumbId = mValues.get(position).thumbnail;

        holder.mTitleView.setText(title);
        holder.mImageView.setImageResource(thumbId);
    }

    @Override
    public int getItemCount() {
        if (mValues != null)
            return mValues.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImageView;
        final TextView mTitleView;
        HomeItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.thumbnail);
            mTitleView = (TextView) view.findViewById(R.id.home_title);
        }

    }
}
