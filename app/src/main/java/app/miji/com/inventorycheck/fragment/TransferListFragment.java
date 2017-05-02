package app.miji.com.inventorycheck.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.TransferFirebaseAdapter;
import app.miji.com.inventorycheck.model.Transfer;


public class TransferListFragment extends Fragment {

    private static final String LOG_TAG = TransferListFragment.class.getSimpleName();

    //for determining floating label for  location label
    //FLAG 0: from StockInActivity
    //FLAG 1: from StockOutActivity
    public static final String FLAG = "location_label";
    private int flag;
    private List<Transfer> list;

    //firebase database variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private TransferFirebaseAdapter mFirebaseAdapter;

    public TransferListFragment() {
        // Required empty public constructor
    }

    public static TransferListFragment newInstance(int activityFlag) {
        TransferListFragment fragment = new TransferListFragment();
        Bundle args = new Bundle();
        args.putInt(FLAG, activityFlag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flag = getArguments().getInt(FLAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer_list, container, false);

        //inflate views
        //MaterialBetterSpinner spinnerLocation = (MaterialBetterSpinner) view.findViewById(R.id.spinner_location);
        //AutoCompleteTextView txtItem = (AutoCompleteTextView) view.findViewById(R.id.txt_item);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_transfer);

        //setup location spinner
        //Utility.setupLocationSpinner(getActivity(), spinnerLocation);


        int mColumnCount = getResources().getInteger(R.integer.list_stockTake_column_count);
        //set Layout Manager
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        setupFirebaseRecyclerView(recyclerView);

        return view;
    }


    private void setupFirebaseRecyclerView(RecyclerView recyclerView) {
        Log.e(LOG_TAG, "FROM ACTIVITY===============>" + flag);

        //Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        switch (flag) {
            case 0:
                mDatabaseReference = mFirebaseDatabase.getReference().child("transfer_stock_in");
                break;
            case 1:
                mDatabaseReference = mFirebaseDatabase.getReference().child("transfer_stock_out");
                break;
        }

        //The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners.
        mDatabaseReference.keepSynced(true);


        //sort data by date
        Query query = mDatabaseReference.orderByChild("date");

        mFirebaseAdapter = new TransferFirebaseAdapter(query, Transfer.class);
        recyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseAdapter.notifyDataSetChanged();
    }

}
