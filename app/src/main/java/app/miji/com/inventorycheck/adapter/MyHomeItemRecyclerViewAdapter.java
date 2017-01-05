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

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.activity.ProductActivity;
import app.miji.com.inventorycheck.activity.StockOutActivity;
import app.miji.com.inventorycheck.activity.StockInActivity;
import app.miji.com.inventorycheck.model.HomeContent.HomeItem;


public class MyHomeItemRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeItemRecyclerViewAdapter.ViewHolder> {

    private final List<HomeItem> mValues;
    private Context mContext;

    public MyHomeItemRecyclerViewAdapter(Context context, List<HomeItem> items) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        String title = mValues.get(position).title;
        Integer thumbId = mValues.get(position).thumbnail;

        holder.mTitleView.setText(title);
        holder.mImageView.setImageResource(thumbId);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start another activity
                Intent appInfo;
                switch (position) {
                    case 0:
                        //STOCK IN
                        appInfo = new Intent(mContext, StockInActivity.class);
                        mContext.startActivity(appInfo);
                        break;
                    case 1:
                        //STOCK OUT
                        appInfo = new Intent(mContext, StockOutActivity.class);
                        mContext.startActivity(appInfo);
                        break;
                    case 2:
                        //TODO: start STOCK TAKE
                        break;
                    case 3:
                        //TODO: start INVENTORY ADJUSTMENT
                        break;
                    case 4:
                        appInfo = new Intent(mContext, ProductActivity.class);
                        mContext.startActivity(appInfo);
                        break;
                    case 5:
                        //TODO: start REPORT
                        break;
                    case 6:
                        //TODO: start SETUP
                        break;
                }
            }
        });
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
