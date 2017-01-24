package app.miji.com.inventorycheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.miji.com.inventorycheck.adapter.ItemRecyclerViewAdapter;
import app.miji.com.inventorycheck.model.Delivery;
import app.miji.com.inventorycheck.model.Item;

/**
 * A placeholder fragment containing a simple view.
 */
public class ItemListFragment extends Fragment {


    private List<Item> list;
    private Delivery mDelivery;

    private ItemRecyclerViewAdapter mAdapter;
    public static final String DELIVERY = "delivery";


    public ItemListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_item_list);

        //setup recyclerview
        int mColumnCount = getResources().getInteger(R.integer.list_item_column_count);
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
        //get list of items from previous fragment
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            mDelivery = intent.getParcelableExtra(DELIVERY);
            list = mDelivery.getItems();
        }

        mAdapter = new ItemRecyclerViewAdapter(getActivity(), list);
        recyclerView.setAdapter(mAdapter);
    }
}
