package app.miji.com.inventorycheck.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.NewItemRecyclerViewAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewItemsFragment extends Fragment {

    private NewItemRecyclerViewAdapter mAdapter;
    private int recyclerviewItemQty = 1;

    public NewItemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_new_items, container, false);


        //setup recyclerview
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_new_items);
        int mColumnCount = getResources().getInteger(R.integer.list_column_count);
        ;
        //set Layout Manager
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        assert recyclerView != null;
        setupRecyclerView(recyclerView);


        //setup fab
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add another item in recyclerview
                setupRecyclerView(recyclerView);


            }
        });


        return view;
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        mAdapter = new NewItemRecyclerViewAdapter(getContext(), recyclerviewItemQty++);
        recyclerView.setAdapter(mAdapter);
        //scroll to last item
        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
    }
}
