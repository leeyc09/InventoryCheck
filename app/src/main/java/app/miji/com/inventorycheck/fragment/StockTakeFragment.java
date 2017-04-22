package app.miji.com.inventorycheck.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.StockTakeRecyclerViewAdapter;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * A placeholder fragment containing a simple view.
 */
public class StockTakeFragment extends Fragment {


    StockTakeRecyclerViewAdapter mAdapter;

    public StockTakeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_take, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_items);

        int mColumnCount = getResources().getInteger(R.integer.list_stockTake_column_count);
        //set Layout Manager
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        setupRecyclerView(recyclerView);
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mAdapter = new StockTakeRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);
    }
}
