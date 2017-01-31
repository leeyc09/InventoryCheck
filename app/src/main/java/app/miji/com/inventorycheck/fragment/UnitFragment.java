package app.miji.com.inventorycheck.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.UnitFirebaseAdapter;
import app.miji.com.inventorycheck.model.Unit;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * A placeholder fragment containing list of unit from firebase database
 */
public class UnitFragment extends Fragment {

    //firebase database variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private UnitFirebaseAdapter mAdapter;

    public UnitFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit, container, false);

        //Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("unit");
        //The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners.
        mDatabaseReference.keepSynced(true);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context mContext = getActivity();
                final LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_unit_input, null);
                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

                //show location dialog box
                Utility.showUnitDialogBox(mContext, mView, userInputDialogEditText, layoutInflaterAndroid, mDatabaseReference);

            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_unit);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);


        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        //sort data alphabetically
        Query query = mDatabaseReference.orderByChild("name");

        mAdapter = new UnitFirebaseAdapter(query, Unit.class);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.destroy();
    }
}
