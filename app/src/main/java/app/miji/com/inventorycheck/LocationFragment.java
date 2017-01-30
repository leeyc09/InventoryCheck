package app.miji.com.inventorycheck;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import app.miji.com.inventorycheck.adapter.LocationRecyclerViewAdapter;
import app.miji.com.inventorycheck.model.Location;
import app.miji.com.inventorycheck.utility.Utility;

/**
 * A placeholder fragment containing location list
 */
public class LocationFragment extends Fragment {


    private static final String LOG_TAG = LocationFragment.class.getSimpleName();
    private LocationRecyclerViewAdapter mAdapter;
    private List<Location> list;


    //receive events about changes in the child locations of a given DatabaseReference
    private ChildEventListener mChildEventListener;

    //firebase database variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    public LocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        //Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        mDatabaseReference = mFirebaseDatabase.getReference().child("location");
        //The Firebase Realtime Database synchronizes and stores a local copy of the data for active listeners.
        mDatabaseReference.keepSynced(true);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context mContext = getActivity();
                final LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_location_input, null);
                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

                //show location dialog box
                Utility.showLocationDialogBox(mContext, mView, userInputDialogEditText, layoutInflaterAndroid, mDatabaseReference, mFirebaseDatabase);
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_location);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void attachDatabaseReadListener() {

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Location location = dataSnapshot.getValue(Location.class);

                    Log.e(LOG_TAG, "LOCATION FROM DB -------->  :  " + location.getName());

                    list.add(location);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(LOG_TAG, databaseError.getMessage());
                }
            };

            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    public void setupRecyclerView(RecyclerView recyclerView) {
        list = new ArrayList<>();
        attachDatabaseReadListener();

        mAdapter = new LocationRecyclerViewAdapter(getActivity(), list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
