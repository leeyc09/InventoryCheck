package app.miji.com.inventorycheck.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.SalesFirebaseAdapter;
import app.miji.com.inventorycheck.model.Sales;

public class SalesListFragment extends Fragment {

    //firebase database variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private SalesFirebaseAdapter mFirebaseAdapter;

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

        //setupRecyclerView(recyclerView);
        setupFirebaseRecyclerView(recyclerView);

        return view;
    }

    private void setupFirebaseRecyclerView(RecyclerView recyclerView) {

        //Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("sales");

        //The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners.
        mDatabaseReference.keepSynced(true);


        //sort data by date
        Query query = mDatabaseReference.orderByChild("date");

        mFirebaseAdapter = new SalesFirebaseAdapter(query, Sales.class);
        recyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseAdapter.notifyDataSetChanged();
    }

}
