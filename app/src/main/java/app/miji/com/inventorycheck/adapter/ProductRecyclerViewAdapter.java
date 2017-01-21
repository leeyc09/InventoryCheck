package app.miji.com.inventorycheck.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Product;


/**
 * RecyclerView adapter enabling undo on a swiped away item.
 * tutorial:
 * http://nemanjakovacevic.net/blog/english/2016/01/12/recyclerview-swipe-to-delete-no-3rd-party-lib-necessary/
 */

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Product> items;

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    List<Product> itemsPendingRemoval;
    int lastInsertedIndex; // so we can add some more items for testing purposes
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public ProductRecyclerViewAdapter(Context mContext, List<Product> list) {
        this.mContext = mContext;
        this.items = list;
        itemsPendingRemoval = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_take, parent, false);
        return new ProductRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Product item = items.get(position);


        if (itemsPendingRemoval.contains(item)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.RED);
            holder.detailLayout.setVisibility(View.GONE);
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(items.indexOf(item));
                }
            });
        } else {
            // we need to show the "normal" state
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.detailLayout.setVisibility(View.VISIBLE);
            holder.undoButton.setVisibility(View.GONE);
            holder.undoButton.setOnClickListener(null);

            //set values
            //TODO change IMAGE
            holder.mImgItem.setImageResource(R.drawable.cake);
            holder.mTxtItem.setText(items.get(position).getName());
            //TODO quantity and unit depends on database count
            holder.mTxtQty.setText("30");
            holder.mTxtUnit.setText("pcs");
        }
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        return 0;
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final Product item = items.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(items.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item.getName(), pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        Product item = items.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (items.contains(item)) {
            items.remove(position);
            this.notifyDataSetChanged();
            notifyItemRemoved(position);
            //TODO: remove item in database
        }
    }

    public boolean isPendingRemoval(int position) {
        Product item = items.get(position);
        return itemsPendingRemoval.contains(item);
    }


    /**
     * ViewHolder capable of presenting two states: "normal" and "undo" state.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final ImageView mImgItem;
        final TextView mTxtItem;
        final TextView mTxtQty;
        final TextView mTxtUnit;
        final CoordinatorLayout detailLayout;
        final Button undoButton;

        Product mProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImgItem = (ImageView) mView.findViewById(R.id.img_item);
            mTxtItem = (TextView) mView.findViewById(R.id.txt_name);
            mTxtQty = (TextView) mView.findViewById(R.id.txt_qty);
            mTxtUnit = (TextView) mView.findViewById(R.id.txt_unit);

            detailLayout = (CoordinatorLayout) mView.findViewById(R.id.title_text_view);
            undoButton = (Button) mView.findViewById(R.id.undo_button);
        }

    }

}
