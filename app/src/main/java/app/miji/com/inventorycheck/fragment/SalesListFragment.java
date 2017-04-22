package app.miji.com.inventorycheck.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.SalesRecyclerViewAdapter;
import app.miji.com.inventorycheck.model.Sales;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesListFragment extends Fragment {

    private SalesRecyclerViewAdapter mAdapter;
    private List<Sales> list;

    public SalesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sales_list, container, false);

        //inflate views
       RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_sales);


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
        list = new ArrayList<>();
        list = Utility.getSalesData(getContext());

        mAdapter = new SalesRecyclerViewAdapter(getActivity(),list);
        recyclerView.setAdapter(mAdapter);
    }

}
